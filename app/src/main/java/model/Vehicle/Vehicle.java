package model.Vehicle;

import java.io.Serializable;

import model.Stickers.IVignette;

/**
 *  Abstract Vehicle class
 */

public abstract class Vehicle implements Serializable {
    private String brand;
    private String model;
    private int productionYear;
    private String registrationPlate;
    private IVignette vignette;
    private static int id=0;
    private int myId;

    public Vehicle(){

    }

    public Vehicle(String brand, String model,int productionYear,String registrationPlate, IVignette vignette){
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.registrationPlate = registrationPlate;
        this.vignette = vignette;
        this.myId = ++id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        if(brand != null && !brand.isEmpty()) {
            this.brand = brand;
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if(model != null && !model.isEmpty()) {
            this.model = model;
        }
    }


    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public void setVignette(IVignette vignette) {
        this.vignette = vignette;
    }

    public int getId(){
        return myId;
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

}
