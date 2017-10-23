package com.carcalendar.dmdev.carcalendar.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Stickers.Insurance;
import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Motorcycle;
import model.Vehicle.Vehicle;
import model.taxes.Tax;

/**
 * Created by DevM on 10/19/2017.
 */

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;
    private final UserManager userManager;

    public DatabaseManager(Context context){
        this.ourcontext = context;
        dbHelper = new DatabaseHelper(ourcontext);
        database = dbHelper.getWritableDatabase();
        userManager = UserManager.getInstance();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * @param modelObj - Object of the model to be inserted : vehicle and vehicle subclasses, the data embedded
     */
    public void insert(Object modelObj) throws Exception {
        ContentValues contentValues = new ContentValues();
        Car tempCar = null;
        Motorcycle tempCycle = null;
        if (modelObj instanceof Vehicle) {
            Vehicle tempVehicle = (Vehicle)modelObj;
            // Vehicle data
            contentValues.put(DatabaseHelper.VEHICLES_REGISTRATION, tempVehicle.getRegistrationPlate());
            contentValues.put(DatabaseHelper.VEHICLES_OWNERID, UserManager.getInstance().getLoggedUserName());
            contentValues.put(DatabaseHelper.VEHICLES_BRAND,tempVehicle.getBrand());
            contentValues.put(DatabaseHelper.VEHICLES_MODEL,tempVehicle.getModel());
            contentValues.put(DatabaseHelper.VEHICLES_PROD_YEAR,tempVehicle.getProductionYear());
            contentValues.put(DatabaseHelper.VEHICLES_IMAGE_PATH,tempVehicle.getPathToImage());
            contentValues.put(DatabaseHelper.VEHICLES_NEXT_OIL,Integer.parseInt(tempVehicle.getNextOilChange()));
            // Insurance data
            if (tempVehicle.getInsurance() != null) {
                contentValues.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempVehicle.getRegistrationPlate());
                contentValues.put(DatabaseHelper.TAXES_TYPE, tempVehicle.getInsurance().getTypeForDB());
                contentValues.put(DatabaseHelper.TAXES_DATE_FROM, tempVehicle.getInsurance().getStartDate());
                contentValues.put(DatabaseHelper.TAXES_DATE_TO, tempVehicle.getInsurance().getEndDateString());
                contentValues.put(DatabaseHelper.TAXES_PRICE, tempVehicle.getInsurance().getPrice());
            }
            // Taxes data
            if (tempVehicle.getTax() != null){
                contentValues.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempVehicle.getRegistrationPlate());
                contentValues.put(DatabaseHelper.TAXES_TYPE, tempVehicle.getTax().getType());
                contentValues.put(DatabaseHelper.TAXES_DATE_FROM, tempVehicle.getTax().getEndDate());
                contentValues.put(DatabaseHelper.TAXES_DATE_TO, tempVehicle.getTax().getEndDate());
                contentValues.put(DatabaseHelper.TAXES_PRICE, tempVehicle.getTax().getAmount());
            }

        }
        if (modelObj instanceof Car){
            tempCar = (Car)modelObj;
            contentValues.put(DatabaseHelper.VEHICLES_ENGINE_TYPE,tempCar.getEngineType());
            contentValues.put(DatabaseHelper.VEHICLES_TYPE,"Car");
            contentValues.put(DatabaseHelper.VEHICLES_BODY_TYPE,tempCar.getCarType());
            contentValues.put(DatabaseHelper.VEHICLES_RANGE,tempCar.getKmRange());
            // Vignette addition
            contentValues.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempCar.getRegistrationPlate());
            switch(tempCar.getVignette().getType()){
                case "Annual":
                    contentValues.put(DatabaseHelper.TAXES_TYPE,"annual-vignette");
                    break;
                case "Month":
                    contentValues.put(DatabaseHelper.TAXES_TYPE,"month-vignette");
                    break;
                case "Week":
                    contentValues.put(DatabaseHelper.TAXES_TYPE,"week-vignette");
                    break;
            }
            contentValues.put(DatabaseHelper.TAXES_DATE_FROM, tempCar.getVignette().getStartDate());
            contentValues.put(DatabaseHelper.TAXES_DATE_TO, tempCar.getVignette().getEndDate());
            contentValues.put(DatabaseHelper.TAXES_PRICE, tempCar.getVignette().getPrice());
            database.insert(DatabaseHelper.VEHICLES_TABLE, null, contentValues);

        }
        else if (modelObj instanceof  Motorcycle){
            tempCycle = (Motorcycle) modelObj;
            contentValues.put(DatabaseHelper.VEHICLES_TYPE,"Motorcycle");
            contentValues.put(DatabaseHelper.VEHICLES_ENGINE_TYPE,tempCycle.getEngineType());
            contentValues.put(DatabaseHelper.VEHICLES_BODY_TYPE,tempCycle.getMotorcycleType());
            contentValues.put(DatabaseHelper.VEHICLES_RANGE,tempCycle.getKmRange());
            database.insert(DatabaseHelper.VEHICLES_TABLE, null, contentValues);
        }

    }


    public void insertUser(String username, String pass, int age){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERS_USERNAME,username);
        contentValues.put(DatabaseHelper.USERS_PASSWORD,pass);
        contentValues.put(DatabaseHelper.USERS_AGE,age);
        contentValues.put(DatabaseHelper.USERS_ISLOGGED,false);
        database.insert(DatabaseHelper.USERS_TABLE,null,contentValues);
    }

    public Cursor fetch(String table, String ... columns) {
        Cursor cursor = database.query(table, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    /**
     * Updates Vehicle objects in DB
     * @return rows affected or -1 if method not executed
     */
    public int update(Object modelObj) {
        ContentValues contentValues = new ContentValues();
        Car tempCar = null;
        Motorcycle tempCycle = null;
        if (modelObj instanceof Vehicle) {
            Vehicle tempVehicle = (Vehicle)modelObj;
            // Vehicle data
            contentValues.put(DatabaseHelper.VEHICLES_REGISTRATION, tempVehicle.getRegistrationPlate());
            contentValues.put(DatabaseHelper.VEHICLES_OWNERID, UserManager.getInstance().getLoggedUserName());
            contentValues.put(DatabaseHelper.VEHICLES_BRAND,tempVehicle.getBrand());
            contentValues.put(DatabaseHelper.VEHICLES_MODEL,tempVehicle.getModel());
            contentValues.put(DatabaseHelper.VEHICLES_PROD_YEAR,tempVehicle.getProductionYear());
            contentValues.put(DatabaseHelper.VEHICLES_IMAGE_PATH,tempVehicle.getPathToImage());
            contentValues.put(DatabaseHelper.VEHICLES_NEXT_OIL,Integer.parseInt(tempVehicle.getNextOilChange()));
            // Insurance data
            if (tempVehicle.getInsurance() != null) {
                contentValues.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempVehicle.getRegistrationPlate());
                contentValues.put(DatabaseHelper.TAXES_TYPE, tempVehicle.getInsurance().getTypeForDB());
                contentValues.put(DatabaseHelper.TAXES_DATE_FROM, tempVehicle.getInsurance().getStartDate());
                contentValues.put(DatabaseHelper.TAXES_DATE_TO, tempVehicle.getInsurance().getEndDateString());
                contentValues.put(DatabaseHelper.TAXES_PRICE, tempVehicle.getInsurance().getPrice());
            }
            // Taxes data
            if (tempVehicle.getTax() != null){
                contentValues.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempVehicle.getRegistrationPlate());
                contentValues.put(DatabaseHelper.TAXES_TYPE, tempVehicle.getTax().getType());
                contentValues.put(DatabaseHelper.TAXES_DATE_FROM, tempVehicle.getTax().getEndDate());
                contentValues.put(DatabaseHelper.TAXES_DATE_TO, tempVehicle.getTax().getEndDate());
                contentValues.put(DatabaseHelper.TAXES_PRICE, tempVehicle.getTax().getAmount());
            }

        }
        if (modelObj instanceof Car){
            tempCar = (Car)modelObj;
            contentValues.put(DatabaseHelper.VEHICLES_ENGINE_TYPE,tempCar.getEngineType());
            contentValues.put(DatabaseHelper.VEHICLES_TYPE,"Car");
            contentValues.put(DatabaseHelper.VEHICLES_BODY_TYPE,tempCar.getCarType());
            contentValues.put(DatabaseHelper.VEHICLES_RANGE,tempCar.getKmRange());
            // Vignette addition
            contentValues.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempCar.getRegistrationPlate());
            switch(tempCar.getVignette().getType()){
                case "Annual":
                    contentValues.put(DatabaseHelper.TAXES_TYPE,"annual-vignette");
                    break;
                case "Month":
                    contentValues.put(DatabaseHelper.TAXES_TYPE,"month-vignette");
                    break;
                case "Week":
                    contentValues.put(DatabaseHelper.TAXES_TYPE,"week-vignette");
                    break;
            }
            contentValues.put(DatabaseHelper.TAXES_DATE_FROM, tempCar.getVignette().getStartDate());
            contentValues.put(DatabaseHelper.TAXES_DATE_TO, tempCar.getVignette().getEndDate());
            contentValues.put(DatabaseHelper.TAXES_PRICE, tempCar.getVignette().getPrice());
            return database.update(DatabaseHelper.VEHICLES_TABLE,contentValues,null,null);

        }
        else if (modelObj instanceof  Motorcycle){
            tempCycle = (Motorcycle) modelObj;
            contentValues.put(DatabaseHelper.VEHICLES_TYPE,"Motorcycle");
            contentValues.put(DatabaseHelper.VEHICLES_ENGINE_TYPE,tempCycle.getEngineType());
            contentValues.put(DatabaseHelper.VEHICLES_BODY_TYPE,tempCycle.getMotorcycleType());
            contentValues.put(DatabaseHelper.VEHICLES_RANGE,tempCycle.getKmRange());
            return database.update(DatabaseHelper.VEHICLES_TABLE,contentValues,null,null);
        }
        return -1;
    }

    public boolean updateUser(String username,String pass){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERS_ISLOGGED,true);
        int rowsAffected=0;
        rowsAffected = database.update(DatabaseHelper.USERS_TABLE,contentValues,"username = ?",new String[]{username});
        if (rowsAffected > 0) return true;
        return false;
    }

    public void delete(String table,String whereClause) {
        database.delete(table, whereClause, null);
    }

    public String getLoggedUserFromDB() {
        Cursor cursor = fetch(DatabaseHelper.USERS_TABLE, DatabaseHelper.USER_TABLE_COLUMNS);
        return null;
    }

  }
