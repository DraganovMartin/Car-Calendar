package model;

import model.authentication.ICredentialChecker;

/**
 * Singleton class UserManager for creating, registering and managing users.
 */
public class UserManager implements ICredentialChecker {
    private static UserManager manager = new UserManager();

    public static UserManager getInstance() {
        return manager;
    }

    private UserManager() {


    }

    @Override
    public boolean authenticate(String mail, String password) {
        return false;
    }

    private class User{
        private String name;
        private int age;
        private int id;
        private String email;
        //private TreeSet<Vehicle> ownedVehicles;

    }
}
