package com.carcalendar.dmdev.carcalendar;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carcalendar.dmdev.carcalendar.recycle.VehicleAdapter;
import com.carcalendar.dmdev.carcalendar.recycle.VehicleViewHolder;

import model.UserManager;
import model.Vehicle.Vehicle;
import model.storage.StorageManager;

public class GarageActivity extends AppCompatActivity implements VehicleViewHolder.OnRecyclerViewItemLongPressListener {

    private UserManager manager = UserManager.getInstance();
    private RecyclerView vehicleList;
    private VehicleAdapter adapter;
    private RecyclerView.LayoutManager vehicleListManager;
    private TextView usernameTV;
    private FloatingActionButton btnAddVechicle;
    private Menu menu = null;
    private TextView noVehicles;
    private ListView fabMenu;
    private RelativeLayout garageContainer;
    private LinearLayout helloLayout;
    private VehicleAdapter vAdapter;
    private boolean doubleBackToExitPressedOnce;
    private boolean fabMenuShown = false;
    private Handler mHandler = new Handler();
    public static final String SAVE_USER_MANAGER = "USER_MANAGER_SAVE";
    public static final int VEHICLE_ADDED_SUCCESSFULLY = 0;
    public static final int VEHICLE_ADDED_UNSUCCESSFULLY = 1;

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

        garageContainer = (RelativeLayout) findViewById(R.id.activity_garage);
        helloLayout = (LinearLayout) findViewById(R.id.HelloLayout);

        usernameTV = (TextView) findViewById(R.id.Username);
        usernameTV.setText(manager.getLoggedUserName());
        noVehicles = (TextView) findViewById(R.id.NoVehicles);
        btnAddVechicle = (FloatingActionButton) findViewById(R.id.btn_add_vehicle);

        vehicleList = (RecyclerView) findViewById(R.id.view_vehicle_list);
        vehicleList.setHasFixedSize(true);
        vehicleListManager = new LinearLayoutManager(this);
        vehicleList.setLayoutManager(vehicleListManager);
        vAdapter = new VehicleAdapter(manager.getRegisteredUserVehicles(),this);
        vehicleList.setAdapter(vAdapter);


        // Insert the static items in FAB's list
        String[] vehicleTypes = getResources().getStringArray(R.array.VehicleTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.fab_list_item, vehicleTypes);

        fabMenu = (ListView) findViewById(R.id.list_vehicle_types);
        fabMenu.setAdapter(adapter);

        Log.e("marto",String.valueOf(manager.getRegisteredUserVehicles().size()));

        fabMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:         // CAR
                        Intent intent = new Intent(getApplicationContext(),AddVehicleCarActivity.class);
                        intent.putExtra("Car",R.mipmap.car_add_image);
                        startActivityForResult(intent,VEHICLE_ADDED_SUCCESSFULLY);
                        hideFabMenu();
                        undoBackgroundDefocus();
                        break;
                    case 1:         // MOTORCYCLE
                        Intent intentMotor = new Intent(getApplicationContext(),AddVehicleCarActivity.class);
                        intentMotor.putExtra("Motor",R.mipmap.motorcycle_black);
                        startActivityForResult(intentMotor,VEHICLE_ADDED_SUCCESSFULLY);
                        hideFabMenu();
                        undoBackgroundDefocus();
                        break;


                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(vAdapter.getItemCount() < 1){
            vehicleList.setVisibility(View.INVISIBLE);
            noVehicles.setVisibility(View.VISIBLE);
        }
        else{
            vehicleList.setVisibility(View.VISIBLE);
            noVehicles.setVisibility(View.INVISIBLE);
        }

    }

    /**
         * Toggles the FAB menu.
         * Changes the container background and changes the clickable property
         * of its children to false
         */
    public void toggleFabMenu(View view){
        if(!fabMenuShown) {
            doBackgroundDefocus();
            showFabMenu();
        }else{
            hideFabMenu();
            undoBackgroundDefocus();
        }
    }

    /**
    * Removes the Fab menu when the layout outside it is pressed
    *
    */
    public void restoreLayout(View view){
        if(fabMenuShown){
            hideFabMenu();
            undoBackgroundDefocus();
        }
    }

    private void doBackgroundDefocus(){
        garageContainer.setBackgroundColor(0xBFFFFFFF);
        vAdapter.setItemDefocus(true);
        vehicleList.setLayoutFrozen(true);
        helloLayout.setClickable(false);
    }

    private void undoBackgroundDefocus(){
        garageContainer.setBackgroundColor(0x00000000);
        vAdapter.setItemDefocus(false);
        vehicleList.setLayoutFrozen(false);
        helloLayout.setClickable(true);
    }

    private void showFabMenu(){
        fabMenu.setVisibility(View.VISIBLE);
        fabMenuShown = true;
    }

    private void hideFabMenu(){
        fabMenu.setVisibility(View.GONE);
        fabMenuShown = false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        saveDataUserManager(manager);

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {

        if(fabMenuShown){
            hideFabMenu();
            undoBackgroundDefocus();
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == VEHICLE_ADDED_SUCCESSFULLY){
            Toast.makeText(this,"Vehicle added successfully !!!",Toast.LENGTH_SHORT).show();
            vAdapter.updateVehicleList();
        }
        else{
            Toast.makeText(this,"Something went wrong !!!",Toast.LENGTH_SHORT).show();
        }
    }



    // Handles the event of a long press on a recycler view's item
    @Override
    public void onRecyclerViewItemLongPress(View v, final int pos) {
        PopupMenu popup = new PopupMenu(v.getContext(),v);
        // Handles the popup menu item's click
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.it_edit:
                        return true;
                    case R.id.it_diary:
                        return true;
                    case R.id.it_delete:
                        vAdapter.deleteItemFromList(pos);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.vehicle_options_menu,popup.getMenu());
        popup.show();
    }
}
