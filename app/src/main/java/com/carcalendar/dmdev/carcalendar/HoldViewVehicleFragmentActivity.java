package com.carcalendar.dmdev.carcalendar;

import android.content.Intent;
import android.os.PersistableBundle;
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
        if (savedInstanceState == null) {
            Vehicle vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
            Log.e("vehicle", String.valueOf(vehicle != null) + "in activity");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ViewVehicleFragment fr = ViewVehicleFragment.newInstance(vehicle);
            fr.setRetainInstance(true);
            fragmentTransaction.add(R.id.toHoldFragment, fr, "frag");
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("fragment",false);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
