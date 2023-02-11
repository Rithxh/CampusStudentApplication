package com.example.mad;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView headName,headUsn,sem,year,mail,sec;
    private int layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth=FirebaseAuth.getInstance();
        headName=findViewById(R.id.headerName);
        headUsn=findViewById(R.id.usnHeader);
        sem=findViewById(R.id.profileSem);
        sec=findViewById(R.id.profileSec);
        year=findViewById(R.id.profileYear);
        mail=findViewById(R.id.profileEmail);
        getData();

    }

    private void getData() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            headName.setText(snapshot.child("name").getValue().toString());
                            headUsn.setText(snapshot.child("usn").getValue().toString());
                            sem.setText("Sem :"+snapshot.child("sem").getValue().toString());
                            sec.setText("Section :"+snapshot.child("section").getValue().toString());
                            year.setText("Year : "+snapshot.child("year").getValue().toString());
                            mail.setText("Email : "+snapshot.child("email").getValue().toString());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
    }
}