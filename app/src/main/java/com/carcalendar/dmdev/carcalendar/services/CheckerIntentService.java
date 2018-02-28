package com.carcalendar.dmdev.carcalendar.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.carcalendar.dmdev.carcalendar.ExpirationNotifier;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.UserManager;
import model.Vehicle.Vehicle;

import model.expiration.ExpirationManager;
import model.expiration.ExpirationManager.*;
import model.expiration.ExpirationMessage;
import model.expiration.ExpiringTypes;

/**
 * Created by DevM on 1/7/2018.
 */

public class CheckerIntentService extends IntentService {
    // Dummy interval
    // TODO Get intervals from app preferences
    private static final int NOTIFICATION_INTERVAL_DAYS = 3;

    private TreeMap<String, List<Vehicle>> allUsersWithVehicles;
    private final UserManager userManager;
    private final ExpirationManager expManager;
    private ExpirationNotifier expirationNotifier;

    public CheckerIntentService() {
        super("CheckerService");
        userManager = UserManager.getInstance();
        allUsersWithVehicles = new TreeMap<>();
        expManager = new ExpirationManager();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Fixes NullPointer when app is not running. Database is initialized.
        userManager.setDbContext(getApplicationContext());
        expirationNotifier = new ExpirationNotifier(getApplicationContext());
    }

    // allUsersWithVehicles contains username - all vehicles for that user ...
    // 1. Cast all vehicles to the appropriate type - DONE
    // 2. Check all vehicles for a expiring insurance, next tax payment and vignette expiring - DONE
    // 3. Send status bar notification for the vehicle that has expiring events - DONE
    // 4. If the user for a given vehicle isn't logged in, log out the previous and ask for password - DONE
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // If no vehicles are registered yet the service exits without entering the body of all the loops
        // Adds all the required data in a data class - ExpirationData.java for easy management and access
        allUsersWithVehicles = (TreeMap<String, List<Vehicle>>) userManager.getAllVehicles();
        for (Map.Entry<String, List<Vehicle>> entry : allUsersWithVehicles.entrySet()) {
            for (Vehicle v : entry.getValue()) {
                expManager.addExpirationData(v, entry.getKey(), NOTIFICATION_INTERVAL_DAYS);
            }
        }

        // Goes through the expiration data and shows a notification for every vehicle
        for(VehicleExpirationData expData : expManager.getVehicleExpirationData()) {
            Map<ExpiringTypes, Pair<Integer, Integer>> expStuff = expData.getExpiringStuff();
            ExpirationMessage.Builder expMsgBuilder = new ExpirationMessage.Builder();
            // Determines what is going to expire, builds an expiration message
            for (Map.Entry<ExpiringTypes, Pair<Integer, Integer>> currExpiration : expStuff.entrySet()) {
                String vehicleType = expData.getVehicle().getVehicleType();
                String vehicleRegNum = expData.getVehicle().getRegistrationPlate();
                String expirationName = currExpiration.getKey().getValue();

                int remainingDays = currExpiration.getValue().first;

                Log.d("Test", "In loop exp days: " + remainingDays);

                // Build messages for all expiring stuff: tax, insurance, etc.
                ExpirationMessage.Builder builder = expMsgBuilder.setVehicleType(vehicleType)
                        .setVehicleRegNum(vehicleRegNum)
                        .setExpiringTypeName(expirationName)
                        .setRemainingDaysToExpiration(remainingDays);

                // Set interval data if the expiring object is an Insurance
                if(currExpiration.getKey() == ExpiringTypes.INSURANCE) {
                    Integer currInsuranceInterval = currExpiration.getValue().second;
                    builder.setInsuranceIntervalNumber(currInsuranceInterval);
                }

                expManager.addExpirationMessage(builder.build());
            }

            // Spawns a different notification for every vehicle
            if (!expStuff.isEmpty()){
                expirationNotifier.spawnNotification(expData.getVehicle(), expData.getOwner(),
                        expManager.constructNotificationMessage());
            }
        }

        // FIXED: It works ... but only when app is open and database initialized, needs work for another day
        // WITH: userManager.setDbContext(getApplicationContext()), but the service still does not run when
        // the user force closes the app from settings
        // Log.d("service", allCars.toString());
        // Log.d("service", allMotors.toString());
    }
}
