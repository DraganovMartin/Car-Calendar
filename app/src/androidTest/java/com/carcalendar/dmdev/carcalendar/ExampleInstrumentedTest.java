package com.carcalendar.dmdev.carcalendar;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.carcalendar.dmdev.carcalendar.utils.DatabaseHelper;
import com.carcalendar.dmdev.carcalendar.utils.DatabaseManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private DatabaseManager dbManager;
    private static final String[] DATA = {"FU8383CK", "dimcho", "Toyota", "Neshto si", "Car", "Sedan", "Diesel", "12000", null, "2003", "13000"};
    private static final String[] UPDATED_DATA = {"DP8383KL", "dimcho", "Rakia", "Slivova", "Car", "Sedan", "Diesel", "1000", null, "2017", "10000"};
    private static final String[] USER_DATA = {"dimcho", "haha", "22","1"};

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbManager = new DatabaseManager(appContext);
        dbManager.insert(DatabaseHelper.USERS_TABLE, DatabaseHelper.USER_TABLE_COLUMNS, USER_DATA);
        dbManager.insert(DatabaseHelper.VEHICLES_TABLE, DatabaseHelper.VEHICLES_TABLE_COLUMNS, DATA);
    }

    @Test
    public void shouldGetUserDimchoFromDB() {
        Cursor cur = dbManager.fetch(DatabaseHelper.USERS_TABLE, DatabaseHelper.USER_TABLE_COLUMNS, null, null, null);
        assertEquals(1, cur.getCount());
        assertEquals("dimcho", cur.getString(0));
        assertEquals("haha", cur.getString(1));
        assertEquals(22, cur.getInt(2));
        assertEquals(1, cur.getInt(3));
    }

    public void shouldQueryVehiclesTable() throws Exception {
        Cursor cur = dbManager.fetch(DatabaseHelper.VEHICLES_TABLE, DatabaseHelper.VEHICLES_TABLE_COLUMNS, null, null, null);
        assertEquals(1, cur.getCount());
    }

    @Test
    public void shouldQueryUsersTable() throws Exception {
        Cursor cur2 = dbManager.fetch("users", DatabaseHelper.USER_TABLE_COLUMNS, null, null, null);
        assertEquals(1, cur2.getCount());
    }

    @Test
    public void shouldQueryTaxesTable() throws Exception {
        Cursor cur2 = dbManager.fetch("taxes", DatabaseHelper.TAX_TABLE_COLUMNS, null, null, null);
        assertEquals(0, cur2.getCount());
    }

    @Test
    public void shouldQueryMaintenanceTable() throws Exception {
        Cursor cur2 = dbManager.fetch("maintenance", DatabaseHelper.MAINTENANCE_TABLE_COLUMNS, null, null, null);
        assertEquals(0, cur2.getCount());
    }


    @Test
    public void shouldInsertInToTableVehicles() throws Exception {
        // Fetch inserted data
        Cursor cur = dbManager.fetch(DatabaseHelper.VEHICLES_TABLE, DatabaseHelper.VEHICLES_TABLE_COLUMNS, null, null, null);
        assertEquals(1, cur.getCount());
        assertEquals(DATA[0], cur.getString(0));
        assertEquals("dimcho", cur.getString(1));
        assertEquals("Toyota", cur.getString(2));
        assertEquals("Neshto si", cur.getString(3));
        assertEquals("Car", cur.getString(4));
        assertEquals("Sedan", cur.getString(5));
        assertEquals("Diesel", cur.getString(6));
        assertEquals("12000", cur.getString(7));
        assertNull(cur.getString(8));
        assertEquals(2003, cur.getInt(9));
        assertEquals(13000, cur.getInt(10));
    }

    @Test
    public void shouldUpdateUsersTable() {
        dbManager.update(null,DatabaseHelper.VEHICLES_TABLE, DatabaseHelper.VEHICLES_TABLE_COLUMNS, UPDATED_DATA);

        // Fetch inserted data
        Cursor cur = dbManager.fetch(DatabaseHelper.VEHICLES_TABLE, DatabaseHelper.VEHICLES_TABLE_COLUMNS, null, null, null);
        assertEquals(1, cur.getCount());
        assertEquals(UPDATED_DATA[0], cur.getString(0));
        assertEquals(UPDATED_DATA[1], cur.getString(1));
        assertEquals(UPDATED_DATA[2], cur.getString(2));
        assertEquals(UPDATED_DATA[3], cur.getString(3));
        assertEquals(UPDATED_DATA[4], cur.getString(4));
        assertEquals(UPDATED_DATA[5], cur.getString(5));
        assertEquals(UPDATED_DATA[6], cur.getString(6));
        assertEquals(UPDATED_DATA[7], cur.getString(7));
        assertNull(cur.getString(8));
        assertEquals(Integer.valueOf(UPDATED_DATA[9]).intValue(), cur.getInt(9));
        assertEquals(Integer.valueOf(UPDATED_DATA[10]).intValue(), cur.getInt(10));
    }

    @After
    public void tearDown(){
        dbManager.delete(DatabaseHelper.VEHICLES_TABLE, "ownerID = 'dimcho'");
        dbManager.delete(DatabaseHelper.USERS_TABLE, "username = 'dimcho'");
        dbManager.close();
    }
}
