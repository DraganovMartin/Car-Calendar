package com.carcalendar.dmdev.carcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import model.UserManager;
import model.authentication.RunningStatus;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText passET;
    private Button loginBtn;
    private Button registerBTn;
    public static final int DATA_OKEY = 0;
    public static final int BAD_DATA = -1;
    public static final int NO_DATA = -10;
    private UserManager manager = UserManager.getInstance();
    private RunningStatus runStatus = RunningStatus.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(checkAvailableFile()){
            manager.updateFromFile(loadDataUserManager());
            if(manager.getLoggedUser() != null){
                Intent toMain = new Intent(this.getApplicationContext(),GarageActivity.class);
                finish();
                startActivity(toMain);
            }
        }else {
            manager = UserManager.getInstance();
        }
        usernameET = (EditText) findViewById(R.id.usernameET);
        passET = (EditText) findViewById(R.id.passET);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBTn = (Button) findViewById(R.id.regBtn);

        runStatus.setStatus(true);

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

    /**
     * Checks if UsermanagerDATA file is available
     * @return true or false
     */
    public boolean checkAvailableFile(){
        String path= this.getFilesDir().getAbsolutePath()+"/UsermanagerDATA.txt";
        File file = new File(path);
        if(file.exists()) return true;
        else return false;
    }

    /**
     * Checks if loggedUser is null file is available
     * @return true if it's not null, false otherwise
     */

    private UserManager loadDataUserManager(){
        /*Thread load = new Thread(new Runnable() {
            @Override
            public void run() {*/
        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput("UsermanagerDATA.txt"));
           UserManager managerTmp = (UserManager) in.readObject();
            return managerTmp;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
            /*}
        });
        load.start();
        */
        return null;
    }

    @Override
    protected void onDestroy() {
        runStatus.setStatus(false);
        super.onDestroy();
    }
}
