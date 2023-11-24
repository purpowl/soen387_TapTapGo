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

    /**
     * private User constructor to create guest user with only session ID
     * @param sessionID string
     * @throws InvalidParameterException from Customer constructor if unable to create guest customer
     */
    private User(String sessionID) throws InvalidParameterException {
        this.userID = "gc" + sessionID;
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.email = null;
        // create guest customer instance
        this.customer = new Customer("guest");
        // guest cannot be staff
        this.staff = null;
    }

    /**
     * private User constructor to create guest user with session ID and personal info
     * @param sessionID string
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @throws InvalidParameterException from Customer constructor if unable to create guest customer
     */
    private User(String sessionID, String firstName, String lastName, String phone, String email) throws InvalidParameterException {
        this.userID = "gc" + sessionID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        // create guest customer instance
        this.customer = new Customer("guest");
        // guest cannot be staff
        this.staff = null;
    }

    /**
     * private User constructor to create registered user with no ID yet
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @param isStaff boolean
     * @throws InvalidParameterException from Customer constructor if unable to create registered customer
     */
    private User(String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        registeredIDGen.incrementAndGet();
        this.userID = "rc" + String.format("%05d", registeredIDGen.get());
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        // create registered customer instance
        this.customer = new Customer("registered");

        // if user is also staff, create staff instance
        if (isStaff) {
            this.staff = new Staff();
        }
        else
            this.staff = null;
    }

    /**
     * private User constructor to create registered user with existing ID
     * @param userID string
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @param isStaff boolean
     * @throws InvalidParameterException from Customer constructor if unable to create registered customer
     */
    private User(String userID, String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        // create registered customer instance
        this.customer = new Customer("registered");

        // if user is also staff, create staff instance
        if (isStaff) {
            this.staff = new Staff();
        }
        else
            this.staff = null;
    }

    /**
     * public method to create registered user with existing ID
     * @param userID string
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @param isStaff boolean
     * @return User object
     * @throws InvalidParameterException from private constructor if unable to create registered customer
     */
    public static User loadRegisteredUser(String userID, String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        return new User(userID, firstName, lastName, phone, email, isStaff);
    }

    /**
     * public method to create registered user with new ID
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @param isStaff boolean
     * @return User object
     * @throws InvalidParameterException from private constructor if unable to create registered customer
     */
    public static User createRegisteredUser(String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        return new User(firstName, lastName, phone, email, isStaff);
    }

    /**
     * public method to create guest user with only session ID
     * @param sessionID string
     * @return User object
     * @throws InvalidParameterException from private constructor if unable to create guest customer
     */
    public static User createGuestUser(String sessionID) throws InvalidParameterException {
        return new User(sessionID);
    }

    /**
     * public method to create guest user with session ID and personal info
     * @param sessionID string
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @return User object
     * @throws InvalidParameterException from private constructor if unable to create guest customer
     */
    public static User createGuestUserWithInfo(String sessionID, String firstName, String lastName, String phone, String email) throws InvalidParameterException {
        return new User(sessionID, firstName, lastName, phone, email);
    }

    /**
     * check if user is a registered user
     * @return true if registered, false if not
     */
    public boolean isRegisteredUser() {
        return this.getCustomer().customerTypeToString().equals("registered");
    }

    /**
     * check if user is staff
     * @return true if staff, false if not
     */
    public boolean isStaff() {
        return this.staff != null;
    }

    /**
     * add this user (guest or registered) to the database
     * @param passcode the passcode of the new user
     * @return true if successfully set, false if not
     */
    public boolean addUserToDB(String passcode) throws InvalidParameterException {
        if (this.isRegisteredUser()) {
            return UserIdentityMap.addRegisteredUserToDB(this, passcode);
        }
        else {
            return UserIdentityMap.addGuestUserToDB(this);
        }
    }

    /**
     * Set the passcode for an existing registered user
     * @param newPasscode the passcode we want to set
     * @return true if successfully set, false if not
     * @throws InvalidParameterException if user is not registered, or unable to set new passcode
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