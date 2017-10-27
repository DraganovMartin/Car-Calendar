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

    @BeforeClass
    public static void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbManager = new DatabaseManager(appContext);

        // Insert user in db
        dbManager.insertUser("dimcho", "pass", 22);
        dbManager.updateUser("dimcho", "pass", 22, true, true);
        UserManager.getInstance().setLoggedUserUsername("dimcho");


        car.setTax(tax);
        car.setVignette(vignette);
        car.setInsurance(insurance);
        dbManager.insert(car, false);
    }

    @Test
    public void shouldFetchUserFromDB() throws Exception {
        String[] userData  = dbManager.getLoggedUserFromDB();
        assertEquals(3, userData.length);
        assertEquals(userData[0], "dimcho");
        assertEquals(userData[1], "pass");
        assertEquals(userData[2], "22");
    }

    @Test
    public void shouldFetchUserVehicleData() throws Exception {
        List<Vehicle> vehicles = dbManager.getVehiclesForLoggedUser("dimcho");
        assertEquals(1, vehicles.size());

        for (Vehicle vehicle : vehicles) {
            assertEquals(vehicle.getRegistrationPlate(), car.getRegistrationPlate());
            assertEquals(vehicle.getBrand(), car.getBrand());
            assertEquals(vehicle.getModel(), car.getModel());
            assertEquals(vehicle.getProductionYear(), car.getProductionYear());
            assertEquals(vehicle.getPathToImage(), car.getPathToImage());
            assertEquals(vehicle.getPathToImage(), car.getPathToImage());
            assertEquals(vehicle.getNextOilChange(), car.getNextOilChange());

            // Insurance
            Insurance tempInsurance = vehicle.getInsurance();
            assertEquals(tempInsurance.getStartDate(), insurance.getStartDate());
            assertEquals(tempInsurance.getTotalEndDate(), insurance.getTotalEndDate());
            assertEquals(tempInsurance.getTypeForDB(), insurance.getTypeForDB());
            assertEquals(String.valueOf(tempInsurance.getPrice()), String.valueOf(insurance.getPrice()));

            // Tax
            Tax tmpTax = vehicle.getTax();
            assertEquals(tmpTax.getEndDate(), tax.getEndDate());
            assertEquals(tmpTax.getType(), tax.getType());
            assertEquals(String.valueOf(tmpTax.getAmount()), String.valueOf(tax.getAmount()));

            if(vehicle instanceof Car) {
                Car tmpCar = (Car) vehicle;
                assertEquals(tmpCar.getKmRange(), car.getKmRange());
                assertEquals(tmpCar.getEngineType(), car.getEngineType());
                assertEquals(tmpCar.getCarType(), car.getCarType());

                IVignette tmpVignette = tmpCar.getVignette();
                assertEquals(tmpVignette.getStartDate(), vignette.getStartDate());
                assertEquals(tmpVignette.getEndDate(), vignette.getEndDate());
                assertEquals(tmpVignette.getType(), vignette.getType());
                assertEquals(String.valueOf(tmpVignette.getPrice()), String.valueOf(vignette.getPrice()));
            }

        }
    }

    @AfterClass
    public static void tearDown() {
        dbManager.delete(DatabaseHelper.USERS_TABLE, "username = 'dimcho'");
        dbManager.delete(DatabaseHelper.VEHICLES_TABLE,"ownerID = 'dimcho'");
        dbManager.delete(DatabaseHelper.TAXES_TABLE,"vehicle_registration = 'FU2334CK'");
        dbManager.close();
    }
}
