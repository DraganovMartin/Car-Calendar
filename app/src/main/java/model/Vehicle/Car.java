package model.Vehicle;

/**
 * Created by DevM on 12/19/2016.
 */

public class Car extends Vehicle {
    private String engineType;
    public Car(int productionYear, int productionMonth, int productionDay, String registrationPlate) {
        super(productionYear, productionMonth, productionDay, registrationPlate);
    }
}
