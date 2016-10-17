package model;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Vehicle.Vehicle;
import model.authentication.IUserAuthenticator;

/**
 * Singleton class UserManager for creating, registering and managing users.
 */
public class UserManager implements IUserAuthenticator {
    private static UserManager manager = new UserManager();
    public static UserManager getInstance() {
        return manager;
    }
    private TreeSet<User> registeredUSers;

    private UserManager() {

        registeredUSers = new TreeSet<User>();
    }

    public User createUser(String name,String password, int age, String email){
        User tmp = new User(name,password,age,email);
        return tmp;
    }

    private boolean isPasswordGood(String password){
        final Pattern passPattern = Pattern.compile( "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
        Matcher matcher = passPattern.matcher(password);
        return matcher.matches();
    }

    public void registerUser(User x){
        registeredUSers.add(x);
    }

    /**
     *
     * @param mail
     * @param password
     * @return true if login details are correctly entered, false otherwise
     */
    @Override
    public boolean authenticateLogin(String mail, String password)
    {
        for (User x: registeredUSers) {
            if(x.email.equals(mail) && x.password.equals(password)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param mail
     * @param password
     * @return true if mail and password are good, false otherwise
     */
    @Override
    public boolean validateRegister(String mail, String password) {
        if(registeredUSers.isEmpty()){

            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(mail);
            boolean valid = matcher.matches();
            if(valid && isPasswordGood(password)){
                return true;
            }
            else return false;
        }
        if(!registeredUSers.isEmpty()) {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(mail);
            boolean valid = matcher.matches();
            if(valid && isPasswordGood(password)){
                for (User x : registeredUSers) {
                    if (x.email.equals(mail)) {
                        return false;
                    }
                }
            }
            else return false;

        }
        return false;
    }

    private class User implements Comparable<User> {
        private String name;
        private String password;
        private int age;
        private int id;
        private String email;
        private TreeSet<Vehicle> ownedVehicles;

        private User(String name,String password, int age, String email){
            this.name = name;
            this.age = age;
            this.email = email;
            this.password = password;
            ownedVehicles = new TreeSet<Vehicle>();
        }

        private void addVehicle(Vehicle x){
            if(x != null){
                ownedVehicles.add(x);
            }
            else{
                throw new NullPointerException();
            }
        }

        private void removeVehicle(Vehicle x){
            if(ownedVehicles.contains(x)){
                ownedVehicles.remove(x);
            }
        }

        @Override
        public int compareTo(User user) {
            if (this.id == user.id) {
                return 0;
            }
            if (this.id < user.id) {
                return -1;
            }
                return 1;
        }
    }
}
