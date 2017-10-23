package com.carcalendar.dmdev.carcalendar.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.Stickers.AnnualVignette;
import model.Stickers.IVignette;
import model.Stickers.Insurance;
import model.Stickers.MonthVignette;
import model.Stickers.WeekVignette;
import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Motorcycle;
import model.Vehicle.Vehicle;
import model.taxes.Tax;
import model.taxes.VehicleTax;

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
     *
     *
     * @param table database table
     * @param columns am array of columns
     * @param values an array of column values
     */
    public void insert(String table,String[] columns, String[] values) throws Exception {
        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length) {
            switch (table) {
                case "vehicles":
                    for (int i = 0; i < columns.length; i++) {
                        if (i == 9 || i == 10) {
                            int tempVal = Integer.parseInt(values[i]);
                            contentValues.put(columns[i], tempVal);
                        } else contentValues.put(columns[i], values[i]);
                    }
                    break;
                case "users":
                    for (int i = 0; i < columns.length; i++) {
                        if (i == 2 || i == 3) {
                            int tempVal = Integer.parseInt(values[i]);
                            contentValues.put(columns[i], tempVal);
                        } else contentValues.put(columns[i], values[i]);
                    }
                    break;
                case "taxes":
                    for (int i = 0; i < columns.length; i++) {
                        if (i == columns.length - 1) {
                            double tempVal = Double.parseDouble(values[i]);
                            contentValues.put(columns[i], tempVal);
                        } else contentValues.put(columns[i], values[i]);
                    }
                    break;
                case "maintenance":
                    for (int i = 0; i < columns.length; i++) {
                        if (i == columns.length - 1) {
                            double tempVal = Double.parseDouble(values[i]);
                            contentValues.put(columns[i], tempVal);
                        } else contentValues.put(columns[i], values[i]);
                    }
                    break;
                default:
                    for (int i = 0; i < columns.length; i++) {
                        contentValues.put(columns[i], values[i]);
                    }
            }

            database.insert(table, null, contentValues);
        } else {
            throw new Exception("Column number is different than values Number");
        }
    }

    public Cursor fetch(String table,String[] columns, String whereClause, String[] whereValues, String sorter) {
        Cursor cursor = database.query(table, columns, whereClause,
                whereValues, null, null, sorter);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    /**
     *
     * @param whereClause - 'this = that'
     * @param table - Database table to update
     * @param columns - String array of columns to update
     * @param values - String array of values corresponding by index to columns to update
     * @return rows affected or -1 if method not executed
     */
    public int update(String whereClause,String table, String []columns, String [] values) {
        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length) {
            switch (table) {
                case "vehicles":
                    for (int i = 0; i < columns.length; i++) {
                        if (i == 9 || i == 10) {
                            int tempVal = Integer.parseInt(values[i]);
                            contentValues.put(columns[i], tempVal);
                        } else contentValues.put(columns[i], values[i]);
                    }
                    break;
                case "users":
                    for (int i = 0; i < columns.length; i++) {
                        if (i == 2 || i == 3) {
                            int tempVal = Integer.parseInt(values[i]);
                            contentValues.put(columns[i], tempVal);
                        } else contentValues.put(columns[i], values[i]);
                    }
                    break;
                case "taxes":
                    for (int i = 0; i < columns.length; i++) {
                        if (i == columns.length-1) {
                            double tempVal = Double.parseDouble(values[i]);
                            contentValues.put(columns[i], tempVal);
                        } else contentValues.put(columns[i], values[i]);
                    }
                    break;
                case "maintenance" :
                    for (int i = 0; i < columns.length; i++) {
                        if (i == columns.length-1) {
                            double tempVal = Double.parseDouble(values[i]);
                            contentValues.put(columns[i], tempVal);
                        } else contentValues.put(columns[i], values[i]);
                    }
                    break;
                default :
                    for (int i = 0; i <columns.length ; i++) {
                        contentValues.put(columns[i],values[i]);
                    }
            }
            int i = database.update(table, contentValues,
                    whereClause, null);
            return i;
        }
        return -1;
    }

    public void delete(String table,String whereClause) {
        database.delete(table, whereClause, null);
    }

    /**
     * Gets the currently logged user from the database.
     * The method can be used to check if a user is currently logged in
     *
     * @return the logged user's data, null if there is no logged user
     * @throws Exception if the 'isLogged' flag set for more than one user
     */
    public String[] getLoggedUserFromDB() throws Exception {
        Cursor cursor = database.query(DatabaseHelper.USERS_TABLE,
                DatabaseHelper.USER_TABLE_COLUMNS,
                "isLogged = ?",
                new String[]{"1"},
                null, null, null);

        if(!cursor.moveToFirst()) {
            return null;
        }

        if(cursor.getCount() > 1) {
            throw new Exception("Two users have the \'isLogged\' flag set to 1!");
        }

        String username = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_USERNAME));
        String password = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_PASSWORD));
        String age = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_AGE));

        cursor.close();

        return new String[]{username, password, age};
    }

    /**
     * Gets the user's vehicles from the database as an ArrayList.
     *
     * The method also gets and sets the taxes, vignettes and insurance of the vehicle
     * The method constructs the individual Vehicle objects (Car, Motorcycle) and stores them in an ArrayList.
     *
     * @param username the unique id that identifies which vehicles are owned by this user
     * @return a list of the user's vehicles, null if no vehicles are stored in the db
     */
    public List<Vehicle> getVehiclesForLoggedUser(String username) throws Exception {
        Cursor cursor = database.query(DatabaseHelper.VEHICLES_TABLE,
                DatabaseHelper.VEHICLES_TABLE_COLUMNS,
                "ownerID = ?",
                new String[]{username},
                null, null, null);

        if(cursor.getCount() == 0) {
            return null;
        }

        List<Vehicle> vehicles = new ArrayList<>();
        while(cursor.moveToNext()) {
            String registration = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_REGISTRATION));
            String brand = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_BRAND));
            String model = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_MODEL));
            String type = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_TYPE));
            String bodyType = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_BODY_TYPE));
            String engineType = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_ENGINE_TYPE));
            String range = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_RANGE));
            String imagePath = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_IMAGE_PATH));
            int productionYear = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_PROD_YEAR));
            int nextOilChange = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_NEXT_OIL));

            // Get the vehicle's tax and insurance
            VehicleTax vehicleTax = getTaxForVehicle(registration);
            Insurance vehicleInsurance = getInsuranceForVehicle(registration);

            switch(type){
                case "Car" :
                    Car car = new Car(registration, brand, model, bodyType,
                            engineType, range, imagePath, productionYear, nextOilChange);

                    // Sets the car's tax
                    car.setTax(vehicleTax);

                    // Sets the car's insurance
                    car.setInsurance(vehicleInsurance);

                    // Sets the car's vignette
                    car.setVignette(getVignetteForVehicle(registration));

                    vehicles.add(car);

                    break;

                case "Motorcycle" :
                    Motorcycle motor = new Motorcycle(registration, brand, model, bodyType,
                            engineType, range, imagePath, productionYear, nextOilChange);

                    // Sets the motorcycles's tax
                    motor.setTax(vehicleTax);

                    // Sets the motorcycles's insurance
                    motor.setInsurance(vehicleInsurance);

                    vehicles.add(motor);
                    break;

                default: throw new Exception("No such vehicle type! Please check the type in table vehicles.");
            }
        }

        cursor.close();

        return vehicles;
    }

    /**
     * Gets the vehicle's (specified by registration) tax
     *
     * @param registration the vehicle's registration
     * @return the vehicle tax
     */
    public VehicleTax getTaxForVehicle(String registration) throws Exception{
        Cursor cursor = database.query(DatabaseHelper.TAXES_TABLE,
                DatabaseHelper.TAX_TABLE_COLUMNS,
                "vehicle_registration = ? And type = 'tax'",
                new String[]{registration},
                null, null,
                null);

        if(!cursor.moveToFirst()) {
            throw new Exception("No tax for vehicle \'" + registration + "\' found!");
        }

        VehicleTax tax = new VehicleTax();
        double price = cursor.getDouble(
                cursor.getColumnIndexOrThrow(DatabaseHelper.TAXES_PRICE));
        String dateTo = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseHelper.TAXES_DATE_TO));

        // Get the tax endDate
        Calendar endDate = Calendar.getInstance();
        // Adding because of SQLite
        endDate.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateTo));
        Calendar check = Calendar.getInstance();
        if (endDate.after(check) || endDate.compareTo(check) == 0) {
            System.out.println(endDate.get(Calendar.DAY_OF_MONTH));
            tax.setAmount(price);
            tax.setEndDate(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        }

        cursor.close();

        return tax;
    }

    /**
     * Gets the current vignette data from the db for the specified vehicle
     * The query sorts the dates in descending order and returns the newest vignette
     *
     * @param registration the vehicle's registration number
     * @return the newest vignette from the db
     */
    public IVignette getVignetteForVehicle(String registration) throws Exception {
        Cursor cursor = database.query(DatabaseHelper.TAXES_TABLE,
                DatabaseHelper.TAX_TABLE_COLUMNS,
                "vehicle_registration = ? And type Like '%vignette'",
                new String[]{registration},
                null, null,
                DatabaseHelper.TAXES_DATE_TO + " DESC Limit 1");

        if(!cursor.moveToFirst()) {
           throw new Exception("No vignette for vehicle \'" + registration + "\' found!");
        }

        IVignette vignette;

        // Gets the vignette start date (endDate is calculated in vignette constructor)
        // and parses it
        String dateFrom = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseHelper.TAXES_DATE_FROM));
        Calendar parsedDate = Calendar.getInstance();
        parsedDate.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom));

        // Gets the vignette price and type
        double price = cursor.getDouble(
                cursor.getColumnIndexOrThrow(DatabaseHelper.TAXES_PRICE));
        String type = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseHelper.TAXES_TYPE));

        // Create the individual vignettes by model type
        switch (type) {
            case "annual-vignette":
                vignette = new AnnualVignette(parsedDate.get(Calendar.YEAR),
                        parsedDate.get(Calendar.MONTH),
                        parsedDate.get(Calendar.DAY_OF_MONTH),
                        price);
                break;

            case "month-vignette":
                vignette = new MonthVignette(parsedDate.get(Calendar.YEAR),
                        parsedDate.get(Calendar.MONTH),
                        parsedDate.get(Calendar.DAY_OF_MONTH),
                        price);
                break;

            case "week-vignette":
                vignette = new WeekVignette(parsedDate.get(Calendar.YEAR),
                        parsedDate.get(Calendar.MONTH),
                        parsedDate.get(Calendar.DAY_OF_MONTH),
                        price);
                break;

            default:
                throw new Exception("No such vignette type! Please check the type in table taxes.");
        }

        cursor.close();

        return vignette;
    }

    public Insurance getInsuranceForVehicle(String registration) throws Exception {
        Cursor cursor = database.query(DatabaseHelper.TAXES_TABLE,
                DatabaseHelper.TAX_TABLE_COLUMNS,
                "vehicle_registration = ? And type Like '%insurance'",
                new String[]{registration},
                null, null,
                DatabaseHelper.TAXES_DATE_FROM + " DESC Limit 1");

        if(!cursor.moveToFirst()) {
            throw new Exception("No insurance for vehicle \'" + registration + "\' found!");
        }

        Insurance insurance = new Insurance();
        String type = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseHelper.TAXES_TYPE));
        Insurance.Payments payments = null;
        switch (type) {
            case "1-insurance":
                payments = Insurance.Payments.ONE;
                break;
            case "2-insurance":
                payments = Insurance.Payments.TWO;
                break;
            case "3-insurance":
                payments = Insurance.Payments.THREE;
                break;
            case "4-insurance":
                payments = Insurance.Payments.FOUR;
                break;

            default:
                throw new Exception("No such insurance type! Please check the type in table taxes.");
        }

        insurance.setTypeCount(payments);

        double insurancePrice = cursor.getDouble(
                cursor.getColumnIndexOrThrow(DatabaseHelper.TAXES_PRICE));
        insurance.setPrice(insurancePrice);


        // Gets the vignette start date (endDate is calculated in vignette constructor)
        // and parses it
        String dateFrom = cursor.getString(
                cursor.getColumnIndexOrThrow(DatabaseHelper.TAXES_DATE_FROM));
        Calendar parsedDate = Calendar.getInstance();
        parsedDate.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom));

        insurance.setStartDate(parsedDate.get(Calendar.YEAR),
                parsedDate.get(Calendar.MONTH),
                parsedDate.get(Calendar.DAY_OF_MONTH));

        cursor.close();

        return insurance;
    }

  }
