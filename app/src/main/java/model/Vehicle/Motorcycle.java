package model.Vehicle;

/**
 * Created by DevM on 12/24/2016.
 */

public class Motorcycle extends Vehicle {
    private String engineType;
    private String motorcycleType;
    private String kmRange;

    public Motorcycle(){
        super();
    }

    public Motorcycle(String registrationPlate, String brand, String model, String motorcycleTypeType, String engineType,
                      String range, String pathToImage, int productionYear,int nextOilChange) {

        super(registrationPlate, brand, model, pathToImage, productionYear,nextOilChange);

        this.motorcycleType = motorcycleTypeType;
        this.engineType = engineType;
        this.kmRange = range;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        if(engineType != null && !engineType.isEmpty()) {
            this.engineType = engineType;
        }
    }

    public String getMotorcycleType() {
        return motorcycleType;
    }

    public void setMotorcycleType(String carType) {

        if(carType != null && !carType.isEmpty()) {
            this.motorcycleType = carType;
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

}