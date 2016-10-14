package model.storage;

import model.Vehicle.Vehicle;

public interface Storage {
    void store(Vehicle vehicle);
    Vehicle retrieve();
}
