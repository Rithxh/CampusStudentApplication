package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAttendanceActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    ListView attendance;
    ArrayList<String> courses=new ArrayList<>();
    ArrayList<String> attended=new ArrayList<>();
    ArrayList<String> total=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        attendance=findViewById(R.id.attendanceList);
        firebaseAuth=FirebaseAuth.getInstance();
        getAttendance();
    }

    private void getAttendance() {

        courses.add("Course Code");
        attended.add("Attended");
        total.add("Conducted");

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot item:snapshot.child("courseDetails").getChildren()) {
                            courses.add(item.getKey().toString());
                            attended.add(item.child("Attended").getValue().toString());
                            total.add(item.child("Total").getValue().toString());
                        }
                        setMarks();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



    private void setMarks() {

        AttendanceAdapter attendanceAdapter=new AttendanceAdapter(getApplicationContext(),courses,attended,total);
        attendance.setAdapter(attendanceAdapter);

    }
}