package com.carcalendar.dmdev.carcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.carcalendar.dmdev.carcalendar.recycle.VehicleAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Vehicle.Vehicle;

public class GarageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        RecyclerView vehicleList = (RecyclerView) findViewById(R.id.view_vehicle_list);
        vehicleList.setHasFixedSize(true);

        RecyclerView.LayoutManager vehicleListManager = new LinearLayoutManager(this);
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
}
