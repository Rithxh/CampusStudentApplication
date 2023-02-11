package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ShowTimetableActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String file_path="";
    String semPath="",secPath="",yearPath="";
    TextView temp;

    String logTag="SHOW_TIMETABLE";

    ImageView timetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timetable);

        timetable=findViewById(R.id.timetableImage);


        firebaseAuth=FirebaseAuth.getInstance();
        getPath();



    }

    private void getPath() {

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        semPath=snapshot.child("sem").getValue().toString();
                        secPath=snapshot.child("section").getValue().toString();
                        yearPath=snapshot.child("year").getValue().toString();
                        file_path="Timetable/"+yearPath+"/"+semPath+"/"+secPath;
                        loadImage();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Log.d(logTag, file_path);
    }

    private void loadImage() {

        StorageReference storageReference= FirebaseStorage.getInstance().getReference(file_path);
        try {
            File localFile=File.createTempFile("temp",".jpeg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            timetable.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ShowTimetableActivity.this, "Unable to load image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}