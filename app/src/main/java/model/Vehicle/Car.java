package model.Vehicle;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by DevM on 12/19/2016.
 */

public class Car extends Vehicle implements Serializable {

    private String engineType;
    private String carType;
    private String kmRange;
    private double vehicleTaxAmount;


    public Car(){
        super();
    }

//    public Car(int productionYear, String registrationPlate, IVignette vignette) {
//        super(productionYear, registrationPlate, vignette);
//    }

    public String getEngineType() {
        return engineType;
    }

//    public void setYear(int year){
//        super.setProductionYear(year);
//    }

    public void setEngineType(String engineType) {
        if(engineType != null && !engineType.isEmpty()) {
            this.engineType = engineType;
        }
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {

        if(carType != null && !carType.isEmpty()) {
            this.carType = carType;
        }
    }

    public String getKmRange() {
        return kmRange;
    }

    public void setKmRange(String kmRange) {
        if(kmRange != null && !kmRange.isEmpty()) {
            this.kmRange = kmRange;
        }
    }

    public double getVehicleTaxAmount() {
        return vehicleTaxAmount;
    }

    public void setVehicleTaxAmount(double vehicleTaxAmount) {
        if(vehicleTaxAmount >= 0) {
            this.vehicleTaxAmount = vehicleTaxAmount;
        }
    }

}
