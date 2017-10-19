package com.carcalendar.dmdev.carcalendar.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;

/**
 * Created by DevM on 10/18/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {
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

    // users columns
    public static final String USERS_USERNAME = "username";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_AGE = "userAge";
    public static final String USERS_ISLOGGED = "isLogged";

    // taxes columns
    public static final String TAXES_VEHICLE_REGISTRATION = "vehicle_registration";
    public static final String TAXES_TYPE = "type";
    public static final String TAXES_DATE_FROM = "dateFrom";
    public static final String TAXES_DATE_TO = "dateTo";
    public static final String TAXES_PRICE = "price";

    // maintenance columns
    public static final String MTENANCE_VEHICLE_REGISTRATION = "vehicle_registration";
    public static final String MTENANCE_REPAIR_TYPE = "repair_type";
    public static final String MTENANCE_DATE = "date";
    public static final String MTENANCE_PRICE = "price";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS `vehicles` (\n" +
                "\t`registration`\tTEXT NOT NULL UNIQUE,\n" +
                "\t`ownerID`\tTEXT,\n" +
                "\t`brand`\tTEXT,\n" +
                "\t`model`\tTEXT,\n" +
                "\t`type`\tTEXT,\n" +
                "\t`body_type`\tTEXT,\n" +
                "\t`engine_type`\tTEXT,\n" +
                "\t`rangeKm`\tTEXT,\n" +
                "\t`image_path`\tTEXT,\n" +
                "\t`productionYear`\tINTEGER,\n" +
                "\t`nextOilChange`\tINTEGER,\n" +
                "\tFOREIGN KEY(`ownerID`) REFERENCES `users`(`username`)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `users` (\n" +
                "\t`username`\tTEXT NOT NULL UNIQUE,\n" +
                "\t`password`\tTEXT DEFAULT 'mygarage',\n" +
                "\t`userAge`\tINTEGER,\n" +
                "\t`isLogged`\tINTEGER,\n" +
                "\tPRIMARY KEY(`username`)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `taxes` (\n" +
                "\t`vehicle_registration`\tTEXT NOT NULL,\n" +
                "\t`type`\tTEXT NOT NULL,\n" +
                "\t`dateFrom`\tTEXT NOT NULL,\n" +
                "\t`dateTo`\tTEXT NOT NULL,\n" +
                "\t`price`\tREAL DEFAULT 0.0,\n" +
                "\tPRIMARY KEY(`vehicle_registration`,`type`,`dateFrom`),\n" +
                "\tFOREIGN KEY(`vehicle_registration`) REFERENCES `vehicles`(`registration`)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `maintenance` (\n" +
                "\t`vehicle_registration`\tTEXT NOT NULL,\n" +
                "\t`repair_type`\tTEXT NOT NULL DEFAULT 'repair',\n" +
                "\t`date`\tTEXT NOT NULL,\n" +
                "\t`price`\tREAL DEFAULT 0.0,\n" +
                "\tFOREIGN KEY(`vehicle_registration`) REFERENCES `vehicles`(`registration`),\n" +
                "\tPRIMARY KEY(`vehicle_registration`,`repair_type`,`date`)\n" +
                ");\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
