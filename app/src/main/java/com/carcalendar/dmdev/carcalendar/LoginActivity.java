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
        if(checkLoggedUserState()){
            Intent toMain = new Intent(this.getApplicationContext(),GarageActivity.class);
            manager = loadDataUserManager();
            toMain.putExtra("UserManager",manager);
            finish();
            startActivity(toMain);
        }
        Log.e("run",String.valueOf(!runStatus.stillRunning()));
        if(new File(this.getFilesDir().getAbsolutePath()+"/UsermanagerDATA.txt").length() > 0 && !runStatus.stillRunning()){
            manager = loadDataUserManager();
            Log.e("marto",String.valueOf(manager.getRegisteredUsers().size()));
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
                   Log.e("setLogged","are you setting it well bitch : " + String.valueOf(manager.getLoggedUser() != null));
                   Intent toMain = new Intent(view.getContext(),GarageActivity.class);
                   manager.userLogin(manager.getLoggedUser());
                   toMain.putExtra("LoggedUserName",manager.getLoggedUserName());
                   finish();
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
     * Checks if loggedUser is null file is available
     * @return true if it's null, false otherwise
     */
    public boolean checkLoggedUserState(){
        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput("UsermanagerDATA.txt"));
            UserManager tmp = (UserManager) in.readObject();
            if(tmp.getLoggedUser() != null){
                Log.e("droide","inside if");
                return true;
            }
            else return false;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private UserManager loadDataUserManager(){
        /*Thread load = new Thread(new Runnable() {
            @Override
            public void run() {*/
        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput("UsermanagerDATA.txt"));
            manager = (UserManager) in.readObject();
            return manager;
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
