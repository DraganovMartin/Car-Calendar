package com.carcalendar.dmdev.carcalendar.authentication;

public class UserAuthenticator {
    private static UserAuthenticator ourInstance = new UserAuthenticator();

    public static UserAuthenticator getInstance() {
        return ourInstance;
    }

    private UserAuthenticator() {}

    // UserManager provides the user from the tree map
    // Returns true if the user info matches false otherwise
    public boolean authenticate(/*User user*/){
        //TODO get stored user data and authenticate user

        return false;
    }
}
