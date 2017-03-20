package com.carcalendar.dmdev.carcalendar.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import model.util.ImageUtils;

public class ImageSaver extends IntentService {

    public ImageSaver() {
        super("ImageSaverService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            File externalStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if(externalStorage == null){
                Toast.makeText(getApplicationContext(),"Error on saving image",Toast.LENGTH_SHORT).show();
            }
            ImageUtils.createImageFile(externalStorage.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
