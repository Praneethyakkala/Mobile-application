package com.bmlmunjal.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_Activity extends AppCompatActivity {

    private Button ResetBtn;
    private EditText email_forgot1;
    private String email_forgot2;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        auth = FirebaseAuth.getInstance();

        email_forgot1 = findViewById(R.id.email_forgot);
        ResetBtn = findViewById(R.id.rstbutton);

        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });



    }

    private void validateData() {
        email_forgot2 = email_forgot1.getText().toString();
        if (email_forgot2.isEmpty()){
            email_forgot1.setError("Required");
        }else{
            forgetPass();
        }
    }

    private void forgetPass() {
        auth.sendPasswordResetEmail(email_forgot2)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(forgot_Activity.this, "Email sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(forgot_Activity.this,Login_Activity.class));
                            finish();
                        }else{
                            Toast.makeText(forgot_Activity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}