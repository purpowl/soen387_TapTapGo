package com.taptapgo.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
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
        String insertOrderQuery = "INSERT INTO `order` (OrderID, OrderPayDate, TotalAmt, PayMethod, 4CreditDigits, BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, CustomerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertOrderItemQuery = "INSERT INTO orderitem (OrderID, ProductSKU, Quantity) VALUES (?, ?, ?)";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("com.mysql.jdbc.Driver");
            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");
            
            // Save checkpoint to rollback in case insert fail
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


    /**
     * Get an order given the order ID
     * @param orderID the order ID that is being searched for
     * @return An Order object, this will be null if no order is found with the corresponding ID
     */
    public static Order readOrderByID(int orderID) {
        String getOrderQuery = "SELECT OrderPayDate, TotalAmt, PayMethod, 4CreditDigits, BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, TrackingNumber, ShipDate, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, CustomerID, GCID FROM `order` WHERE OrderID = ?";
        String getOrderItemsQuery = "SELECT ProductSKU, Quantity FROM orderitem WHERE OrderID = ?";
        
        try {
            // Open database connection
            Class.forName("com.mysql.jdbc.Driver");
            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            // Get an order that has this orderID
            PreparedStatement pstmt = db_conn.prepareStatement(getOrderQuery);
            pstmt.setInt(1, orderID);
            ResultSet queryResult = pstmt.executeQuery();

            if (queryResult.next()) {
                // If an order is found, extract all order information
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
                queryResult.close();

                if (customerID != null) {
                    customer = CustomerIdentityMap.getCustomerByID(customerID);
                } else {
                    customer = CustomerIdentityMap.getCustomerByID(guestCustomerID);
                }


                // Extract all items that are in this order
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
                queryResult.close();

                db_conn.close();

                return Order.loadOrder(orderID, totalAmount, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNum, shipDate, customer, orderItems);
            } else {
                // If order is not found, return null
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Get all matching orders from the database given a customer object
     * @param customer the customer that we are search orders for
     * @param ordersLoaded A Hashmap of all orders that are already loaded in memory, so we avoid reloading these orders
     * @return A Hashmap mapping orderID to an Order object. This hashmap will be empty if no order is found.
     */
    public static HashMap<Integer, Order> readOrderByCustomer(Customer customer, HashMap<Integer, Order> ordersLoaded) {
        String getOrderQuery = "SELECT OrderID, OrderPayDate, TotalAmt, PayMethod, 4CreditDigits, BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, TrackingNumber, ShipDate, ShipAddress, ShipCity, ShipCountry, ShipPostalCode FROM `order` WHERE CustomerID = ?";
        String getOrderItemsQuery = "SELECT ProductSKU, Quantity FROM orderitem WHERE OrderID = ?";
        HashMap<Integer, Order> orderResults = new HashMap<Integer, Order>();

        try {
            // Open database connection
            Class.forName("com.mysql.jdbc.Driver");
            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            // Query the database to get all orders of this customer
            PreparedStatement pstmt = db_conn.prepareStatement(getOrderQuery);
            pstmt.setString(1, customer.getUserID());
            ResultSet queryResult = pstmt.executeQuery();

            while (queryResult.next()) {
                // Extract all order information
                int orderID = queryResult.getInt(1);
                // If order is already loaded, skip
                if(ordersLoaded.get(orderID) != null) {
                    continue;
                }
                Date payDate = queryResult.getDate(2);
                float totalAmount = queryResult.getFloat(3);
                String payMethod = queryResult.getString(4);
                int cardNum = queryResult.getInt(5);
                String billAddress = queryResult.getString(6);
                String billCity = queryResult.getString(7);
                String billCountry = queryResult.getString(8);
                String billPostalCode = queryResult.getString(9);
                String shipStatus = queryResult.getString(10);
                String trackingNum = queryResult.getString(11);
                Date shipDate = queryResult.getDate(12);
                String shipAddress = queryResult.getString(13);
                String shipCity = queryResult.getString(14);
                String shipCountry = queryResult.getString(15);
                String shipPostalCode = queryResult.getString(16);
                queryResult.close();


                // Extract all order items within this order
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
                queryResult.close();

                
                // Create an order and load to result hashmap
                Order newOrder = Order.loadOrder(orderID, totalAmount, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNum, shipDate, customer, orderItems);
                orderResults.put(newOrder.getOrderID(), newOrder);

            }

            db_conn.close();

            return orderResults;

        } catch(Exception e) {
            e.printStackTrace();
            return orderResults;
        }
    }


    /**
     * Get the maximum order ID from the order
     * This function is called at server start-up to update the order counter
     * @return the maximum order ID currently present in database
     */
    public static int getMaxOrderID() {
        String getMaxOrderIDQuery = "SELECT MAX(OrderID) FROM `order`";
        int maxOrderID = 0;

        try {
            // Open database connection
            Class.forName("com.mysql.jdbc.Driver");
            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            // Query the database to get the maximum orderID
            Statement stmt = db_conn.createStatement();
            ResultSet result = stmt.executeQuery(getMaxOrderIDQuery);

            if (result.next()) {
                maxOrderID = result.getInt(1);
            }

            stmt.close();
            db_conn.close();

            return maxOrderID;

        } catch (Exception e) {
            e.printStackTrace();
            return maxOrderID;
        }
    }

    /**
     * Load all orders from database into memory.
     * This also means loading all customers from database into memory.
     * 
     * @param ordersLoaded A Hashmap of already loaded order to avoid loading an object twice
     * @return A Hashmap mapping orderID to an order object.
     */
    public static HashMap<Integer, Order> loadAllOrders(HashMap<Integer, Order> ordersLoaded){
        String getOrderQuery = "SELECT OrderID, OrderPayDate, TotalAmt, PayMethod, 4CreditDigits, BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, TrackingNumber, ShipDate, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, GCID, CustomerID FROM `order`";
        String getOrderItemsQuery = "SELECT ProductSKU, Quantity FROM orderitem WHERE OrderID = ?";
        HashMap<Integer, Order> orderResults = new HashMap<Integer, Order>();

        try {
            // Open database connection
            Class.forName("com.mysql.jdbc.Driver");
            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            // Query the database to get all orders of this customer
            PreparedStatement pstmt = db_conn.prepareStatement(getOrderQuery);
            ResultSet queryResult = pstmt.executeQuery();

            while (queryResult.next()) {
                // Extract all order information
                int orderID = queryResult.getInt(1);
                // If order is already loaded, skip
                if(ordersLoaded.get(orderID) != null) {
                    continue;
                }
                Date payDate = queryResult.getDate(2);
                float totalAmount = queryResult.getFloat(3);
                String payMethod = queryResult.getString(4);
                int cardNum = queryResult.getInt(5);
                String billAddress = queryResult.getString(6);
                String billCity = queryResult.getString(7);
                String billCountry = queryResult.getString(8);
                String billPostalCode = queryResult.getString(9);
                String shipStatus = queryResult.getString(10);
                String trackingNum = queryResult.getString(11);
                Date shipDate = queryResult.getDate(12);
                String shipAddress = queryResult.getString(13);
                String shipCity = queryResult.getString(14);
                String shipCountry = queryResult.getString(15);
                String shipPostalCode = queryResult.getString(16);
                String guestCustomerID = queryResult.getString(17);
                String registeredCustomerID = queryResult.getString(18);
                Customer customer = null;
                queryResult.close();

                // Load customer object to memory
                if (registeredCustomerID != null) {
                    customer = CustomerIdentityMap.getCustomerByID(registeredCustomerID);
                } else {
                    customer = CustomerIdentityMap.getCustomerByID(guestCustomerID);
                }


                // Load all order items of this order to memory
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
                queryResult.close();

                
                // Create an order and load to result hashmap
                Order newOrder = Order.loadOrder(orderID, totalAmount, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNum, shipDate, customer, orderItems);
                orderResults.put(newOrder.getOrderID(), newOrder);

            }

            db_conn.close();

            return orderResults;

        } catch(Exception e) {
            e.printStackTrace();
            return orderResults;
        }
    }


    /**
     * Update an order in database with tracking number and shipping date
     * 
     * @param orderID
     * @param tracking
     * @param shipDate
     * @return true on success, false on failure
     */
    public static boolean shipOrder(int orderID, String tracking, Date shipDate) {
        String modifyOrderQuery = "UPDATE `order` SET TrackingNumber = ?, ShipDate = ? WHERE OrderID = ?";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("com.mysql.jdbc.Driver");
            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");
            
            // Save checkpoint to rollback in case update fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt = db_conn.prepareStatement(modifyOrderQuery);
            pstmt.setString(1, tracking);
            pstmt.setDate(2, shipDate);
            pstmt.setInt(3, orderID);

            pstmt.executeUpdate();
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
