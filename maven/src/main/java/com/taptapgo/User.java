package com.taptapgo;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.taptapgo.exceptions.InvalidParameterException;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static AtomicInteger registeredIDGen = new AtomicInteger(0);
    protected String userID;
    protected String firstName;
    protected String lastName;
    protected String phone;
    protected String email;
    protected Customer customer;
    protected Staff staff;

    public User(String sessionID) throws InvalidParameterException {
        this.userID = "gc" + sessionID;
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.email = null;
        this.customer = new Customer("guest");
        this.staff = null;
    }

    public User(String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        registeredIDGen.incrementAndGet();
        this.userID = "rc" + String.format("%05d", registeredIDGen.get());
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.customer = new Customer("registered");
        if (isStaff) {
            this.staff = new Staff();
        }
        else
            this.staff = null;
    }

    public User(String userID, String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.customer = new Customer("registered");
        if (isStaff) {
            this.staff = new Staff();
        }
        else
            this.staff = null;
    }

    public static User loadRegisteredUser(String UserID, String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        // check if the password matches the user with userID in db
        return new User(firstName, lastName, phone, email, isStaff);
    }

    public static User createRegisteredUser(String UserID, String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        return new User(UserID, firstName, lastName, phone, email, isStaff);
        //this.encryptedPasscode = BCrypt.withDefaults().hashToString(12, password.toCharArray());;
    }

    public static User createGuestUser(String sessionID) throws InvalidParameterException {
        return new User(sessionID);
    }

//    public boolean changeUsername(String oldUsername, String newUsername, String password){
//        if (oldUsername == null || newUsername == null) return false;
//        else if (oldUsername.equals(newUsername)) return false;
//        else if (this.username.equals(oldUsername) && this.password.equals(password)) {
//            this.username = newUsername;
//            return true;
//        }
//        else return false;
//    }

//    public boolean changePassword(String username, String oldPassword, String newPassword){
//        if (this.password != null && authenticate(username, oldPassword)) {
//            this.password = newPassword;
//            return true;
//        }
//        else return false;
//    };

    public String getUserID() {
        return this.userID;
    }

    public static void updateRegisteredIDGen(int maxID) {
        registeredIDGen = new AtomicInteger(maxID);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Staff getStaff() {
        return (staff != null) ? staff : null;
    }

    public Customer getCustomer() {
        return (customer != null) ? customer : null;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null){
            return false;
        }
        if (!(object.getClass().equals(this.getClass()))){
            return false;
        }

        User otherUser = (User) object;

        if(otherUser.userID.equals(this.userID) && otherUser.firstName.equals(this.firstName) && otherUser.lastName.equals(this.lastName)){
            return true;
        }
        return false;
    }

 }