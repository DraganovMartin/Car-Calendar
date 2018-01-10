package com.carcalendar.dmdev.carcalendar.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Motorcycle;
import model.Vehicle.Vehicle;

/**
 * Created by DevM on 1/7/2018.
 */

public class CheckerIntentService extends IntentService {
    private UserManager userManager;
    private TreeMap<String,List<Vehicle>> allVehicles;
    private ArrayList<Car> allCars;
    private ArrayList<Motorcycle> allMotors;

    public CheckerIntentService(){
        super("CheckerService");
        userManager = UserManager.getInstance();
        allVehicles = new TreeMap<>();
        allCars = new ArrayList<>();
        allMotors = new ArrayList<>();
    }

    private void getAllCarsAndMotors(List<Vehicle> vehicleList){
        for (int i = 0; i <vehicleList.size() ; i++) {
            if(vehicleList.get(i) instanceof  Car) allCars.add((Car)vehicleList.get(i));
            else allMotors.add((Motorcycle)vehicleList.get(i));
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // allVehicles contains username - all vehicles for that user ...
        // 1. Cast all vehicles to the appropriate type - DONE
        // 2. TODO Check all vehicles for a expiring insurance, next tax payment and vignette expiring
        // 3. TODO Send status bar notification for the vehicle that has expiring events
        // 4. TODO If the user for a given vehicle isn't logged in, log out the previous and ask for password
        allVehicles = (TreeMap<String, List<Vehicle>>) userManager.getAllVehicles();
        for (Map.Entry<String,List<Vehicle>> entry : allVehicles.entrySet()){
            getAllCarsAndMotors(entry.getValue());
        }
        Log.e("service",allCars.toString()); // It works ... but only when app is open and database initialized, needs work for another day
        Log.e("service",allMotors.toString());


    }
}
