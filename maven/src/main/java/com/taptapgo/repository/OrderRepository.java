package com.taptapgo.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Date;

import com.taptapgo.Order;

public class OrderRepository implements Repository{
    private static Connection db_conn;

    @Override
    public boolean create(Object order) {
        if(!(order instanceof Order)){
            return false;
        }

        Order orderToCreate = (Order) order;

        String insertQuery = "INSERT INTO orderpayment (OrderID, OrderPayDate, TotalAmt, PayMethod, 4CreditDigits, ShippingAddressNo, ShippingStreet, ShippingCity, ShippingCountry, ShippingPostalCode, CustomerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(insertQuery);

            pstmt.setInt(1, orderToCreate.getOrderID());
            pstmt.setDate(2, new Date(orderToCreate.getPayDate().getTime()));
            pstmt.setFloat(3, orderToCreate.getOrderTotal());
            pstmt.setString(4, orderToCreate.getPaymentMethod());
            pstmt.setInt(5, orderToCreate.getCardNum());
            pstmt.setInt(6, orderToCreate.getShippingAddressNum());
            pstmt.setString(7, orderToCreate.getShippingStreet());
            pstmt.setString(8, orderToCreate.getShippingCity());
            pstmt.setString(9, orderToCreate.getShippingCountry());
            pstmt.setString(10, orderToCreate.getShippingPostalCode());
            pstmt.setString(11, orderToCreate.getCustomer().getUserID());

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
    public Object read(Object orderID) {
        if(!(orderID instanceof Integer)) {
            return null;
        }

        Integer orderIDToRetrieve = (Integer) orderID;

        String getQuery = "SELECT OrderPayDate, TotalAmt, PayMethod, 4CreditDigits, ShippingAddressNo, ShippingStreet, ShippingCity, ShippingCountry, ShippingPostalCode, TrackingNo, CustomerID, SessionID FROM orderpayment WHERE OrderID = ?";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(getQuery);
            pstmt.setInt(1, orderIDToRetrieve);

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
