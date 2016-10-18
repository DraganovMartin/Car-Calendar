package model.storage;

import model.Vehicle.Vehicle;

public interface Storable {
    void store(Vehicle vehicle);
    Vehicle retrieve();
}
