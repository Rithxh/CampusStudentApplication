package com.example.mad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddTimetableActivity extends AppCompatActivity {

    private final String[] year={"1","2","3","4"},sem={"1","2"};
    private String yearStr="",semStr="",secStr="";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    private Uri pdfUri=null;
    private static final int PIC_PICK_CODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);

        Button sub=findViewById(R.id.submitTimetable),upload=findViewById(R.id.uploadTimetableButton);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        Spinner yearSpin = findViewById(R.id.spinnerYearTimetable);
        Spinner semSpin=findViewById(R.id.spinnerSemTimetable);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,year);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(arrayAdapter);

        ArrayAdapter<String> aa=new ArrayAdapter<>(AddTimetableActivity.this,android.R.layout.simple_spinner_item,sem);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpin.setAdapter(aa);

        EditText sec=findViewById(R.id.secUpload);

        yearSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sem[0]=Integer.toString(Integer.parseInt(year[i])*2-1);
                sem[1]=Integer.toString(Integer.parseInt(year[i])*2);
                yearStr=year[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        semSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                semStr=sem[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfPickIntent();

            }
        });
        
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secStr=sec.getText().toString().trim();
                validateData();
            }
        });
    }

    private void validateData() {
        if(yearStr.length()==0){
            Toast.makeText(this, "Pick the year", Toast.LENGTH_SHORT).show();
        }
        else if(semStr.length()==0){
            Toast.makeText(this, "Pick the semester", Toast.LENGTH_SHORT).show();
        }
        else if(secStr.length()==0){
            Toast.makeText(this, "Pick the section", Toast.LENGTH_SHORT).show();
        }
        else if(pdfUri==null){
            Toast.makeText(this, "Pick a file", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadPdfToStorage();
        }

    }

    private void uploadPdfToStorage() {
        progressDialog.setMessage("Uploading File");
        progressDialog.show();

        long timestamp=System.currentTimeMillis();
        String filePathAndName="Timetable/"+yearStr+"/"+semStr+"/"+secStr;

        StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String uploadedPdfUrl=""+uriTask.getResult();
                        uploadPdfInfoToDb(uploadedPdfUrl,timestamp);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTimetableActivity.this, "Upload failed due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadPdfInfoToDb(String uploadedPdfUrl,long timestamp) {
        String uid=firebaseAuth.getUid();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",""+uid);
        hashMap.put("url",""+uploadedPdfUrl);
        hashMap.put("timestamp",""+timestamp);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Timetable");
        databaseReference.child(yearStr)
                        .child(semStr)
                        .child(secStr).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(AddTimetableActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTimetableActivity.this, "Could not add to Db due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void pdfPickIntent() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select pdf"),PIC_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode== RESULT_OK) {
            if(requestCode==PIC_PICK_CODE) {
                pdfUri=data.getData();
                Toast.makeText(this, "File selected", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}