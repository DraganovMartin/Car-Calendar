package com.carcalendar.dmdev.carcalendar;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.carcalendar.dmdev.carcalendar.utils.DatabaseHelper;
import com.carcalendar.dmdev.carcalendar.utils.DatabaseManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import model.Stickers.AnnualVignette;
import model.Stickers.IVignette;
import model.Stickers.Insurance;
import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Vehicle;
import model.taxes.Tax;
import model.taxes.VehicleTax;

import static org.junit.Assert.assertEquals;

/**
 * Created by dimcho on 27.10.17.
 */

@RunWith(AndroidJUnit4.class)
public class FetchFromDBTest {
    private static DatabaseManager dbManager;
    private static final Car car = new Car("FU2334CK", "Hello", "Oiii",
            "Pff", "gasoline", "1000",
            null, 2003, "12000");

    private static final Insurance insurance = new Insurance(Insurance.Payments.FOUR, 123.2, Calendar.getInstance());
    private static final AnnualVignette vignette = new AnnualVignette(2017, 10, 27, 100000);
    private static final VehicleTax tax = new VehicleTax();
    private static final String[][] USER_DATA = {{"test", "pass", "22"}, {"test1", "password", "22"}};

    @BeforeClass
    public static void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbManager = new DatabaseManager(appContext);

        // Insert user in db
        dbManager.insertUser("test", "pass", 22);
        dbManager.insertUser("test1", "password", 22);
        dbManager.updateUser("test", "pass", 22, true, true);
        UserManager.getInstance().setLoggedUserUsername("test");


        car.setTax(tax);
        car.setVignette(vignette);
        car.setInsurance(insurance);
        dbManager.insert(car, false);
    }

    @Test
    public void shouldFetchUserFromDB() throws Exception {
        String[] userData  = dbManager.getLoggedUserFromDB();
        assertEquals(3, userData.length);
        assertEquals("test", userData[0]);
        assertEquals("pass", userData[1]);
        assertEquals("22", userData[2]);
    }

    @Test
    public void shouldFetchUserVehicleData() throws Exception {
        List<Vehicle> vehicles = dbManager.getVehiclesForUser("test");
        assertEquals(1, vehicles.size());

        for (Vehicle vehicle : vehicles) {
            assertEquals(car.getRegistrationPlate(), vehicle.getRegistrationPlate());
            assertEquals(car.getBrand(), vehicle.getBrand());
            assertEquals(car.getModel(), vehicle.getModel());
            assertEquals(car.getProductionYear(), vehicle.getProductionYear());
            assertEquals(car.getPathToImage(), vehicle.getPathToImage());
            assertEquals(car.getPathToImage(),vehicle.getPathToImage());
            assertEquals(car.getNextOilChange(), vehicle.getNextOilChange());

            // Insurance
            Insurance tempInsurance = vehicle.getInsurance();
            assertEquals(insurance.getStartDate(), tempInsurance.getStartDate());
            assertEquals(insurance.getTotalEndDate(), tempInsurance.getTotalEndDate());
            assertEquals(insurance.getTypeForDB(), tempInsurance.getTypeForDB());
            assertEquals(String.valueOf(insurance.getPrice()), String.valueOf(tempInsurance.getPrice()));

            // Tax
            Tax tmpTax = vehicle.getTax();
            assertEquals(tax.getEndDate(), tmpTax.getEndDate());
            assertEquals(tax.getType(), tmpTax.getType());
            assertEquals(String.valueOf(tax.getAmount()), String.valueOf(tmpTax.getAmount()));

            if(vehicle instanceof Car) {
                Car tmpCar = (Car) vehicle;
                assertEquals(car.getKmRange(), tmpCar.getKmRange());
                assertEquals(car.getEngineType(), tmpCar.getEngineType());
                assertEquals(car.getCarType(), tmpCar.getCarType());

                IVignette tmpVignette = tmpCar.getVignette();
                assertEquals(vignette.getStartDate(), tmpVignette.getStartDate());
                assertEquals(vignette.getEndDate(), tmpVignette.getEndDate());
                assertEquals( vignette.getType(), tmpVignette.getType());
                assertEquals(String.valueOf(vignette.getPrice()), String.valueOf(tmpVignette.getPrice()));
            }

        }
    }

    @Test
    public void shouldFetchAllUsersAndInitializeUserManager() {
        dbManager.getAndStoreAllUsers();
        UserManager manager = UserManager.getInstance();
        String[] registeredUsers = manager.getAllRegisteredUsers();

        int i = 0;
        for (String user :  registeredUsers) {
            String[] userData =  user.split(" ");

            assertEquals(3, userData.length);
            assertEquals(USER_DATA[i][0], userData[0]);
            assertEquals(USER_DATA[i][1], userData[1]);
            assertEquals(USER_DATA[i][2], userData[2]);

            i++;
       }

    }

    @AfterClass
    public static void tearDown() {
        dbManager.delete(DatabaseHelper.USERS_TABLE, "username = 'test'");
        dbManager.delete(DatabaseHelper.USERS_TABLE, "username = 'test1'");
        dbManager.delete(DatabaseHelper.VEHICLES_TABLE,"ownerID = 'test'");
        dbManager.delete(DatabaseHelper.TAXES_TABLE,"vehicle_registration = 'FU2334CK'");
        dbManager.close();
    }
}
