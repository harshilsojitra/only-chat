package com.example.onch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onch.Activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot extends AppCompatActivity {

    EditText foremail;
    Button btnforgotpass;
    private  String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        foremail = findViewById(R.id.foremail);
        btnforgotpass = findViewById(R.id.btnforgotpass);

        btnforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email= foremail.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(forgot.this, "Please provide your email", Toast.LENGTH_SHORT).show();
                }else {
                    forgotPassword();
                }
            }
        });
    }

    private void forgotPassword() {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(forgot.this, "Check your Email", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(forgot.this, LoginActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(forgot.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}