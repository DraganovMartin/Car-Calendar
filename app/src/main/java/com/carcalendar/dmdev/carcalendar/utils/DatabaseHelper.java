package com.carcalendar.dmdev.carcalendar.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DevM on 10/18/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "carendar.db";
    // Table names
    public static final String VEHICLES_TABLE = "vehicles";
    public static final String USERS_TABLE = "users";
    public static final String TAXES_TABLE = "taxes";
    public static final String MAINTENANCE_TABLE = "maintenance";
    // Table columns

    //vehicles columns
    public static final String VEHICLES_REGISTRATION = "registration";
    public static final String VEHICLES_OWNERID = "ownerID";
    public static final String VEHICLES_BRAND = "brand";
    public static final String VEHICLES_MODEL = "model";
    public static final String VEHICLES_TYPE = "type";
    public static final String VEHICLES_BODY_TYPE = "body_type";
    public static final String VEHICLES_ENGINE_TYPE = "engine_type";
    public static final String VEHICLES_RANGE = "rangeKm";
    public static final String VEHICLES_IMAGE_PATH = "image_path";
    public static final String VEHICLES_PROD_YEAR = "productionYear";
    public static final String VEHICLES_NEXT_OIL = "nextOilChange";
    public static final String[] VEHICLES_TABLE_COLUMNS = new String[]{VEHICLES_REGISTRATION, VEHICLES_OWNERID, VEHICLES_BRAND, VEHICLES_MODEL, VEHICLES_TYPE, VEHICLES_BODY_TYPE, VEHICLES_ENGINE_TYPE, VEHICLES_RANGE, VEHICLES_IMAGE_PATH,VEHICLES_PROD_YEAR,VEHICLES_NEXT_OIL};

    // users columns
    public static final String USERS_USERNAME = "username";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_AGE = "userAge";
    public static final String USERS_ISLOGGED = "isLogged";
    public static final String[] USER_TABLE_COLUMNS = new String[] {USERS_USERNAME, USERS_PASSWORD, USERS_AGE, USERS_ISLOGGED};

    // taxes columns
    public static final String TAXES_VEHICLE_REGISTRATION = "vehicle_registration";
    public static final String TAXES_TYPE = "type";
    public static final String TAXES_DATE_FROM = "dateFrom";
    public static final String TAXES_DATE_TO = "dateTo";
    public static final String TAXES_PRICE = "price";
    public static final String[] TAX_TABLE_COLUMNS = {TAXES_VEHICLE_REGISTRATION,TAXES_TYPE, TAXES_DATE_FROM, TAXES_DATE_TO, TAXES_PRICE};

    // maintenance columns
    public static final String MTENANCE_VEHICLE_REGISTRATION = "vehicle_registration";
    public static final String MTENANCE_REPAIR_TYPE = "repair_type";
    public static final String MTENANCE_DATE = "date";
    public static final String MTENANCE_PRICE = "price";
    public static final String[] MAINTENANCE_TABLE_COLUMNS = {MTENANCE_VEHICLE_REGISTRATION, MTENANCE_REPAIR_TYPE, MTENANCE_DATE, MTENANCE_PRICE};

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS 'users' (\n" +
                        "\tusername\tTEXT NOT NULL UNIQUE,\n" +
                        "\tpassword\tTEXT DEFAULT 'mygarage',\n" +
                        "\tuserAge\tINTEGER,\n" +
                        "\tisLogged\tINTEGER,\n" +
                        "\tPRIMARY KEY(username)\n" +
                        ");");

        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS 'vehicles' (\n" +
                "\tregistration\tTEXT NOT NULL UNIQUE,\n" +
                "\townerID\tTEXT,\n" +
                "\tbrand\tTEXT,\n" +
                "\tmodel\tTEXT,\n" +
                "\ttype\tTEXT,\n" +
                "\tbody_type\tTEXT,\n" +
                "\tengine_type\tTEXT,\n" +
                "\trangeKm\tTEXT,\n" +
                "\timage_path\tTEXT,\n" +
                "\tproductionYear\tINTEGER,\n" +
                "\tnextOilChange\tINTEGER,\n" +
                "\tFOREIGN KEY(ownerID) REFERENCES 'users'('username')\n" +
                ");");

        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS 'taxes' (\n" +
                "\tvehicle_registration\tTEXT NOT NULL,\n" +
                "\ttype\tTEXT NOT NULL,\n" +
                "\tdateFrom\tTEXT NOT NULL,\n" +
                "\tdateTo\tTEXT NOT NULL,\n" +
                "\tprice\tREAL DEFAULT 0.0,\n" +
                "\tPRIMARY KEY(vehicle_registration,'type','dateFrom'),\n" +
                "\tFOREIGN KEY(vehicle_registration) REFERENCES 'vehicles'('registration')\n" +
                ");");

        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS 'maintenance' (\n" +
                "\tvehicle_registration\tTEXT NOT NULL,\n" +
                "\trepair_type\tTEXT NOT NULL DEFAULT 'repair',\n" +
                "\tdate\tTEXT NOT NULL,\n" +
                "\tprice\tREAL DEFAULT 0.0,\n" +
                "\tFOREIGN KEY(vehicle_registration) REFERENCES 'vehicles'('registration'),\n" +
                "\tPRIMARY KEY(vehicle_registration,'repair_type','date')\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
