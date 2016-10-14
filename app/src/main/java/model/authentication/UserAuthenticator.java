package model.authentication;

public interface UserAuthenticator {
    // UserManager provides the user info from the tree map
    // Will return true if the user info matches false otherwise
    boolean authenticate(String mail, String password);
}
