package model.Vehicle;

import java.io.Serializable;

import model.Stickers.Insurance;
import model.taxes.Tax;
import model.taxes.VehicleTax;

/**
 *  Abstract Vehicle class
 */

public abstract class Vehicle implements Serializable,Comparable<Vehicle> {
    private String brand;
    private String model;
    private int productionYear;
    private String registrationPlate;
    private String registrationPlateCache;
    private String pathToImage;
    private Insurance insurance;
    private VehicleTax tax;
    private String nextOilChange;
    private static int id=0;
    private int myId;

    public Vehicle(){
        myId = ++id;
        insurance = new Insurance();
        tax = new VehicleTax();
    }

    public Vehicle(String brand, String model,int productionYear,String registrationPlate, String nextOilChange, String pathToImage){
        this.brand = brand;
        this.model = model;
        this.pathToImage = pathToImage;
        this.productionYear = productionYear;
        this.registrationPlate = registrationPlate;
        this.myId = ++id;
        this.nextOilChange = nextOilChange;
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

    public int getId(){
        return myId;
    }
    public void setId(int id) {
        myId = id;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }
    public void setRegistrationPlateCache(String registrationPlateCache) {
        this.registrationPlateCache = registrationPlateCache;
    }
    public String getRegistrationPlateCache() {
        return registrationPlateCache;
    }

    @Override
    public int compareTo(Vehicle vehicle) {
        if(this.brand.compareToIgnoreCase(vehicle.brand) == 0){
            if(this.model.compareToIgnoreCase(vehicle.model) == 0){
                if(this.myId == vehicle.myId) return 0;
                else return 1;
            }
            else{
                return  this.model.compareToIgnoreCase(vehicle.model);
            }
        }
        else return this.brand.compareToIgnoreCase(vehicle.brand);
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    /**
     *
     * @return Tax object
     */
    public Tax getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax.setAmount(tax);
    }

    public String getNextOilChange() {
        return nextOilChange;
    }

    public void setNextOilChange(String nextOilChange) {
        this.nextOilChange = nextOilChange;
    }

    public void setTax(VehicleTax tax){
        this.tax = tax;
    }

    @Override
    public boolean equals(Object obj) {
        Vehicle toCompare = (Vehicle) obj;
        if(this.brand.equals(toCompare.brand)){
            if (this.model.equals(toCompare.model)){
                if (this.myId == toCompare.myId) return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.brand.hashCode()*this.model.hashCode()*this.myId;
    }

    @Override
    public String toString() {
        return "Brand : " + brand + "\n " + "Model : " + model + "\n " + "Year : " + productionYear + "\n " + "Registration : " + registrationPlate + "\n " + "Next oil : " + nextOilChange + "\n " ;
    }
}
