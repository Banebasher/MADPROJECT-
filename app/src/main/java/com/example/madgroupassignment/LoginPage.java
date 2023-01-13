package com.example.madgroupassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    EditText loginemail,loginpassword;
    TextView noaccount;
    Button loginbutton;
    ProgressBar loginprogress;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginprogress = findViewById(R.id.loginprogress);
        loginemail = findViewById(R.id.logine_email);
        loginpassword = findViewById(R.id.login_password);
        noaccount = findViewById(R.id.donthaveaccount);
        loginbutton = findViewById(R.id.loginbutton);
        auth= FirebaseAuth.getInstance();
        loginprogress.setVisibility(View.INVISIBLE);



        noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignupPage.class));
                finish();
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginprogress.setVisibility(View.VISIBLE);
                loginbutton.setVisibility(View.INVISIBLE);

                checkcredentials();

            }
        });
    }
    private  void checkcredentials(){

        String email = loginemail.getText().toString();
        String password = loginpassword.getText().toString();
        if (email.isEmpty()) {
            showError(loginemail, "Enter Email");
            loginbutton.setVisibility(View.VISIBLE);
            loginprogress.setVisibility(View.INVISIBLE);}
        else if (password.isEmpty()) {
            showError(loginemail, "Please Enter Password");
            loginbutton.setVisibility(View.VISIBLE);
            loginprogress.setVisibility(View.INVISIBLE);}

        else if (password.length() < 7) {
            showError(loginpassword, "Password must be 7 Characters");
            loginbutton.setVisibility(View.VISIBLE);
            loginprogress.setVisibility(View.INVISIBLE);}

        else
        {
            signIn(email,password);
        }

    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    loginprogress.setVisibility(View.VISIBLE);
                    loginbutton.setVisibility(View.INVISIBLE);
                    updateUI();
                }
                else
                {showMessage(task.getException().getMessage());
                    loginbutton.setVisibility(View.VISIBLE);
                    loginprogress.setVisibility(View.INVISIBLE);}

            }
        });

    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
    private void showMessage(String Msg)
    {
        Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {

        Intent intent  = new Intent(LoginPage.this,Lobby.class);
        startActivity(intent);
        finish();


    }


}