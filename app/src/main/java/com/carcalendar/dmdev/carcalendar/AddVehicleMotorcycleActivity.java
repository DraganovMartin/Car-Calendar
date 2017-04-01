package com.carcalendar.dmdev.carcalendar;

import android.app.DatePickerDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carcalendar.dmdev.carcalendar.dialogs.DatePickerFragment;

public class AddVehicleMotorcycleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_motorcycle);
    }
}
