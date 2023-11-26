package com.taptapgo.repository;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;

import com.taptapgo.User;
import com.taptapgo.exceptions.InvalidParameterException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserRepository {
    private static Connection db_conn;
    
    /**
     * private helper method to create a new guest user in the database
     * @param user a User object
     * @return boolean true if success, false if not
     * @throws InvalidParameterException if user is not guest
     */
    protected static synchronized boolean createGuestUserInDatabase(User user) throws InvalidParameterException {

        // check if user is guest
        if (user.isRegisteredUser()) {
            throw new InvalidParameterException("Guest user required.");
        }

        String insertQuery = "INSERT INTO guestuser (GuestID, FirstName, LastName, Phone, Email) VALUES (?, ?, ?, ?, ?)";
        Savepoint savepoint = null;

        try {
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

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
    protected static synchronized boolean createRegisteredUserInDB(User user, String passcode) throws InvalidParameterException {
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
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

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
    protected static synchronized User getUserFromDatabaseByID(String userID) throws InvalidParameterException {
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
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

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
        return null;
    }

    /**
     * get largest userID from the database
     * @return Integer object if succeed, null if failed
     */
    protected static synchronized Integer getMaxRegisteredUserID() {
        String query = "SELECT MAX(UserID) FROM registereduser";

        try {
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            Statement stmt = db_conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);

            if (queryResult.next()) {
                // get the highest userID
                String maxUserID_str = queryResult.getString(1);
                Integer maxUserID = Integer.parseInt(maxUserID_str.substring(2));
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
    }

    /**
     * Change the passcode for a user in database
     *
     * @param userID user we want to change passcode for
     * @param newPasscode new passcode to set
     * @return true on success, false on failure
     */
    protected static synchronized boolean changeUserPasscodeInDB(String userID, String newPasscode) throws InvalidParameterException {
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
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

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
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

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
    }


    public static synchronized boolean updateUserInfo(String userID, String firstName, String lastName, String phone, String email) {
        String updateQuery = "UPDATE registeredUser SET ";
        ArrayList<String> updateFields = new ArrayList<>();
        Savepoint savepoint = null;

        if (firstName != null) {
            if (updateQuery.length() > 26){
                updateQuery += ",";
            }
            updateQuery += "FirstName = ?";
            updateFields.add(firstName);
        }
        if (lastName != null) {
            if (updateQuery.length() > 26){
                updateQuery += ",";
            }
            updateQuery += "LastName = ?";
            updateFields.add(lastName);
        }
        if (phone != null) {
            if (updateQuery.length() > 26){
                updateQuery += ",";
            }
            updateQuery += "Phone = ?";
            updateFields.add(phone);
        }
        if (email != null) {
            if (updateQuery.length() > 26){
                updateQuery += ",";
            }
            updateQuery += "Email = ?";
            updateFields.add(email);
        }
        updateQuery += " WHERE UserID = ?";

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);
            System.out.println("Editing on Database URL: " + dbUrl.toString());

            // Save checkpoint to rollback in case update fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            // Load the values into prepared statement
            PreparedStatement pstmt = db_conn.prepareStatement(updateQuery);
            for (int i = 0; i < updateFields.size(); i++) {
                pstmt.setString((i+1), updateFields.get(i));
            }
            pstmt.setString(updateFields.size()+1, userID);
            int result = pstmt.executeUpdate();
            if (result < 1) {
                throw new SQLException("User information update failed!");
            }

            db_conn.commit();
            db_conn.setAutoCommit(true);
            db_conn.close();

            return true;
        } catch (SQLException e) {
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return false;
        }
    }
}
