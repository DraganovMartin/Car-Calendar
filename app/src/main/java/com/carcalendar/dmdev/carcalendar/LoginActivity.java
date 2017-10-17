package com.carcalendar.dmdev.carcalendar;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import model.UserManager;
import model.Vehicle.Vehicle;
import model.util.ImageUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText passET;
    private Button loginBtn;
    private Button registerBTn;
    private ProgressBar progressBar;
    public static final int DATA_OKEY = 0;
    public static final int BAD_DATA = -1;
    public static final int NO_DATA = -10;
    private UserManager manager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(LoaderActivity.userManagerFileAvailable(this)){
            if(manager.getLoggedUser() != null){
                Intent toMain = new Intent(this.getApplicationContext(),GarageActivity.class);
                finish();
                startActivity(toMain);
            }
        }

        usernameET = (EditText) findViewById(R.id.usernameET);
        passET = (EditText) findViewById(R.id.passET);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBTn = (Button) findViewById(R.id.regBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
               if(manager.authenticateLogin(usernameET.getText().toString(),passET.getText().toString())){
                   // TODO : start AsyncTaskLoader to map images in login
                  // LoginMappe
                   UserManager.saveDataUserManager(view.getContext(),manager);
                   Intent toMain = new Intent(view.getContext(),GarageActivity.class);
                   startActivity(toMain);
                   finish();
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
                usernameET.setText(data.getStringExtra("Username"));
                passET.setText(data.getStringExtra("Password"));
            }
            if(resultCode == NO_DATA){
                usernameET.setHint(R.string.enter_username);
                passET.setHint(R.string.enter_password);
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class LoginMapper extends AsyncTaskLoader {
        public LoginMapper(Context context) {
            super(context);
        }

        @Override
        public Object loadInBackground() {
            ArrayList<Vehicle> list = (ArrayList<Vehicle>) manager.getRegisteredUserVehicles();
            for (Vehicle x : list) {
                ImageUtils.mapImageToVehicle(x, ImageUtils.getBitmapFromPath(x.getPathToImage()));
            }
            return null;
        }
    }

}

