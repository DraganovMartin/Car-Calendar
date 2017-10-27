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

import model.UserManager;
import model.Vehicle.Car;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InsertDBTest {
    private DatabaseManager dbManager;
    private static final Car car = new Car("FU2334CK", "Hello", "Oiii",
            "Pff", "gasoline", "1000",
            null, 2003, "12000");
    private UserManager manager;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbManager = new DatabaseManager(appContext);
        manager = mock(UserManager.getInstance().getClass());
    }

    @Test
    public void shouldInsertInToUsers() {
        long rowID = dbManager.insertUser("dimcho", "pass", 22);
        assertEquals("Error while inserting user!", 1, rowID);

        long passed = dbManager.delete(DatabaseHelper.USERS_TABLE, "username = 'dimcho'");
        assertEquals("No users to delete", 1, passed);
    }

//    @Test
    public void shouldInsertInToVehicles() throws Exception {
        when(manager.getLoggedUserName()).thenReturn("dimcho");
        //TODO implement
    }

    @After
    public void tearDown() {
        dbManager.close();
    }
}