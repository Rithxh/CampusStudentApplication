package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMarksActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    ListView marks;
    ArrayList<String> courses=new ArrayList<>();
    ArrayList<String> marks1=new ArrayList<>();
    ArrayList<String> marks2=new ArrayList<>();
    ArrayList<String> marks3=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_marks);
        marks=findViewById(R.id.marksList);
        firebaseAuth=FirebaseAuth.getInstance();
        getMarks();

    }

    private void setMarks() {
        MarksAdapter marksAdapter=new MarksAdapter(getApplicationContext(),courses,marks1,marks2,marks3);
        marks.setAdapter(marksAdapter);
    }

    private void getMarks() {

        courses.add("Course Code");
        marks1.add("CIE 1");
        marks2.add("CIE 2");
        marks3.add("CIE 3");

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot item:snapshot.child("courseDetails").getChildren()) {
                            courses.add(item.getKey().toString());
                            marks1.add(item.child("CIE1").getValue().toString());
                            marks2.add(item.child("CIE2").getValue().toString());
                            marks3.add(item.child("CIE3").getValue().toString());
                        }
                        setMarks();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}