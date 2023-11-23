package com.taptapgo.repository;

import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.taptapgo.Customer;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserIdentityMap {
    private HashMap<String, Customer> userMap;
    private static UserIdentityMap instance = null;
    private static Connection db_conn;

    private UserIdentityMap() {
        userMap = new HashMap<String, Customer>();
    }

    public static UserIdentityMap getInstance() {
        if (instance == null) {
            instance = new UserIdentityMap();
        }
        return instance;
    }

    public static boolean createCustomer(Customer customer) {
        boolean addToDBResult = createCustomerInDatabase(customer);

        if (addToDBResult) {
            UserIdentityMap.getInstance().userMap.put(customer.getUserID(), customer);
            return true;
        }
        return false;
    }

    public static Customer getCustomerByID(String customerID) {
        Customer userMapResult = UserIdentityMap.getInstance().userMap.get(customerID);

        if (userMapResult == null) {
            return getCustomerFromDatabaseByID(customerID);
        } else {
            return userMapResult;
        }
    }

    public static Customer getCustomerbyUserName(String username) {
        Customer userMapResult = null;
        for (Map.Entry<String, Customer> customer : UserIdentityMap.getInstance().userMap.entrySet()) {
            if (customer.getValue().getUserName().equals(username))
                userMapResult = customer.getValue();
        }

        if (userMapResult == null) {
            return
                    getCustomerFromDatabaseByID(username);
        } else {
            return userMapResult;
        }
    }

    public static boolean authenticateCustomer(String passcode) throws SQLException {

        BCrypt.Verifyer verifyer = BCrypt.verifyer();

        String queryCustomerPasscode = "SELECT Passcode FROM registereduser";

        db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

        PreparedStatement pstmt = db_conn.prepareStatement(queryCustomerPasscode);

        ResultSet customerQueryResult = pstmt.executeQuery();

        if (customerQueryResult.next()) {

        }
        else {
            PreparedStatement pstmt2 = db_conn.prepareStatement(queryStaffPasscode);

        }

        db_conn.close();


        //String bcryptHashString = BCrypt.withDefaults().hashToString(12, passcode.toCharArray());


        //BCrypt.Result result = BCrypt.verifyer().verify(passcode.toCharArray(), bcryptHashString);

//        String content = "";
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        InputStream is = classLoader.getResourceAsStream("/credentials.json");
//
//        assert is != null;
//        Scanner reader = new Scanner(is, "UTF-8");
//        while (reader.hasNextLine()) {
//            content += reader.nextLine() + "\n";
//        }
//        reader.close();
//
//        if(!content.isEmpty()) {
//            JSONArray usersJsonArray = new JSONArray(content);
//            for (int i = 0; i < usersJsonArray.length(); i++) {
//                JSONObject userObj = usersJsonArray.getJSONObject(i);
//                String usernameInFile = userObj.getString("username");
//                if (usernameInFile.equals(username) && password.equals(userObj.getString("password"))) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    private static boolean createCustomerInDatabase(Customer customer) {
        String insertQuery = "";
        if (customer.getUserID().contains("gc")) {
            insertQuery = "INSERT INTO guestuser (GuestID, FirstName, LastName, Phone, Email) VALUES (?, ?, ?, ?, ?)";
        }
        else if (customer.getUserID().contains("rc")) {
            insertQuery = "INSERT INTO registereduser (CustomerID, FirstName, LastName, Phone, Email, Username) VALUES (?, ?, ?, ?, ?, ?)";
        }
        else {
            return false;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(insertQuery);

            pstmt.setString(1, customer.getUserID());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getLastName());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getEmail());

            if (customer.getUserID().contains("rc")) {
                pstmt.setString(6, customer.getUserName());
            }

            int result = pstmt.executeUpdate();

            db_conn.close();

            if (result == 1){
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Customer getCustomerFromDatabaseByID(String userID) {
        String getQuery = "";
        if (userID.contains("gc")) {
            getQuery = "SELECT GuestID, FirstName, LastName, Phone, Email FROM guestuser WHERE GuestID = ?";
        }
        else if (userID.contains("rc")) {
            getQuery = "SELECT UserID, FirstName, LastName, Phone, Email, Username FROM registereduser WHERE UserID = ?";
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(getQuery);
            pstmt.setString(1, userID);

            ResultSet queryResult = pstmt.executeQuery();

            if (queryResult.next()) {
                String id = queryResult.getString(1);
                String firstName = queryResult.getString(2);
                String lastName = queryResult.getString(3);
                String phone = queryResult.getString(4);
                String email = queryResult.getString(5);

                if (id.contains("rc")) {
                    String usernameDB = queryResult.getString(6);
                    String passwordDB = "";

                    String content = "";

                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    InputStream is = classLoader.getResourceAsStream("/credentials.json");

                    Scanner reader = new Scanner(is, "UTF-8");
                    while (reader.hasNextLine()) {
                        content += reader.nextLine() + "\n";
                    }
                    reader.close();

                    if(!content.isEmpty()) {
                        JSONArray usersJsonArray = new JSONArray(content);
                        for (int i = 0; i < usersJsonArray.length(); i++) {
                            JSONObject userObj = usersJsonArray.getJSONObject(i);
                            String usernameInFile = userObj.getString("username");
                            if (usernameInFile.equals(usernameDB)) {
                                passwordDB = userObj.getString("password");
                                break;
                            }
                        }
                    }

                    if (passwordDB.isEmpty()) {
                        return null;
                    }
                    return Customer.loadRegisteredCustomer(id, usernameDB, passwordDB, firstName, lastName, phone, email);
                }
                else if (id.contains("gc")) {
                    return Customer.loadGuestCustomer(id, firstName, lastName, phone, email);
                }
            }
            else {
                return null;
            }
            queryResult.close();
            db_conn.close();

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Integer getMaxID(String customerType) {
        if(customerType == null) {
            return 0;
        }

        String getQuery = "";
        if (customerType.equals("guest")) {
            getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(GuestID, ' ', -1)), 3) FROM guestuser";
        }
        else if (customerType.equals("registered")) {
            getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(UserID, ' ', -1)), 3) FROM registereduser";
        }
        else {
            return 0;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            Statement stmt = db_conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(getQuery);

            if (queryResult.next()) {
                return Integer.parseInt(queryResult.getString(1));
            } else {
                return 0;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
