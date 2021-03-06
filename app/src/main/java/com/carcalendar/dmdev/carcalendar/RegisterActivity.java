package com.carcalendar.dmdev.carcalendar;

import android.content.Intent;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.authentication.UsedUsernameException;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText ageET;
    private EditText passET;
   // private EditText emailET;
    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameET = (EditText) findViewById(R.id.regUserTextET);
        ageET = (EditText) findViewById(R.id.regAgeET);
        passET = (EditText) findViewById(R.id.regPassET);
        //emailET = (EditText) findViewById(R.id.regEmailET);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        final model.UserManager userManager = model.UserManager.getInstance();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(userManager.validateRegister(usernameET.getText().toString(),passET.getText().toString())){
                        int age = Integer.parseInt(ageET.getText().toString());
                        if(age < 16){
                            ageET.setError("You must be 16 and above");
                            return;
                        }
                        userManager.registerUser(userManager.createUser(usernameET.getText().toString(),passET.getText().toString(),age));
                        userManager.registerUserForDB(userManager.createUser(usernameET.getText().toString(),passET.getText().toString(),age));
                        Intent dataToLogin = new Intent();
                        dataToLogin.putExtra("Username",usernameET.getText().toString());
                        dataToLogin.putExtra("Password",passET.getText().toString());
                        setResult(LoginActivity.DATA_OKEY,dataToLogin);
                        finish();
                    }
                    else{
                        Toast.makeText(view.getContext(),"Weak password !",Toast.LENGTH_SHORT).show();
                    }
                } catch (UsedUsernameException e) {
                    Toast.makeText(view.getContext(),"Username already used !!!",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Logout:
                this.onBackPressed();
                break;
            case R.id.Help:
                Intent intent = new Intent(getApplicationContext(),RegisterHelp.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(LoginActivity.NO_DATA);
        finish();
    }
}
