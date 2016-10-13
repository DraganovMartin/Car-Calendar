package model;

import java.util.TreeSet;

/**
 * Singleton class UserManager for creating, registering and managing users.
 */
public class UserManager {
    private static UserManager manager = new UserManager();

    public static UserManager getInstance() {
        return manager;
    }

    private UserManager() {


    }

    private class User{
        private String name;
        private int age;
        private int id;
        private String email;
        //private TreeSet<Vehicle> ownedVehicles;

    }
}
