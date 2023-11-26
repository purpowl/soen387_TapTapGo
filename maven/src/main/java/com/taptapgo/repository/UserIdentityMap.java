package com.taptapgo.repository;

import java.util.HashMap;

import com.taptapgo.Order;
import com.taptapgo.User;
import com.taptapgo.exceptions.InvalidParameterException;


public class UserIdentityMap {
    private HashMap<String, User> userMap;
    private static UserIdentityMap instance = null;

    private UserIdentityMap() {
        userMap = new HashMap<>();
    }

    /**
     * get identity map instance, create one if none exists yet
     * @return UserIdentityMap object
     */
    public static UserIdentityMap getInstance() {
        if (instance == null) {
            instance = new UserIdentityMap();
        }
        return instance;
    }

    /**
     * add a guest user to database and identity map
     * @param user a User object
     * @return boolean true if succeed, false if failed
     * @throws InvalidParameterException from createGuestUserInDatabase
     */
    public static synchronized boolean addGuestUserToDB(User user) throws InvalidParameterException {
        boolean addToDBResult = UserRepository.createGuestUserInDatabase(user);

        if (addToDBResult) {
            UserIdentityMap.getInstance().userMap.put(user.getUserID(), user);
            return true;
        }
        return false;
    }

    /**
     * add a registered user to database and identity map
     * @param user a User object
     * @return boolean true if success, false if not
     * @throws InvalidParameterException if user is not guest
     * @throws InvalidParameterException from createRegisteredUserInDB
     */
    public static synchronized boolean addRegisteredUserToDB(User user, String passcode) throws InvalidParameterException {
        boolean addToDBResult = UserRepository.createRegisteredUserInDB(user, passcode);

        if (addToDBResult) {
            UserIdentityMap.getInstance().userMap.put(user.getUserID(), user);
            return true;
        }
        return false;
    }

    /**
     * get a user given their userID
     * @param userID a string
     * @return User object if succeed, null if failed
     * @throws InvalidParameterException from getUserFromDatabaseByID
     */
    public static synchronized User getUserByID(String userID) throws InvalidParameterException {
        User userMapResult = UserIdentityMap.getInstance().userMap.get(userID);

        if (userMapResult == null) {
            User userFound = UserRepository.getUserFromDatabaseByID(userID);

            // add user to Identity Map if found
            if (userFound != null)
                UserIdentityMap.getInstance().userMap.put(userID, userFound);
            return userFound;
        }
        else {
            return userMapResult;
        }
    }

    /**
     * check if a given passcode exists in database
     * @param passcode the passcode we want to check
     * @return UserID of the user with the passcode, null if no match found
     */
    public static synchronized String authenticate(String passcode) {
        return UserRepository.authenticate(passcode);
    }
    
    /**
     * get largest userID from the database
     * @return Integer object if succeed, null if failed
     */
    public static synchronized Integer getMaxRegisteredUserID() {
        return UserRepository.getMaxRegisteredUserID();
    }
    
    /**
     * Change the passcode for a user in database
     *
     * @param userID user we want to change passcode for
     * @param newPasscode new passcode to set
     * @return true on success, false on failure
     */
    public static synchronized boolean changeUserPasscode(String userID, String newPasscode) throws InvalidParameterException {
        return UserRepository.changeUserPasscodeInDB(userID, newPasscode);
    }


    /**
     * Update the information for a user
     * 
     * @param userID user we want to update information for
     * @param firstName the new first name to be set. This field can be Null if user doesn't want to change first name.
     * @param lastName the new last name to be set. This field can be Null if user doesn't want to change last name.
     * @param phone the new phone to be set. This field can be Null if user doesn't want to change phone number.
     * @param email the new email to be set. This field can be Null if user doesn't want to change email address.
     * @return true on success, false on failure
     */
    public static synchronized boolean updateUserInfo(String userID, String firstName, String lastName, String phone, String email) {
        User userMapResult = UserIdentityMap.getInstance().userMap.get(userID);

        if (userMapResult != null) {
            if(UserRepository.updateUserInfo(userID, firstName, lastName, phone, email)) {
                if(firstName != null) {
                    userMapResult.setFirstName(firstName);
                }
                if (lastName != null) {
                    userMapResult.setLastName(lastName);
                }
                if(phone != null) {
                    userMapResult.setPhone(phone);
                }
                if(email != null) {
                    userMapResult.setEmail(email);
                }

                return true;
            }
            return false;
        } else {
            return UserRepository.updateUserInfo(userID, firstName, lastName, phone, email);
        }
    }

    /**
     * Load all users from database into memory
     * 
     * @return A HashMap mapping each userID to an User object
     */
    public static synchronized HashMap<String, User> loadAllUsers() {
        HashMap<String, User> usersFromDB = UserRepository.loadAllUsers(UserIdentityMap.getInstance().userMap);

        instance.userMap.putAll(usersFromDB);

        return instance.userMap;
    }
}