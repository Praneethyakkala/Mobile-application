package com.bmlmunjal.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp_activity extends AppCompatActivity {

    private Button buttonSignUp;
    private EditText firstName,lastName,UserName,EmailPhone,password,repassword;
    private TextView login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        buttonSignUp= findViewById(R.id.SignUpButtonSignUpActivity);

        login =findViewById(R.id.login_signup);

        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        UserName = findViewById(R.id.username);
        EmailPhone = findViewById(R.id.email_signup);
        password = findViewById(R.id.pass);
        repassword = findViewById(R.id.re_password);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            startActivity(new Intent(SignUp_activity.this, Login_Activity.class));
        }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();

            }
        });
    }
    private void PerforAuth() {

        String First = firstName.getText().toString();
        String Last = lastName.getText().toString();
        String User = UserName.getText().toString();
        String Email = EmailPhone.getText().toString();
        String Password = password.getText().toString();
        String RePassword = repassword.getText().toString();

        if(First.isEmpty()){
            firstName.setError("This can't be empty");
        }
        else if(Last.isEmpty()){
            lastName.setError("This can't be empty");
        }
        else if(User.isEmpty()){
            UserName.setError("Please enter Username");
        }
        else if (!Email.matches(emailPattern)){

            EmailPhone.setError("Enter correct Email");
        }
        else if(Password.isEmpty() || Password.length()<6){
            password.setError("Enter proper password");
        }
        else if(!Password.equals(RePassword)){
            repassword.setError("Passwords donot match");
        }
        else{
            progressDialog.setMessage("please wait while we Registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(SignUp_activity.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SignUp_activity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(SignUp_activity.this, profile_page.class);
        startActivity(intent);
    }

}