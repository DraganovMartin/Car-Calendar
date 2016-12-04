package com.carcalendar.dmdev.carcalendar;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import model.UserManager;

public class LoaderActivity extends AppCompatActivity {

    private ProgressBar bar;
    private TextView loadingTV;
    private UserManager manager = UserManager.getInstance();
    public static final String USERMANAGER_FILE_STORAGE = "UsermanagerDATA.txt";
    private boolean user_manager_saved_successfully = false;

    public static boolean userManagerFileAvailable(Context context){
        String path= context.getFilesDir().getAbsolutePath()+"/"+USERMANAGER_FILE_STORAGE;
        File file = new File(path);
        if (file.exists()) {
           return true;
        }
        else return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        String path= this.getFilesDir().getAbsolutePath()+"/"+USERMANAGER_FILE_STORAGE;
        bar = (ProgressBar) findViewById(R.id.progressBar);
        loadingTV = (TextView) findViewById(R.id.loadingTV);
            if (userManagerFileAvailable(this)) {
                ManagerLoader loader = new ManagerLoader();
                loader.execute();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
    }

    private class ManagerLoader extends AsyncTask<Void,Void,Void>
    {

        ManagerLoader() {}

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
            loadingTV.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            
            while (model.storage.StorageManager.isRunning){

            }

            if(userManagerFileAvailable(getApplicationContext())) {
                try {
                    String path= getApplicationContext().getFilesDir().getAbsolutePath()+"/"+USERMANAGER_FILE_STORAGE;
                    File file = new File(path);
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    manager.updateFromFile((UserManager)in.readObject());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(LoaderActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}


