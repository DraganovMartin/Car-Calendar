package com.carcalendar.dmdev.carcalendar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carcalendar.dmdev.carcalendar.recycle.VehicleAdapter;

import model.UserManager;
import model.storage.StorageManager;

public class GarageActivity extends AppCompatActivity {

    private RecyclerView vehicleList;
    private RecyclerView.LayoutManager vehicleListManager;
    private TextView usernameTV;
    private UserManager manager = UserManager.getInstance();
    private Menu menu = null;
    private TextView noVehicles;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    public static final String SAVE_USER_MANAGER = "USER_MANAGER_SAVE";

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
        noVehicles = (TextView) findViewById(R.id.NoVehicles);

        vehicleList = (RecyclerView) findViewById(R.id.view_vehicle_list);
        vehicleList.setHasFixedSize(true);
        vehicleListManager = new LinearLayoutManager(this);
        vehicleList.setLayoutManager(vehicleListManager);

        vehicleList.setAdapter(new VehicleAdapter(manager.getRegisteredUserVehicles()));
        if(vehicleListManager.getChildCount() < 1){
            vehicleList.setVisibility(View.INVISIBLE);
            noVehicles.setVisibility(View.VISIBLE);
        }
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
        saveDataUserManager(manager);
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }

    /**
     *  Serializing the UserManager object to internal storage  with openFileOutput()
     * @param x - UserManager
     */
    private void saveDataUserManager(final UserManager x){
        Intent intent = new Intent(this,StorageManager.class);
        intent.putExtra(SAVE_USER_MANAGER,x);
        startService(intent);
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
            case R.id.Help:
                Intent helpAct = new Intent(this,GarageHelp.class);
                startActivity(helpAct);
        }
        return super.onOptionsItemSelected(item);
    }
}
