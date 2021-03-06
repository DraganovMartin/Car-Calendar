package com.carcalendar.dmdev.carcalendar.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.Stickers.AnnualVignette;
import model.Stickers.IVignette;
import model.Stickers.Insurance;
import model.Stickers.MonthVignette;
import model.Stickers.WeekVignette;
import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Motorcycle;
import model.Vehicle.Vehicle;
import model.taxes.VehicleTax;

/**
 * Created by DevM on 10/19/2017.
 */

public class DatabaseManager {
    private final UserManager userManager;
    private DatabaseHelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        this.ourcontext = context;
        dbHelper = new DatabaseHelper(ourcontext);
        database = dbHelper.getWritableDatabase();
        userManager = UserManager.getInstance();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * @param modelObj     - Object of the model to be inserted : vehicle and vehicle subclasses, the data embedded
     * @param shouldUpdate - initiates database updated if flag is true, else inserts to database
     * @return long the row ID of the newly inserted row, -3 if whole method is not executed or throws exception
     * in case of insurance,tax or vignette failure.
     */
    public long insert(Object modelObj, boolean shouldUpdate) throws Exception {
        ContentValues contentValues = new ContentValues();
        ContentValues insuranceContent = new ContentValues();
        ContentValues taxContent = new ContentValues();
        ContentValues vignetteContent = new ContentValues();
        Car tempCar = null;
        Motorcycle tempCycle = null;
        if (modelObj instanceof Vehicle) {
            Vehicle tempVehicle = (Vehicle) modelObj;
            // Vehicle data
            contentValues.put(DatabaseHelper.VEHICLES_REGISTRATION, tempVehicle.getRegistrationPlate());
            contentValues.put(DatabaseHelper.VEHICLES_OWNERID, UserManager.getInstance().getLoggedUserName());
            contentValues.put(DatabaseHelper.VEHICLES_BRAND, tempVehicle.getBrand());
            contentValues.put(DatabaseHelper.VEHICLES_MODEL, tempVehicle.getModel());
            contentValues.put(DatabaseHelper.VEHICLES_PROD_YEAR, tempVehicle.getProductionYear());
            contentValues.put(DatabaseHelper.VEHICLES_IMAGE_PATH, tempVehicle.getPathToImage());
            contentValues.put(DatabaseHelper.VEHICLES_NEXT_OIL, Integer.parseInt(tempVehicle.getNextOilChange()));
            // Insurance data
            if (tempVehicle.getInsurance() != null) {
                insuranceContent.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempVehicle.getRegistrationPlate());
                insuranceContent.put(DatabaseHelper.TAXES_TYPE, tempVehicle.getInsurance().getTypeForDB());
                insuranceContent.put(DatabaseHelper.TAXES_DATE_FROM, tempVehicle.getInsurance().getStartDate());
                insuranceContent.put(DatabaseHelper.TAXES_DATE_TO, tempVehicle.getInsurance().getEndDateString());
                insuranceContent.put(DatabaseHelper.TAXES_PRICE, tempVehicle.getInsurance().getPrice());
                if (shouldUpdate) {
                    if (database.update(DatabaseHelper.TAXES_TABLE,
                            insuranceContent,
                            "vehicle_registration = ? AND type Like '%insurance'",
                            new String[]{tempVehicle.getRegistrationPlate()}) == 0) {
                        throw new Exception("Updating insurance data fucked up !!");
                    }

                } else {
                    database.insertOrThrow(DatabaseHelper.TAXES_TABLE, null, insuranceContent);
                }
            }
            // Taxes data
            if (tempVehicle.getTax() != null) {
                taxContent.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempVehicle.getRegistrationPlate());
                taxContent.put(DatabaseHelper.TAXES_TYPE, tempVehicle.getTax().getType());
                taxContent.put(DatabaseHelper.TAXES_DATE_FROM, tempVehicle.getTax().getEndDate());
                taxContent.put(DatabaseHelper.TAXES_DATE_TO, tempVehicle.getTax().getEndDate());
                taxContent.put(DatabaseHelper.TAXES_PRICE, tempVehicle.getTax().getAmount());
                if (shouldUpdate) {

                    if (database.update(DatabaseHelper.TAXES_TABLE, taxContent,
                            "vehicle_registration = ? AND type = 'tax'",
                            new String[]{tempVehicle.getRegistrationPlateCache()}) == 0) {
                        throw new Exception("Updating tax data fucked up !!");
                    }
                } else {
                    database.insertOrThrow(DatabaseHelper.TAXES_TABLE, null, taxContent);
                }
            }

        }
        if (modelObj instanceof Car) {
            tempCar = (Car) modelObj;
            contentValues.put(DatabaseHelper.VEHICLES_ENGINE_TYPE, tempCar.getEngineType());
            contentValues.put(DatabaseHelper.VEHICLES_TYPE, "Car");
            contentValues.put(DatabaseHelper.VEHICLES_BODY_TYPE, tempCar.getCarType());
            contentValues.put(DatabaseHelper.VEHICLES_RANGE, tempCar.getKmRange());
            // Vignette addition
            if (tempCar.getVignette() != null) {
                vignetteContent.put(DatabaseHelper.TAXES_VEHICLE_REGISTRATION, tempCar.getRegistrationPlate());
                vignetteContent.put(DatabaseHelper.TAXES_TYPE, tempCar.getVignette().getType());
                vignetteContent.put(DatabaseHelper.TAXES_DATE_FROM, tempCar.getVignette().getStartDate());
                vignetteContent.put(DatabaseHelper.TAXES_DATE_TO, tempCar.getVignette().getEndDate());
                vignetteContent.put(DatabaseHelper.TAXES_PRICE, tempCar.getVignette().getPrice());
                if (shouldUpdate) {
                    if (database.update(DatabaseHelper.TAXES_TABLE, vignetteContent,
                            "vehicle_registration = ? AND type Like '%vignette'",
                            new String[]{tempCar.getRegistrationPlateCache()}) == 0) {
                        throw new Exception("Updating vignette data fucked up !!");
                    }
                } else {
                    database.insertOrThrow(DatabaseHelper.TAXES_TABLE, null, vignetteContent);
                }
            }
            if (shouldUpdate) {
                if (database.update(DatabaseHelper.VEHICLES_TABLE, contentValues,
                        "registration = ?",
                        new String[]{tempCar.getRegistrationPlateCache()}) == 0) {
                    throw new Exception("Updating vehicle data fucked up !!");
                }

                return 1;
            } else
                return database.insertOrThrow(DatabaseHelper.VEHICLES_TABLE, null, contentValues);

        } else if (modelObj instanceof Motorcycle) {
            tempCycle = (Motorcycle) modelObj;
            contentValues.put(DatabaseHelper.VEHICLES_TYPE, "Motorcycle");
            contentValues.put(DatabaseHelper.VEHICLES_ENGINE_TYPE, tempCycle.getEngineType());
            contentValues.put(DatabaseHelper.VEHICLES_BODY_TYPE, tempCycle.getMotorcycleType());
            contentValues.put(DatabaseHelper.VEHICLES_RANGE, tempCycle.getKmRange());

            if (shouldUpdate) {
                if (database.update(DatabaseHelper.VEHICLES_TABLE, contentValues, "registration = ?",
                        new String[]{tempCycle.getRegistrationPlateCache()}) == 0) {
                    throw new Exception("Updating vehicle data fucked up !!");
                }

                return 1;
            } else
                return database.insertOrThrow(DatabaseHelper.VEHICLES_TABLE, null, contentValues);
        }

        return -3;
    }


    public long insertUser(String username, String pass, int age) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERS_USERNAME, username);
        contentValues.put(DatabaseHelper.USERS_PASSWORD, pass);
        contentValues.put(DatabaseHelper.USERS_AGE, age);
        contentValues.put(DatabaseHelper.USERS_ISLOGGED, false);
        return database.insert(DatabaseHelper.USERS_TABLE, null, contentValues);
    }

    public Cursor fetch(String table, String[] columns, String whereClause, String[] whereValues, String sorter) {
        Cursor cursor = database.query(table, columns, whereClause,
                whereValues, null, null, sorter);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * This method can also be used for logging out user, just set status to false
     *
     * @param username
     * @param pass
     * @param age
     * @param shouldUpdateStatus
     * @param status
     * @return boolean - true if everything is okay, false otherwise
     */
    public boolean updateUser(String username, String pass, int age, boolean shouldUpdateStatus, boolean status) {
        ContentValues contentValues = new ContentValues();
        if (shouldUpdateStatus) {
            contentValues.put(DatabaseHelper.USERS_ISLOGGED, status);
        }
        contentValues.put(DatabaseHelper.USERS_USERNAME, username);
        contentValues.put(DatabaseHelper.USERS_PASSWORD, pass);
        contentValues.put(DatabaseHelper.USERS_AGE, age);
        int rowsAffected = 0;
        rowsAffected = database.update(DatabaseHelper.USERS_TABLE, contentValues, "username = ?", new String[]{username});
        if (rowsAffected > 0) return true;
        return false;
    }

    public int delete(String table, String whereClause) {
        return database.delete(table, whereClause, null);
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

        if (!cursor.moveToFirst()) {
            return null;
        }

        if (cursor.getCount() > 1) {
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

    public void getAndStoreAllUsers() {
        Cursor cursor = database.query(DatabaseHelper.USERS_TABLE,
                DatabaseHelper.USER_TABLE_COLUMNS,
                null, null, null, null, null);

        String username;
        String password;
        int age;
        while (cursor.moveToNext()) {
            username = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_USERNAME));
            password = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_PASSWORD));
            age = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.USERS_AGE));

            userManager.addToRegisteredUsers(username, password, age);
        }

        cursor.close();
    }

    /**
     * Checks if such user exists in the db.
     *
     * @param username
     * @param password
     * @return the user's age or 0 if there is no such user
     */
    public int checkUserLogin(String username, String password) {
        Cursor cursor = database.query(DatabaseHelper.USERS_TABLE,
                DatabaseHelper.USER_TABLE_COLUMNS,
                "username = ? AND password = ?", new String[]{username, password},
                null, null, null);

        if (!cursor.moveToFirst()) {
            return 0;
        }

        int age = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USERS_AGE));
        cursor.close();

        return age;
    }

    /**
     * Gets the user's vehicles from the database as an ArrayList.
     * <p>
     * The method also gets and sets the taxes, vignettes and insurance of the vehicle
     * The method constructs the individual Vehicle objects (Car, Motorcycle) and stores them in an ArrayList.
     *
     * @param username the unique id that identifies which vehicles are owned by this user
     * @return a list of the user's vehicles, null if no vehicles are stored in the db
     */
    public List<Vehicle> getVehiclesForUser(String username) throws Exception {
        Cursor cursor = database.query(DatabaseHelper.VEHICLES_TABLE,
                DatabaseHelper.VEHICLES_TABLE_COLUMNS,
                "ownerID = ?",
                new String[]{username},
                null, null, null);

        if (cursor.getCount() == 0) {
            return null;
        }

        List<Vehicle> vehicles = new ArrayList<>();
        while (cursor.moveToNext()) {
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
            String nextOilChange = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.VEHICLES_NEXT_OIL));

            // Get the vehicle's tax and insurance
            VehicleTax vehicleTax = getTaxForVehicle(registration);
            Insurance vehicleInsurance = getInsuranceForVehicle(registration);

            switch (type) {
                case "Car":
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

                case "Motorcycle":
                    Motorcycle motor = new Motorcycle(registration, brand, model, bodyType,
                            engineType, range, imagePath, productionYear, nextOilChange);

                    // Sets the motorcycles's tax
                    motor.setTax(vehicleTax);

                    // Sets the motorcycles's insurance
                    motor.setInsurance(vehicleInsurance);

                    vehicles.add(motor);
                    break;

                default:
                    throw new Exception("No such vehicle type! Please check the type in table vehicles.");
            }
        }

        cursor.close();

        return vehicles;
    }

    /**
     * Gets vehicles from all users with taxes, vignettes and insurances
     * @return TreeMap with user username as key and arraylist of vehicles as value or an empty map
     */
    public Map<String,List<Vehicle>> getVehiclesForAllUsers(){
        TreeMap<String,List<Vehicle>> vehicleList = new TreeMap<>();
        getAndStoreAllUsers();
        ArrayList<String> usersUsernames;
        usersUsernames =  userManager.getAllRegisteredUsersUsername();
        for (String x : usersUsernames){
            try {
                List<Vehicle> vehiclesForCurrUser = getVehiclesForUser(x);
                if(vehiclesForCurrUser != null)
                    vehicleList.put(x, vehiclesForCurrUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vehicleList;
    }
    /**
     * Gets the vehicle's (specified by registration) tax
     *
     * @param registration the vehicle's registration
     * @return the vehicle tax
     */
    public VehicleTax getTaxForVehicle(String registration) throws Exception {
        Cursor cursor = database.query(DatabaseHelper.TAXES_TABLE,
                DatabaseHelper.TAX_TABLE_COLUMNS,
                "vehicle_registration = ? And type = 'tax'",
                new String[]{registration},
                null, null,
                null);

        if (!cursor.moveToFirst()) {
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
        tax.setAmount(price);
        tax.setEndDate(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

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

        if (!cursor.moveToFirst()) {
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
                        parsedDate.get(Calendar.DAY_OF_MONTH));
                vignette.setPrice(price);
                break;

            case "month-vignette":
                vignette = new MonthVignette(parsedDate.get(Calendar.YEAR),
                        parsedDate.get(Calendar.MONTH),
                        parsedDate.get(Calendar.DAY_OF_MONTH));
                vignette.setPrice(price);
                break;

            case "week-vignette":
                vignette = new WeekVignette(parsedDate.get(Calendar.YEAR),
                        parsedDate.get(Calendar.MONTH),
                        parsedDate.get(Calendar.DAY_OF_MONTH));
                vignette.setPrice(price);
                break;

            default:
                throw new Exception("No such vignette type! Please check the type in table taxes.");
        }

        cursor.close();

        return vignette;
    }

    /**
     * Gets the insurance data for the specified by 'registration' vehicle from db.
     * <p>
     * Note that the returned insurance may have expired.
     * The query sorts the dates (dateFRom) in descending order and returns the newest insurance
     *
     * @param registration the vehicles's registration
     * @return an Insurance object
     */
    public Insurance getInsuranceForVehicle(String registration) throws Exception {
        Cursor cursor = database.query(DatabaseHelper.TAXES_TABLE,
                DatabaseHelper.TAX_TABLE_COLUMNS,
                "vehicle_registration = ? And type Like '%insurance'",
                new String[]{registration},
                null, null,
                DatabaseHelper.TAXES_DATE_FROM + " DESC Limit 1");

        if (!cursor.moveToFirst()) {
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