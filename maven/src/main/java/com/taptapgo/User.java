package com.taptapgo;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class User {
    public static final int USER_COOKIE_DURATION_SEC = 300;
    private static AtomicInteger guestIDGen = new AtomicInteger(0);
    private static AtomicInteger registeredIDGen = new AtomicInteger(0);
    private static AtomicInteger staffIDGen = new AtomicInteger(0);
    protected String userID;
    protected String username;
    protected String password;


    public User(String sessionID) {
        // guestIDGen.incrementAndGet();
        // this.userID = "gc" + String.format("%05d", guestIDGen.get());
        this.userID = "gc" + sessionID;
        this.username = null;
        this.password = null;
    }
    public User(String userID) {
        this.userID = userID;
        this.username = null;
        this.password = null;
    }

    public User(String userType, String username, String password) {
        if (userType.equals("staff")) {
            staffIDGen.incrementAndGet();
            this.userID = "s" + String.format("%05d", staffIDGen.get());
            this.username = username;
        }
        else if (userType.equals("registered")) {
            registeredIDGen.incrementAndGet();
            this.userID = "rc" + String.format("%05d", registeredIDGen.get());
            this.username = username;
        }
        this.password = password;
    }

    public User(String userType, String userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
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

    // if the user has a password, authenticate them
    public boolean authenticate(String inputPassword) {
        if (inputPassword != null && this.password != null) return (inputPassword.equals(password));
        else return false;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getUserName() {
        return this.username;
    }

    public static void updateGuestIDGen(int maxID) {
        guestIDGen = new AtomicInteger(maxID);
    } 

    public static void updateRegisteredIDGen(int maxID) {
        guestIDGen = new AtomicInteger(maxID);
    } 

    public static void updateStaffIDGen(int maxID) {
        guestIDGen = new AtomicInteger(maxID);
    } 
}