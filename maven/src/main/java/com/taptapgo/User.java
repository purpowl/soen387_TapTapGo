package com.taptapgo;

import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.repository.UserIdentityMap;
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

    private User(String sessionID) throws InvalidParameterException {
        this.userID = "gc" + sessionID;
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.email = null;
        this.customer = new Customer("guest");
        this.staff = null;
    }

    private User(String sessionID, String firstName, String lastName, String phone, String email) throws InvalidParameterException {
        this.userID = "gc" + sessionID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.customer = new Customer("guest");
        this.staff = null;
    }

    private User(String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
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

    private User(String userID, String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
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
        return new User(UserID, firstName, lastName, phone, email, isStaff);
    }

    public static User createRegisteredUser(String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        return new User(firstName, lastName, phone, email, isStaff);
    }

    public static User createGuestUser(String sessionID) throws InvalidParameterException {
        return new User(sessionID);
    }

    public static User createGuestUserWithInfo(String sessionID, String firstName, String lastName, String phone, String email) throws InvalidParameterException {
        return new User(sessionID, firstName, lastName, phone, email);
    }

    public boolean isRegisteredUser() {
        return this.getCustomer().customerTypeToString().equals("registered");
    }

    public boolean isStaff() {
        return this.staff != null;
    }

    public boolean addUserToDB(String passcode) throws InvalidParameterException {
        if (this.isRegisteredUser()) {
            return UserIdentityMap.addRegisteredUserToDB(this, passcode);
        }
        else {
            return UserIdentityMap.addGuestUserToDB(this);
        }
    }

    /**
     * Set the passcode for a registered user, raises error if passcode already exists
     * @param newPasscode the passcode we want to set
     * @return true if successfully set, false if not
     */
    public boolean setPasscode(String newPasscode) throws InvalidParameterException {
        // only registered customer can change passcode, guest customer must go through sign up
        if (!this.isRegisteredUser()) {
            throw new InvalidParameterException("User must be registered to change passcode");
        }

        // change the user passcode
        return UserIdentityMap.changeUserPasscodeInDB(this.userID, newPasscode);
    }

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

        return otherUser.userID.equals(this.userID) && otherUser.firstName.equals(this.firstName) && otherUser.lastName.equals(this.lastName);
    }
 }