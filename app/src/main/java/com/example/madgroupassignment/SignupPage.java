package com.example.madgroupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupPage extends AppCompatActivity {

    EditText signupuser,signupemail,signupaddress,signuppassword,signuprepeatpassword;
    TextView haveaccount;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        signupuser = findViewById(R.id.signup_username);
        signupemail = findViewById(R.id.signup_email);
        signupaddress = findViewById(R.id.signup_address);
        signuppassword = findViewById(R.id.signup_password);
        signuprepeatpassword = findViewById(R.id.signup_rpassword);
        haveaccount = findViewById(R.id.haveaccount);
        register = findViewById(R.id.registerbutton);


        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginPage.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finish();
            }
        });
    }
}