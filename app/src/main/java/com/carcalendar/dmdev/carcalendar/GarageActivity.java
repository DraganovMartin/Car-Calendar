package com.carcalendar.dmdev.carcalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.carcalendar.dmdev.carcalendar.recycle.VehicleAdapter;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import model.UserManager;
import model.Vehicle.Vehicle;
import model.authentication.RunningStatus;

public class GarageActivity extends AppCompatActivity {

    private RecyclerView vehicleList;
    private RecyclerView.LayoutManager vehicleListManager;
    private TextView usernameTV;
    private UserManager manager = UserManager.getInstance();
    private Menu menu = null;
    private RunningStatus runStatus = RunningStatus.getInstance();

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        usernameTV = (TextView) findViewById(R.id.Username);
        usernameTV.setText(manager.getLoggedUserName());
        
        vehicleList = (RecyclerView) findViewById(R.id.view_vehicle_list);
        vehicleList.setHasFixedSize(true);

        vehicleListManager = new LinearLayoutManager(this);
        vehicleList.setLayoutManager(vehicleListManager);

        vehicleList.setAdapter(new VehicleAdapter(createDemoList()));
    }

    private List<Vehicle> createDemoList(){
        List<Vehicle> vehicles = new ArrayList<>(2);
        vehicles.add(new Vehicle(1,2,3,"Test1") {
            @Override
            public int getId() {
                return 0;
            }
        });

        vehicles.add(new Vehicle(5,6,7,"Test2") {
            @Override
            public int getId() {
                return 0;
            }
        });
        return vehicles;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        runStatus.setStatus(false);
        saveDataUserManager(manager);
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }

    /**
     *  Serializing the UserManager object to internal storage  with openFileOutput()
     * @param x - UserManager
     */
    private void saveDataUserManager(final UserManager x){

        /*Thread save = new Thread(new Runnable() {
            @Override
            public void run() {*/

                try {
                    ObjectOutputStream out = new ObjectOutputStream(openFileOutput("UsermanagerDATA.txt", Context.MODE_PRIVATE));
                    out.writeObject(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        /*    }
        });
        save.start();*/

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
                    manager.userLogout();
                    saveDataUserManager(manager);
                    Intent intent = new Intent(this.getApplicationContext(),LoginActivity.class);
                    finish();
                    startActivity(intent);
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
