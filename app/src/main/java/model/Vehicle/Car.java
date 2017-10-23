package model.Vehicle;

import java.io.Serializable;

import model.Stickers.IVignette;

/**
 * Created by DevM on 12/19/2016.
 */

public class Car extends Vehicle implements Serializable {

    private String engineType;
    private String carType;
    private String kmRange;
    private IVignette vignette;


    public Car(){
        super();
    }

    public Car(String registrationPlate, String brand, String model,String carType, String engineType,
               String range,String pathToImage,int productionYear,int nextOilChange) {

        super(registrationPlate, brand, model, pathToImage, productionYear,nextOilChange);

        this.carType = carType;
        this.engineType = engineType;
        this.kmRange = range;
    }

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

    public void setVignette(IVignette vignette) {
        this.vignette = vignette;
    }
    public IVignette getVignette(){
        return vignette;
    }
}