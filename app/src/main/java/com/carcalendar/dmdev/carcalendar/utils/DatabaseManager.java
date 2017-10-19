package com.carcalendar.dmdev.carcalendar.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by DevM on 10/19/2017.
 */

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public DatabaseManager(Context context){
        this.ourcontext = context;
        dbHelper = new DatabaseHelper(ourcontext);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String table,String column, String value) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(column, value);
        contentValue.put(column, value);
        database.insert(table, null, contentValue);
    }

    public Cursor fetch(String table,String ... columns) {
        Cursor cursor = database.query(table, columns, null,
                null, null, null, null);
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

  }
