package com.taptapgo.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Date;

import com.taptapgo.Customer;

public class CustomerRepository implements Repository{
    private static Connection db_conn;

    @Override
    public boolean create(Object customer) {
        if(!(customer instanceof Customer)){
            return false;
        }

        Customer customerToCreate = (Customer) customer;

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
                pstmt.setString(1, customerToCreate.getUserID());
                pstmt.setString(2, customerToCreate.getFirstName());
                pstmt.setString(3, customerToCreate.getLastName());
                pstmt.setString(4, customerToCreate.getPhone());
                pstmt.setString(5, customerToCreate.getEmail());
            }
            else if (((Customer) customer).getUserID().contains("s")) {
                pstmt.setString(1, customerToCreate.getUserID());
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

    @Override
    public Object read(Object userID) {
        if(!(userID instanceof String)) {
            return null;
        }

        String userIDToRetrieve = (String) userID;

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
            pstmt.setString(1, userIDToRetrieve);

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

    @Override
    public boolean update(Object object) {
        return true;
    }

    @Override
    public boolean delete(Object object) {
        return true;
    }
}
