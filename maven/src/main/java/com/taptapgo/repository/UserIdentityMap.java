package com.taptapgo.repository;

import java.sql.*;
import java.util.HashMap;
import com.taptapgo.User;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.taptapgo.exceptions.InvalidParameterException;

import java.sql.Savepoint;


public class UserIdentityMap {
    private HashMap<String, User> userMap;
    private static UserIdentityMap instance = null;
    private static Connection db_conn;

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
        boolean addToDBResult = createGuestUserInDatabase(user);

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
        boolean addToDBResult = createRegisteredUserInDB(user, passcode);

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
            User userFound = getUserFromDatabaseByID(userID);

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
     * private helper method to create a new guest user in the database
     * @param user a User object
     * @return boolean true if success, false if not
     * @throws InvalidParameterException if user is not guest
     */
    private static synchronized boolean createGuestUserInDatabase(User user) throws InvalidParameterException {

        // check if user is guest
        if (user.isRegisteredUser()) {
            throw new InvalidParameterException("Guest user required.");
        }

        String insertQuery = "INSERT INTO guestuser (GuestID, FirstName, LastName, Phone, Email) VALUES (?, ?, ?, ?, ?)";
        Savepoint savepoint = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt = db_conn.prepareStatement(insertQuery);

            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getEmail());

            int result = pstmt.executeUpdate();

            db_conn.commit();
            db_conn.setAutoCommit(true);
            db_conn.close();

