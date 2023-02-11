package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button reg;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    EditText usn;
    RadioButton rb1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usn=findViewById(R.id.usnReg);
        reg=findViewById(R.id.registerSubmit);

        usn.setVisibility(View.INVISIBLE);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth=FirebaseAuth.getInstance();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        rb1=findViewById(R.id.studentRadio);
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    usn.setVisibility(View.VISIBLE);
                }
                else {
                    usn.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    private String nameStr="",emailStr="",passStr="",userType="",usnStr="";

    private void validateData() {
        EditText name=findViewById(R.id.nameReg),email=findViewById(R.id.emailReg),pass=findViewById(R.id.passReg);
        nameStr=name.getText().toString().trim();
        emailStr=email.getText().toString().trim();
        passStr=pass.getText().toString().trim();
        usnStr=usn.getText().toString().trim();
        RadioButton rb2=findViewById(R.id.profRadio);
        if(rb1.isChecked()) {
            userType="student";
        }
        else if(rb2.isChecked()) {
            userType="admin";
        }
        else {
            Toast.makeText(this, "Pick a designation", Toast.LENGTH_SHORT).show();
        }
        if(nameStr.length()==0) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
        else if(passStr.length()==0) {
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
        }
        else {
            makeUser();
        }
    }

    private void makeUser() {
        progressDialog.setMessage("Creating account");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailStr,passStr)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUserData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserData() {
        progressDialog.setMessage("Saving user data");
        long timeStamp=System.currentTimeMillis();

        String uid= firebaseAuth.getUid();
        HashMap<String,Object> hashMap=new HashMap<>();

        if(userType.equals("student")) {
            hashMap.put("uid",uid);
            hashMap.put("email",emailStr);
            hashMap.put("name",nameStr);
            hashMap.put("usn",usnStr);
            hashMap.put("year","");
            hashMap.put("sem","");
            hashMap.put("section","");
            hashMap.put("userType",userType);
            hashMap.put("timestamp",timeStamp);
        }
        else if(userType.equals("admin")) {
            hashMap.put("uid",uid);
            hashMap.put("email",emailStr);
            hashMap.put("name",nameStr);
            hashMap.put("userType",userType);
            hashMap.put("timestamp",timeStamp);
        }


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                        if(userType.equals("student")) {
                            startActivity(new Intent(RegisterActivity.this,CompleteProfileActivity.class));
                            finish();
                        }
                        else if(userType.equals("admin")) {
                            startActivity(new Intent(RegisterActivity.this,DashboardAdminActivity.class));
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}