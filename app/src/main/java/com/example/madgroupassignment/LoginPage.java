package com.example.madgroupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {
    EditText loginuser,loginpassword;
    TextView noaccount;
    Button loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginuser = findViewById(R.id.login_username);
        loginpassword = findViewById(R.id.login_password);
        noaccount = findViewById(R.id.donthaveaccount);
        loginbutton = findViewById(R.id.loginbutton);
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
                startActivity(new Intent(getApplicationContext(),Lobby.class));
            }
        });
    }
}