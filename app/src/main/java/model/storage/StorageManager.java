package model.storage;

import model.Vehicle.Vehicle;

public class StorageManager implements Storage{
    private static StorageManager ourInstance = new StorageManager();

    public static StorageManager getInstance() {
        return ourInstance;
    }

    private StorageManager() {
    }

    @Override
    public void store(Vehicle vehicle) {

    }

    @Override
    public Vehicle retrieve() {
        return null;
    }
}
