package model.Vehicle;

/**
 *  Abstract Vehicle class
 */

public abstract class Vehicle {
    private int productionYear;
    private int productionMonth;
    private int productionDay;
    private String registrationPlate;
    private static int id=0;

    public Vehicle(int productionYear,int productionMonth,int productionDay,String registrationPlate){
        this.productionYear = productionYear;
        this.productionMonth = productionMonth;
        this.productionDay = productionDay;
        this.registrationPlate = registrationPlate;
        id++;
    }

    public int getId(){
        return id;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public int getProductionMonth() {
        return productionMonth;
    }

    public int getProductionDay() {
        return productionDay;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }
}
