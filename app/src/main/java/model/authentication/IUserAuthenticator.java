package model.authentication;

public interface IUserAuthenticator {
    // UserManager provides the user info from the tree map
    // Will return true if the user info matches false otherwise
    boolean authenticateLogin(String mail, String password);
    boolean validateRegister(String mail, String password);
}
