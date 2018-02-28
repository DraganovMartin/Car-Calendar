package com.carcalendar.dmdev.carcalendar;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.carcalendar.dmdev.carcalendar.utils.DatabaseHelper;
import com.carcalendar.dmdev.carcalendar.utils.DatabaseManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import model.Stickers.AnnualVignette;
import model.Stickers.Insurance;
import model.UserManager;
import model.Vehicle.Car;
import model.taxes.VehicleTax;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InsertDBTest {
    private DatabaseManager dbManager;
    private Car car;

    private static final Insurance insurance = new Insurance(Insurance.Payments.FOUR, 123.2, Calendar.getInstance());
    private static final AnnualVignette vignette = new AnnualVignette(2017, 10, 27, 100000);
    private static final VehicleTax tax = new VehicleTax();

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbManager = new DatabaseManager(appContext);
        car = new Car("FU2334CK", "Hello", "Oiii",
                "Pff", "gasoline", "1000",
                null, 2003, "12000");
        UserManager.getInstance().setLoggedUserUsername("dimcho");
    }

    @Test
    public void shouldInsertInToUsers() {
        long rowID = dbManager.insertUser("dimcho", "pass", 22);
        assertEquals("Error while inserting user!", 1, rowID);

        long passed = dbManager.delete(DatabaseHelper.USERS_TABLE, "username = 'dimcho'");
        assertEquals("No users to delete", 1, passed);
    }

    @Test
    public void shouldInsertInToVehicles() throws Exception {
        long rowId = dbManager.insert(car, false);
        assertEquals(1, rowId);
    }

    @Test
    public void shouldInsertInsurance() throws Exception {
        car.setInsurance(insurance);
        long rowId = dbManager.insert(car, false);
        assertEquals(1, rowId);
    }

    @Test
    public void shouldInsertVignette() throws Exception {
        car.setVignette(vignette);
        long rowId = dbManager.insert(car, false);
        assertEquals(1, rowId);
    }

    @Test
    public void shouldInsertTax() throws Exception {
        tax.setAmount(1212);
        tax.setEndDate(2017, 10, 27);
        car.setTax(tax);

        long rowId = dbManager.insert(car, false);
        assertEquals(1, rowId);
    }

    @After
    public void tearDown() {
        car = null;
        dbManager.delete(DatabaseHelper.VEHICLES_TABLE,"ownerID = 'dimcho'");
        dbManager.delete(DatabaseHelper.TAXES_TABLE,"vehicle_registration = 'FU2334CK'");
        dbManager.close();
    }
}