            return result == 1;
        }
        catch(SQLException | ClassNotFoundException e) {
            try {
                if (db_conn != null && savepoint != null) {
                    db_conn.rollback(savepoint);
                    db_conn.setAutoCommit(true);
                    db_conn.close();
                }

            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * private helper method to create a new registered user in the database
     * @param user a User object
     * @return boolean true if success, false if not
     * @throws InvalidParameterException if user is not registered or passcode provided is not unique
     */
    private static synchronized boolean createRegisteredUserInDB(User user, String passcode) throws InvalidParameterException {
        // check if user is registered
        if (!user.isRegisteredUser()) {
            throw new InvalidParameterException("Registered user required.");
        }

        // check if passcode exists in database
        String passcodeOwner = authenticate(passcode);

        // if passcode already exists
        if (passcodeOwner != null) {
            // the new passcode coincide with another user's passcode
            throw new InvalidParameterException("Invalid passcode.");
        }

        // hash passcode
        String hashedPasscode = BCrypt.withDefaults().hashToString(12, passcode.toCharArray());

        Savepoint savepoint = null;

        String insertQuery = "INSERT INTO registereduser (UserID, FirstName, LastName, Phone, Email, Passcode, isStaff) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt = db_conn.prepareStatement(insertQuery);

            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, hashedPasscode);
            pstmt.setInt(7, (user.isStaff()) ? 1 : 0);

            int result = pstmt.executeUpdate();

            db_conn.commit();
            db_conn.setAutoCommit(true);
            db_conn.close();

            return result == 1;
        }
        catch(SQLException | ClassNotFoundException e) {
            try {
                if (db_conn != null && savepoint != null) {
                    db_conn.rollback(savepoint);
                    db_conn.setAutoCommit(true);
                    db_conn.close();
                }

            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * private helper method to get a user from the database using their ID
     * @param userID a string
     * @return User object if succeed, null if failed
     * @throws InvalidParameterException if userID is not for guest or registered
     */
    private static synchronized User getUserFromDatabaseByID(String userID) throws InvalidParameterException {
        String query;

        // create different queries for different user types guest and registered
        if (userID.startsWith("gc")) {
            query = "SELECT GuestID, FirstName, LastName, Phone, Email FROM guestuser WHERE GuestID = ?";
        }
        else if (userID.startsWith("rc")) {
            query = "SELECT UserID, FirstName, LastName, Phone, Email, isStaff FROM registereduser WHERE UserID = ?";
        }
        else {
            throw new InvalidParameterException("Invalid user ID");
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(query);
            pstmt.setString(1, userID);

            ResultSet queryResult = pstmt.executeQuery();

            if (queryResult.next()) {
                String id = queryResult.getString(1);
                String firstName = queryResult.getString(2);
                String lastName = queryResult.getString(3);
                String phone = queryResult.getString(4);
                String email = queryResult.getString(5);

                // get isStaff for registered customer
                // create a new user from the attributes acquired
                if (id.startsWith("rc")) {
                    boolean isStaff = queryResult.getInt(6) == 1;
                    queryResult.close();
                    db_conn.close();
                    return User.loadRegisteredUser(id, firstName, lastName, phone, email, isStaff);
                }
                else {
                    queryResult.close();
                    db_conn.close();
                    return User.createGuestUserWithInfo(id, firstName, lastName, phone, email);
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * get largest userID from the database
     * @return Integer object if succeed, null if failed
     */
    public static synchronized Integer getMaxRegisteredUserID() {
        String query = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(UserID, ' ', -1)), 3) FROM registereduser";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            Statement stmt = db_conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);

            if (queryResult.next()) {
                // get the userID without the first 2 characters
                Integer maxUserID = Integer.parseInt(queryResult.getString(1));
                stmt.close();
                db_conn.close();
                return maxUserID;
            } else {
                stmt.close();
                db_conn.close();
                return 0;
            }

        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Change the passcode for a user in database
     *
     * @param userID user we want to change passcode for
     * @param newPasscode new passcode to set
     * @return true on success, false on failure
     */
    public static synchronized boolean changeUserPasscodeInDB(String userID, String newPasscode) throws InvalidParameterException {
        // check if passcode exists in database
        String passcodeOwner = authenticate(newPasscode);

        // if passcode already exists
        if (passcodeOwner != null) {
            // the new passcode is the same as the user's current one
            if (passcodeOwner.equals(userID)) {
                throw new InvalidParameterException("You cannot set the same password.");
            }
            // the new passcode coincide with another user's passcode
            else {
                throw new InvalidParameterException("Invalid passcode.");
            }
        }

        // hash the passcode
        String hashedPasscode = BCrypt.withDefaults().hashToString(12, newPasscode.toCharArray());

        String changePasscodeQuery = "UPDATE `registereduser` SET Passcode = ? WHERE UserID = ?";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("com.mysql.jdbc.Driver");
            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            // Save checkpoint to rollback in case update fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            // make SQL statement to update user passcode
            PreparedStatement pstmt = db_conn.prepareStatement(changePasscodeQuery);
            pstmt.setString(1, hashedPasscode);
            pstmt.setString(2, userID);

            pstmt.executeUpdate();
            db_conn.commit();

            db_conn.setAutoCommit(true);
            db_conn.close();

            return true;
        } catch (SQLException | ClassNotFoundException e) {
            try {
                if (db_conn != null && savepoint != null) {
                    db_conn.rollback(savepoint); // Rollback the transaction if an exception occurs
                    db_conn.setAutoCommit(true);
                    db_conn.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * check if a given passcode exists in database
     * @param passcode the passcode we want to check
     * @return UserID of the user with the passcode, null if no match found
     */
    public static synchronized String authenticate(String passcode) {

        BCrypt.Verifyer verifyer = BCrypt.verifyer();
        String getPasscodeQuery = "SELECT UserID, Passcode FROM registereduser";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(getPasscodeQuery);
            ResultSet queryResult = pstmt.executeQuery();

            // run through the hashedpasscodes from database
            while (queryResult.next()) {
                String hashedPasscode = queryResult.getString(2);

                String userID;

                // check if it matches the input passcode
                BCrypt.Result result = verifyer.verify(passcode.toCharArray(), hashedPasscode);

                // return user ID if match found
                if (result.verified) {
                    userID =  queryResult.getString(1);
                    db_conn.close();
                    return userID;
                }
            }
            db_conn.close();
            return null;

        }
        catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}