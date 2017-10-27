package com.carcalendar.dmdev.carcalendar;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.carcalendar.dmdev.carcalendar.utils.DatabaseHelper;
import com.carcalendar.dmdev.carcalendar.utils.DatabaseManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import model.Vehicle.Car;

/**
 * Created by dimcho on 27.10.17.
 */

@RunWith(AndroidJUnit4.class)
public class FetchFromDBTest {
    private static DatabaseManager dbManager;
    private static final Car car = new Car("FU2334CK", "Hello", "Oiii",
            "Pff", "gasoline", "1000",
            null, 2003, "12000");

    @BeforeClass
    public static void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbManager = new DatabaseManager(appContext);

        // Insert user in db
        dbManager.insertUser("dimcho", "pass", 22);
        dbManager.updateUser("dimcho", "pass", 22, true, true);
    }

    @Test
    public void shouldFetchUserFromDB() throws Exception {
        String[] userData  = dbManager.getLoggedUserFromDB();
        assertEquals(3, userData.length);
        assertEquals(userData[0], "dimcho");
        assertEquals(userData[1], "pass");
        assertEquals(userData[2], "1");
    }

    @AfterClass
    public static void tearDown() {
        dbManager.delete(DatabaseHelper.USERS_TABLE, "username = 'dimcho'");
        dbManager.close();
    }
}
