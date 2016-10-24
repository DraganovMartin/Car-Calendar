package model;

import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Vehicle.Vehicle;
import model.authentication.IUserAuthenticator;

/**
 * Singleton class UserManager for creating, registering and managing users.
 */
public class UserManager implements IUserAuthenticator,Serializable {

    private static UserManager manager = new UserManager();
    public static UserManager getInstance() {
        return manager;
    }
    private TreeSet<User> registeredUsers;
    private static int userId =0;
    private User loggedUser;

    private UserManager() {

        registeredUsers = new TreeSet<User>();
    }

    /**
     * Increments userId and passes it to the created user.
     */
    public User createUser(String name,String password, int age){
        userId++;
        User tmp = new User(name,password,age,userId);
        return tmp;
    }

    /**
     *  Creates user without incrementing user id, this method should be used only for getting the user from intents,bundle,etc..
     *
     */
    /*public User createUser(User x){
        User tmp = x;
        return tmp;
    }*/

    private boolean isPasswordGood(String password){
        final Pattern passPattern = Pattern.compile( "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
        Matcher matcher = passPattern.matcher(password);
        return matcher.matches();
    }

    public void registerUser(User x){
        registeredUsers.add(x);
    }

   /* public int getLoggedUserID(String mail, String password){
        for (User x: registeredUsers.values()) {
            if(x.email.equals(mail) && x.password.equals(password)){
                return x.id;
            }
        }
        return -1;
    }*/

    public String getLoggedUserName() {
       return loggedUser.name;
   }

    public User getLoggedUser() {
        return loggedUser;
    }

    public TreeSet<User> getRegisteredUsers() {
        return registeredUsers;
    }

    /**
     *  setting "loggedUser" to null !!
     */
    public void userLogout(){
        loggedUser = null;
    }
    public void userLogin(User x){loggedUser = x;}

    /**
     *
     * @param username - String
     * @param password - String
     * @return true if login details are correctly entered, false otherwise
     */
    @Override
    public boolean authenticateLogin(String username, String password)
    {
        username.trim();
        for (User x: registeredUsers) {
            if(x.name.equals(username) && x.password.equals(password)){
                Log.e("authenticate",x.name);
                this.userLogin(x);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param username - String
     * @param password - String
     * @return true if mail and password are good, false otherwise
     */
    @Override
    public boolean validateRegister(String username, String password) {
        username.trim();
        if(registeredUsers.isEmpty()){

            if(!username.isEmpty() && isPasswordGood(password)){
                return true;
            }
            else return false;
        }
        if(!registeredUsers.isEmpty()) {
            if(!username.isEmpty() && isPasswordGood(password)){
                for (User x : registeredUsers) {
                    if (x.name.equals(username)) {
                        return false;
                    }
                }
                return true;
            }
            else return false;
        }
        return false;
    }

    private class User implements Comparable<User>,Serializable {
        private String name;
        private String password;
        private int age;
        private int id;
        private TreeSet<Vehicle> ownedVehicles;

        private User(String name, String password, int age, int id) {
            this.name = name;
            this.age = age;
            this.password = password;
            ownedVehicles = new TreeSet<Vehicle>();
            this.id = id;
        }
        private User(User x){
            this.name = x.name;
            this.age = x.age;
            this.password = x.password;
            ownedVehicles = x.ownedVehicles;
            this.id = x.id;
        }

        private void addVehicle(Vehicle x) {
            if (x != null) {
                ownedVehicles.add(x);
            } else {
                throw new NullPointerException();
            }
        }

        private void removeVehicle(Vehicle x) {
            if (ownedVehicles.contains(x)) {
                ownedVehicles.remove(x);
            }
        }

        @Override
        public int compareTo(User user) {
            if(this.id < user.id) return -1;
            if(this.id == user.id) return 0;
            return 1;
        }
    }
}
