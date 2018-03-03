package com.carcalendar.dmdev.carcalendar.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.carcalendar.dmdev.carcalendar.GarageActivity;
import com.carcalendar.dmdev.carcalendar.LoaderActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;

import model.UserManager;
import model.Vehicle.Vehicle;
import model.storage.Storable;

public class StorageManager extends IntentService implements Storable {

    public static final String USER_MANAGER_SAVED = "USER_MANAGER_SAVED";
    private final UserManager manager = UserManager.getInstance();

    public StorageManager() {
        super("StorageManager");
    }

    @Override
    public void store(Vehicle vehicle) {

    }

    @Override
    public Vehicle retrieve() {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (manager) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(openFileOutput(LoaderActivity.USERMANAGER_FILE_STORAGE, Context.MODE_PRIVATE));
                out.writeObject(intent.getSerializableExtra(UserManager.SAVE_USER_MANAGER));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}