package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardUserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);
        CardView tt = findViewById(R.id.timetableCard);
        CardView profile = findViewById(R.id.profileCard);
        CardView attend = findViewById(R.id.attendanceCard);
        CardView mark = findViewById(R.id.marksCard);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardUserActivity.this,ProfileActivity.class));
            }
        });

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardUserActivity.this,ShowTimetableActivity.class));
            }
        });

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardUserActivity.this,ViewMarksActivity.class));

            }
        });

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardUserActivity.this,ViewAttendanceActivity.class));

            }
        });
    }
}