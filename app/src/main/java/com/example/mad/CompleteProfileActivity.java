package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CompleteProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        EditText semUp,yearUp,secUp;
        semUp=findViewById(R.id.editProfileSem);
        yearUp=findViewById(R.id.editProfileYear);
        secUp=findViewById(R.id.editProfileSec);
        Button sub=findViewById(R.id.editProfileSubmit);

        firebaseAuth=FirebaseAuth.getInstance();


        sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String year=yearUp.getText().toString(),sem=semUp.getText().toString(),sec=secUp.getText().toString();
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                    HashMap<String,Object> courseDetails=new HashMap<>();
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("CIE1","0");
                    hashMap.put("CIE2","0");
                    hashMap.put("CIE3","0");
                    hashMap.put("Attended","0");
                    hashMap.put("Total","0");

                    DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("Course");
                    reference1.child(year).child(sem)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot item:snapshot.getChildren()) {
                                        courseDetails.put(item.child("code").getValue().toString(),hashMap);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().child("year").setValue(year);
                                    snapshot.getRef().child("sem").setValue(sem);
                                    snapshot.getRef().child("section").setValue(sec);
                                    snapshot.getRef().child("courseDetails").setValue(courseDetails);


                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    startActivity(new Intent(CompleteProfileActivity.this,DashboardUserActivity.class));

                }
            });


        }
}