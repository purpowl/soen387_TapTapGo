package com.taptapgo.repository;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import com.taptapgo.Customer;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;


public class CustomerRepository{
    private static Connection db_conn;

    public static boolean create(Customer customer) {
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

    public static Customer read(String userID) {

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
                    String usernameDB = "";
                    String passwordDB = "";
                    usernameDB = queryResult.getString(6);

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

    public static Customer readByUsername(String username) {

        String getQuery = "SELECT CustomerID, FirstName, LastName, Phone, Email, Username FROM registeredcustomer WHERE Username = ?";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(getQuery);
            pstmt.setString(1, username);

            ResultSet queryResult = pstmt.executeQuery();

            if (queryResult.next()) {
                String id = queryResult.getString(1);
                String firstName = queryResult.getString(2);
                String lastName = queryResult.getString(3);
                String phone = queryResult.getString(4);
                String email = queryResult.getString(5);

                    String passwordDB = "";
                    String content = "";

                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    InputStream is = classLoader.getResourceAsStream("/credentials.json");

                    assert is != null;
                    Scanner reader = new Scanner(is, "UTF-8");

                    //Scanner reader = new Scanner(new File("/credentials.json"));
                    while (reader.hasNextLine()) {
                        content += reader.nextLine() + "\n";
                    }
                    reader.close();

                    if(!content.isEmpty()) {
                        JSONArray usersJsonArray = new JSONArray(content);
                        for (int i = 0; i < usersJsonArray.length(); i++) {
                            JSONObject userObj = usersJsonArray.getJSONObject(i);
                            String usernameInFile = userObj.getString("username");
                            if (usernameInFile.equals(username)) {
                                passwordDB = userObj.getString("password");
                                break;
                            }
                        }
                    }

                queryResult.close();
                db_conn.close();

                    if (passwordDB.isEmpty()) {
                        return null;
                    }
                    return Customer.loadRegisteredCustomer(id, username, passwordDB, firstName, lastName, phone, email);
            }
            else {
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean update(Object object) {
        return true;
    }

    
    public boolean delete(Object object) {
        return true;
    }

    public static Integer readMaxID(String customerType) {
        if(!(customerType instanceof String)) {
            return null;
        }

        String getQuery = "";
        if (customerType.equals("guest")) {
            getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(GCID, ' ', -1)), 3) FROM guestcustomer";
        }
        else if (customerType.equals("registered")) {
            getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(CustomerID, ' ', -1)), 3) FROM registeredcustomer";
        }
        else if (customerType.equals("staff")) {
            getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(StaffID, ' ', -1)), 3) FROM staff";
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
