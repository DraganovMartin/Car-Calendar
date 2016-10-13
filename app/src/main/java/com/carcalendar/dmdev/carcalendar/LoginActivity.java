package com.carcalendar.dmdev.carcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText passET;
    private Button loginBtn;
    private Button registerBTn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameET = (EditText) findViewById(R.id.usernameET);
        passET = (EditText) findViewById(R.id.passET);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBTn = (Button) findViewById(R.id.regBtn);

    }
}
