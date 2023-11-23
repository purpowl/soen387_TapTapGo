package com.taptapgo.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.taptapgo.Customer;
import com.taptapgo.Product;
import org.json.JSONArray;
import org.json.JSONObject;

public class CustomerIdentityMap {
    private HashMap<String, Customer> customerMap;
    private static CustomerIdentityMap instance = null;
    private static Connection db_conn;


    private CustomerIdentityMap() {
        customerMap = new HashMap<String, Customer>();
    }

    public static CustomerIdentityMap getInstance() {
        if (instance == null) {
            instance = new CustomerIdentityMap();
        }
        return instance;
    }

    public static boolean createCustomer(Customer customer) {
        boolean addToDBResult = createCustomerInDatabase(customer);

        if (addToDBResult) {
            CustomerIdentityMap.getInstance().customerMap.put(customer.getUserID(), customer);
            return true;
        }
        return false;
    }

    public static Customer getCustomerByID(String customerID) {
        Customer customerMapResult = CustomerIdentityMap.getInstance().customerMap.get(customerID);

        if (customerMapResult == null) {
            return getCustomerFromDatabaseByID(customerID);
        } else {
            return customerMapResult;
        }
    }

    public static Customer getCustomerbyUserName(String username) {
        Customer customerMapResult = null;
        for (Map.Entry<String, Customer> customer : CustomerIdentityMap.getInstance().customerMap.entrySet()) {
            if (customer.getValue().getUserName().equals(username))
                customerMapResult = customer.getValue();
        }

        if (customerMapResult == null) {
            return
                    getCustomerFromDatabaseByID(username);
        } else {
            return customerMapResult;
        }
    }

    public static boolean authenticateCustomer(String username, String password) {
        String content = "";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream("/credentials.json");

        assert is != null;
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
                if (usernameInFile.equals(username) && password.equals(userObj.getString("password"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean createCustomerInDatabase(Customer customer) {
        String insertQuery = "";
        if (customer.getUserID().contains("gc")) {
            insertQuery = "INSERT INTO guestcustomer (GCID, FirstName, LastName, Phone, Email) VALUES (?, ?, ?, ?, ?)";
        }
        else if (customer.getUserID().contains("rc")) {
            insertQuery = "INSERT INTO registeredcustomer (CustomerID, FirstName, LastName, Phone, Email, Username) VALUES (?, ?, ?, ?, ?, ?)";
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
            getQuery = "SELECT GCID, FirstName, LastName, Phone, Email FROM guestcustomer WHERE GCID = ?";
        }
        else if (userID.contains("rc")) {
            getQuery = "SELECT CustomerID, FirstName, LastName, Phone, Email, Username FROM registeredcustomer WHERE CustomerID = ?";
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

    public static Integer readMaxID(String customerType) {
        if(customerType == null) {
            return 0;
        }

        String getQuery = "";
        if (customerType.equals("guest")) {
            getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(GCID, ' ', -1)), 3) FROM guestcustomer";
        }
        else if (customerType.equals("registered")) {
            getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(CustomerID, ' ', -1)), 3) FROM registeredcustomer";
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
