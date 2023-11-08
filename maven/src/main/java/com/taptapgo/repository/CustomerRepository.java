package com.taptapgo.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import com.taptapgo.Customer;

public class CustomerRepository{
    private static Connection db_conn;

    public static boolean create(Customer customer) {
        String insertQuery = "";
        if (((Customer) customer).getUserID().contains("gc")) {
            insertQuery = "INSERT INTO guestcustomer (GCID, FirstName, LastName, Phone, Email) VALUES (?, ?, ?, ?, ?)";
        }
        else if (((Customer) customer).getUserID().contains("rc")) {
            insertQuery = "INSERT INTO registeredcustomer (CustomerID, FirstName, LastName, Phone, Email) VALUES (?, ?, ?, ?, ?)";
        }
        else if (((Customer) customer).getUserID().contains("s")) {
            insertQuery = "INSERT INTO staff (StaffID) VALUES (?)";
        }
        else {
            return false;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(insertQuery);

            if (((Customer) customer).getUserID().contains("gc") || ((Customer) customer).getUserID().contains("rc")) {
                pstmt.setString(1, customer.getUserID());
                pstmt.setString(2, customer.getFirstName());
                pstmt.setString(3, customer.getLastName());
                pstmt.setString(4, customer.getPhone());
                pstmt.setString(5, customer.getEmail());
            }
            else if (((Customer) customer).getUserID().contains("s")) {
                pstmt.setString(1, customer.getUserID());
            }
            else {
                return false;
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

        if (((String) userID).contains("gc")) {
            getQuery = "SELECT FirstName, LastName, Phone, Email FROM guestcustomer WHERE GCID = ?";
        }
        else if (((String) userID).contains("rc")) {
            getQuery = "SELECT FirstName, LastName, Phone, Email FROM registeredcustomer WHERE CustomerID = ?";
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(getQuery);
            pstmt.setString(1, userID);

            ResultSet queryResult = pstmt.executeQuery();

            if (queryResult.next()) {

            } else {
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public boolean update(Object object) {
        return true;
    }

    
    public boolean delete(Object object) {
        return true;
    }
}
