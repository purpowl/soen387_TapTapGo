package com.taptapgo.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.Date;

import com.taptapgo.Customer;
import com.taptapgo.Order;
import com.taptapgo.Product;
import com.taptapgo.Warehouse;

public class OrderRepository{
    private static Connection db_conn;

    public static boolean createOrder(Order order) {
        String insertOrderQuery = "INSERT INTO order (OrderID, OrderPayDate, TotalAmt, PayMethod, 4CreditDigits, BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, CustomerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertOrderItemQuery = "INSERT INTO orderitem (OrderID, ProductSKU, Quantity) VALUES (?, ?, ?)";
        Savepoint savepoint = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt1 = db_conn.prepareStatement(insertOrderQuery);

            pstmt1.setInt(1, order.getOrderID());
            pstmt1.setDate(2, new Date(order.getPayDate().getTime()));
            pstmt1.setFloat(3, order.getTotalPrice());
            pstmt1.setString(4, order.getPaymentMethod());
            pstmt1.setInt(5, order.getCardNum());
            pstmt1.setString(6, order.getBillAddress());
            pstmt1.setString(7, order.getBillCity());
            pstmt1.setString(8, order.getBillCountry());
            pstmt1.setString(9, order.getBillPostalCode());
            pstmt1.setString(10, order.shipStatusToString());
            pstmt1.setString(11, order.getShippingAddress());
            pstmt1.setString(12, order.getShippingCity());
            pstmt1.setString(13, order.getShippingCountry());
            pstmt1.setString(14, order.getShippingPostalCode());
            pstmt1.setString(15, order.getCustomer().getUserID());

            pstmt1.executeUpdate();

            for (Map.Entry<Product, Integer> productEntry : order.getOrderProducts().entrySet()) {
                Product product = productEntry.getKey();
                int amount = productEntry.getValue();

                PreparedStatement pstmt2 = db_conn.prepareStatement(insertOrderItemQuery);

                pstmt2.setInt(1, order.getOrderID());
                pstmt2.setString(2, product.getSKU());
                pstmt2.setInt(3, amount);

                pstmt2.executeUpdate();

            }

            db_conn.commit();

            db_conn.setAutoCommit(true);
            db_conn.close();

            return true;
            
        } catch(SQLException e) {
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

    public static Order readOrderID(int orderID) {
        String getOrderQuery = "SELECT OrderPayDate, TotalAmt, PayMethod, 4CreditDigits, BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, TrackingNumber, ShipDate, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, CustomerID, GCID FROM order WHERE OrderID = ?";
        String getOrderItemsQuery = "SELECT ProductSKU, Quantity FROM order WHERE OrderID = ?";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(getOrderQuery);
            pstmt.setInt(1, orderID);

            ResultSet queryResult = pstmt.executeQuery();

            if (queryResult.next()) {
                Date payDate = queryResult.getDate(1);
                float totalAmount = queryResult.getFloat(2);
                String payMethod = queryResult.getString(3);
                int cardNum = queryResult.getInt(4);
                String billAddress = queryResult.getString(5);
                String billCity = queryResult.getString(6);
                String billCountry = queryResult.getString(7);
                String billPostalCode = queryResult.getString(8);
                String shipStatus = queryResult.getString(9);
                String trackingNum = queryResult.getString(10);
                Date shipDate = queryResult.getDate(11);
                String shipAddress = queryResult.getString(12);
                String shipCity = queryResult.getString(13);
                String shipCountry = queryResult.getString(14);
                String shipPostalCode = queryResult.getString(15);
                String customerID = queryResult.getString(16);
                String guestCustomerID = queryResult.getString(17);
                Customer customer = null;

                if (customerID != null) {
                    customer = CustomerIdentityMap.getRegisteredCustomer(customerID);
                } else {
                    customer = CustomerIdentityMap.getGuestCustomer(guestCustomerID);
                }

                HashMap<Product, Integer> orderItems = new HashMap<Product, Integer>();
                PreparedStatement pstmt2 = db_conn.prepareStatement(getOrderItemsQuery);
                pstmt2.setInt(1, orderID);
                queryResult = pstmt2.executeQuery();

                while (queryResult.next()) {
                    String productSKU = queryResult.getString(1);
                    int quantity = queryResult.getInt(2);
                    Product product = Warehouse.getInstance().findProductBySKU(productSKU);
                    orderItems.put(product, quantity);
                }

                return Order.loadOrder(orderID, totalAmount, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNum, shipDate, customer, orderItems);
            } else {
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
}
