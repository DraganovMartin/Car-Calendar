package model.Vehicle;

import java.io.Serializable;

import model.Stickers.IVignette;

/**
 *  Abstract Vehicle class
 */

public abstract class Vehicle implements Serializable,Comparable<Vehicle> {
    private int productionYear;
    private String registrationPlate;
    private IVignette vignette;
    private static int id=0;

    public Vehicle(){

    }

    public Vehicle(int productionYear,String registrationPlate, IVignette vignette){
        this.productionYear = productionYear;
        this.registrationPlate = registrationPlate;
        this.vignette = vignette;
        id++;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public void setVignette(IVignette vignette) {
        this.vignette = vignette;
    }

    public int getId(){
        return id;
    }

    public int getProductionYear() {
        return productionYear;
    }


    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public IVignette getVignette(){
        return vignette;
    }

    @Override
    public int compareTo(Vehicle vehicle) {
        if(id < vehicle.getId()) return -1;
        if(id == vehicle.getId()) return 0;
        return 1;
    }
}
