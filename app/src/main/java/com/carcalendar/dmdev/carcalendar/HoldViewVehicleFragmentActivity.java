package com.carcalendar.dmdev.carcalendar;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import model.Vehicle.Vehicle;

public class HoldViewVehicleFragmentActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hold_view_vehicle_fragment);

        Vehicle vehicle = (Vehicle)getIntent().getSerializableExtra("vehicle");
        Log.e("vehicle",String.valueOf(vehicle!=null) + "in activity");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.toHoldFragment, ViewVehicleFragment.newInstance(vehicle),"frag");
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
