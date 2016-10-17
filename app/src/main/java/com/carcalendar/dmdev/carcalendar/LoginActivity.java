package com.carcalendar.dmdev.carcalendar;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.UserManager;

public class LoginActivity extends AppCompatActivity {

    private EditText emailET;
    private EditText passET;
    private Button loginBtn;
    private Button registerBTn;
    public static final int DATA_OKEY = 0;
    public static final int BAD_DATA = -1;
    public static final int NO_DATA = -10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailET = (EditText) findViewById(R.id.emailET);
        passET = (EditText) findViewById(R.id.passET);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBTn = (Button) findViewById(R.id.regBtn);
        final UserManager manager = UserManager.getInstance();
        registerBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toReg = new Intent(view.getContext(),RegisterActivity.class);
                startActivityForResult(toReg,DATA_OKEY);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(manager.authenticateLogin(emailET.getText().toString(),passET.getText().toString())){
                   Intent toMain = new Intent(view.getContext(),GarageActivity.class);
                   startActivity(toMain);
               }
                else{
                   Toast.makeText(view.getContext(),"Wrong data or not registered !",Toast.LENGTH_SHORT).show();
               }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DATA_OKEY){
            if(resultCode == DATA_OKEY){
                emailET.setText(data.getStringExtra("Email"));
                passET.setText(data.getStringExtra("Password"));
            }
            if(resultCode == NO_DATA){
                emailET.setHint(R.string.enter_e_mail);
                passET.setHint(R.string.enter_password);
            }
        }
    }
}
