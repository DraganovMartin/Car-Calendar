package com.carcalendar.dmdev.carcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import model.Vehicle.Car;
import model.Vehicle.Vehicle;

public class GarageActivity extends AppCompatActivity implements VehicleViewHolder.OnRecyclerViewItemLongPressListener,VehicleViewHolder.OnRecyclerViewItemClickListener {

    private UserManager manager = UserManager.getInstance();
    private RecyclerView vehicleList;
    private RecyclerView.LayoutManager vehicleListManager;
    private TextView usernameTV;
    private TextView noVehicles;
    private ListView fabMenu;
    private RelativeLayout garageContainer;
    private LinearLayout fabMenuContainer;
    private VehicleAdapter vAdapter;
    private boolean doubleBackToExitPressedOnce;
    private boolean fabMenuShown = false;
    private Handler mHandler = null;
    public static final int VEHICLE_ADDED_SUCCESSFULLY = 0;
    public static final int VEHICLE_ADD_CANCELED = 1;
    public static final int SOMETHING_WENT_WRONG = 2;

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

        fabMenuContainer = (LinearLayout) findViewById(R.id.fab_menu_container);
        garageContainer = (RelativeLayout) findViewById(R.id.garageContainer);

        usernameTV = (TextView) findViewById(R.id.Username);
        usernameTV.setText(manager.getLoggedUserName());
        noVehicles = (TextView) findViewById(R.id.NoVehicles);

        vehicleList = (RecyclerView) findViewById(R.id.view_vehicle_list);
        vehicleList.setHasFixedSize(true);
        vehicleListManager = new LinearLayoutManager(this);
        vehicleList.setLayoutManager(vehicleListManager);
        vAdapter = new VehicleAdapter(manager.getRegisteredUserVehicles(),this,this);
        vehicleList.setAdapter(vAdapter);

        mHandler = new Handler();

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
                        Intent intentMotor = new Intent(getApplicationContext(),AddVehicleMotorcycleActivity.class);
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
        fabMenuContainer.bringToFront();
        fabMenuContainer.setBackgroundColor(0xBFFFFFFF);
    }

    private void undoBackgroundDefocus(){
        garageContainer.bringToFront();
        fabMenuContainer.setBackgroundColor(0x00000000);
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
    protected void onRestart() {
        super.onRestart();
        mHandler = new Handler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("destroy","Called stop !!!");
        mHandler.removeCallbacks(mRunnable);
        mHandler = null;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.e("destroy","Called destroy !!!");
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
                    manager.userLogoutForDB();
                    manager.userLogout();
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
        switch(resultCode){
            case VEHICLE_ADDED_SUCCESSFULLY:
                Toast.makeText(this,"Vehicle added successfully !!!",Toast.LENGTH_SHORT).show();
                vAdapter.updateVehicleList();
                break;
            case VEHICLE_ADD_CANCELED:
                Toast.makeText(this,"Operation canceled!",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"Something went wrong !!!",Toast.LENGTH_SHORT).show();
        }
    }



    // Handles the event of a long press on a recycler view's item
    @Override
    public void onRecyclerViewItemLongPress(final View v, final int pos) {
        PopupMenu popup = new PopupMenu(v.getContext(),v);
        // Handles the popup menu item's click
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.it_edit:
                        Vehicle vehicle = manager.getRegisteredUserVehicles().get(pos);
                        Intent edit;
                        if(vehicle instanceof Car){
                           edit = new Intent(v.getContext(),AddVehicleCarActivity.class);
                        }else{
                            edit = new Intent(v.getContext(),AddVehicleMotorcycleActivity.class);
                        }
                        edit.putExtra("Car object",vehicle);
                        startActivityForResult(edit,VEHICLE_ADDED_SUCCESSFULLY);
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

    @Override
    public void onRecyclerViewClick(View view, int pos) {
        Vehicle vehicle = manager.getRegisteredUserVehicles().get(pos);
        Intent toFrag = new Intent(view.getContext(),HoldViewVehicleFragmentActivity.class);
        toFrag.putExtra("vehicle",vehicle);
        startActivity(toFrag);
    }
}
