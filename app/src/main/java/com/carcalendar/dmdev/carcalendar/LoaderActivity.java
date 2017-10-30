package com.carcalendar.dmdev.carcalendar;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carcalendar.dmdev.carcalendar.utils.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;

import model.UserManager;
import model.Vehicle.Vehicle;
import model.util.ImageUtils;

public class LoaderActivity extends AppCompatActivity {

    private ProgressBar bar;
    private TextView loadingTV;
    private final UserManager manager = UserManager.getInstance();
    public static final String USERMANAGER_FILE_STORAGE = "UsermanagerDATA.txt";
    private boolean user_manager_saved_successfully = false;

    public static boolean DatabaseAvailable(Context context) {
        File file = context.getDatabasePath(DatabaseHelper.DATABASE_NAME);
        if (file.exists()) {
            return true;
        } else return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        String path = this.getFilesDir().getAbsolutePath() + "/" + USERMANAGER_FILE_STORAGE;
        bar = (ProgressBar) findViewById(R.id.progressBar);
        loadingTV = (TextView) findViewById(R.id.loadingTV);
        manager.setDbContext(getApplicationContext());
        if (DatabaseAvailable(this)) {
            ManagerLoader loader = new ManagerLoader();
            loader.execute();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class ManagerLoader extends AsyncTask<Void, Void, Boolean> {

        ManagerLoader() {
        }

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
            loadingTV.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (DatabaseAvailable(getApplicationContext())) {

                if (manager.getLoggedUserFromDB() != null) {
                    ArrayList<Vehicle> list = (ArrayList<Vehicle>) manager.getRegisteredUserVehiclesFromDB();
                    if(list == null) {
                        return true;
                    }

                    for (Vehicle x : list) {
                        ImageUtils.mapImageToVehicle(x, ImageUtils.getBitmapFromPath(x.getPathToImage()));
                    }
                    return true;
                }
                return false;

            }

            return false;
    }

    @Override
    protected void onPostExecute(Boolean flag) {

        Intent intent = null;
        if (flag) {
            intent = new Intent(LoaderActivity.this, GarageActivity.class);
        } else {
            intent = new Intent(LoaderActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
}


