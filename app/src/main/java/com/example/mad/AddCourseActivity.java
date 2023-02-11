package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddCourseActivity extends AppCompatActivity {

    private final String[] year={"1","2","3","4"},sem={"1","2"},credits={"1","2","3","4"};
    private String yearStr="",semStr="",cc="",ct="",cred="";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        Spinner yearSpin = findViewById(R.id.spinnerYear);
        Spinner semSpin=findViewById(R.id.spinnerSem);
        Spinner creditSpin=findViewById(R.id.creditSpinner);
        EditText courseCode=findViewById(R.id.addCourseCode),courseTitle=findViewById(R.id.addCourseTitle);
        Button sub=findViewById(R.id.submitCourse);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,year);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(arrayAdapter);

        ArrayAdapter<String> aa=new ArrayAdapter<>(AddCourseActivity.this,android.R.layout.simple_spinner_item,sem);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpin.setAdapter(aa);

        ArrayAdapter<String> aa1=new ArrayAdapter<>(AddCourseActivity.this,android.R.layout.simple_spinner_item,credits);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creditSpin.setAdapter(aa1);

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

        creditSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cred=credits[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Adding course");
                progressDialog.show();
                cc=courseCode.getText().toString().trim();
                ct=courseTitle.getText().toString().trim();

                String uid= firebaseAuth.getUid();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("code",""+cc);
                hashMap.put("title",""+ct);
                hashMap.put("credits",""+cred);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Course");
                reference.child(yearStr)
                        .child(semStr)
                        .child(cc).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(AddCourseActivity.this, "Course added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddCourseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}