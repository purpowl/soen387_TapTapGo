package com.taptapgo.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


import java.net.URL;
import java.sql.Connection;
import java.sql.Date;

import com.taptapgo.*;
import com.taptapgo.exceptions.InvalidParameterException;

public class OrderRepository{
    private static Connection db_conn;

    public static boolean createOrder(Order order) {
        String insertOrderRegisteredQuery = "INSERT INTO `order` (OrderID, OrderPayDate, TotalAmt, PayMethod, \"4CreditDigits\", BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, UserID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertOrderGuestQuery = "INSERT INTO `order` (OrderID, OrderPayDate, TotalAmt, PayMethod, \"4CreditDigits\", BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, GuestID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertOrderItemQuery = "INSERT INTO orderitem (OrderID, ProductSKU, Name, Description, Price, Vendor, Slug, ImagePath, Quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Save checkpoint to rollback in case insert fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt1;

            if (UserIdentityMap.getUserByID(order.getCustomerID()).isRegisteredUser()) {
                pstmt1 = db_conn.prepareStatement(insertOrderRegisteredQuery);
            } else {
                pstmt1 = db_conn.prepareStatement(insertOrderGuestQuery);
            }

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String payDate = dateFormatter.format(order.getPayDate());

            pstmt1.setInt(1, order.getOrderID());
            pstmt1.setString(2, payDate);
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
            pstmt1.setString(15, order.getCustomerID());

            pstmt1.executeUpdate();

            for (Map.Entry<Product, Integer> productEntry : order.getOrderProducts().entrySet()) {
                Product product = productEntry.getKey();
                int amount = productEntry.getValue();

                PreparedStatement pstmt2 = db_conn.prepareStatement(insertOrderItemQuery);
                if (!Warehouse.getInstance().removeProduct(product, amount, db_conn)) {
                    throw new SQLException("Failed to remove product from Warehouse");
                }

                pstmt2.setInt(1, order.getOrderID());
                pstmt2.setString(2, product.getSKU());
                pstmt2.setString(3, product.getName());
                pstmt2.setString(4, product.getDescription());
                pstmt2.setFloat(5, product.getPrice());
                pstmt2.setString(6, product.getVendor());
                pstmt2.setString(7, product.getSlug());
                pstmt2.setNull(8, java.sql.Types.VARCHAR);
                pstmt2.setInt(9, amount);

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
        } catch (ClassNotFoundException | InvalidParameterException e) {
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
        String getOrderQuery = "SELECT OrderPayDate, TotalAmt, PayMethod, \"4CreditDigits\", BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, TrackingNumber, ShipDate, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, UserID, GuestID FROM `order` WHERE OrderID = ?";
        String getOrderItemsQuery = "SELECT ProductSKU, Name, Description, Price, Vendor, Slug, Quantity FROM orderitem WHERE OrderID = ?";
        
        try {
            // Open database connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Get an order that has this orderID
            PreparedStatement pstmt = db_conn.prepareStatement(getOrderQuery);
            pstmt.setInt(1, orderID);
            ResultSet queryResult = pstmt.executeQuery();

            if (queryResult.next()) {
                // If an order is found, extract all order information
                Date payDate = Date.valueOf(queryResult.getString(1));
                float totalAmount = queryResult.getFloat(2);
                String payMethod = queryResult.getString(3);
                int cardNum = queryResult.getInt(4);
                String billAddress = queryResult.getString(5);
                String billCity = queryResult.getString(6);
                String billCountry = queryResult.getString(7);
                String billPostalCode = queryResult.getString(8);
                String shipStatus = queryResult.getString(9);
                String trackingNum = queryResult.getString(10);
                String shipDate_string = queryResult.getString(11);
                Date shipDate = null;
                if (shipDate_string != null) {
                    shipDate = Date.valueOf(shipDate_string);
                }
                String shipAddress = queryResult.getString(12);
                String shipCity = queryResult.getString(13);
                String shipCountry = queryResult.getString(14);
                String shipPostalCode = queryResult.getString(15);
                String customerID = queryResult.getString(16);
                String guestCustomerID = queryResult.getString(17);
                queryResult.close();

                // Extract all items that are in this order
                HashMap<Product, Integer> orderItems = new HashMap<>();
                PreparedStatement pstmt2 = db_conn.prepareStatement(getOrderItemsQuery);
                pstmt2.setInt(1, orderID);
                queryResult = pstmt2.executeQuery();

                while (queryResult.next()) {
                    String productSKU = queryResult.getString(1);
                    String productName = queryResult.getString(2);
                    String productDesc = queryResult.getString(3);
                    Float productPrice = queryResult.getFloat(4);
                    String productVendor = queryResult.getString(5);
                    String slug = queryResult.getString(6);
                    int quantity = queryResult.getInt(7);
                    Product product = new Product(productSKU, productName, productDesc, productVendor, slug, productPrice);
                    orderItems.put(product, quantity);
                }
                queryResult.close();

                db_conn.close();

                return Order.loadOrder(orderID, totalAmount, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNum, shipDate, orderItems, (customerID != null) ? customerID : guestCustomerID);
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
     * @param user the user that we are searching orders for
     * @param ordersLoaded A Hashmap of all orders that are already loaded in memory, so we avoid reloading these orders
     * @return A Hashmap mapping orderID to an Order object. This hashmap will be empty if no order is found.
     */
    public static HashMap<Integer, Order> readOrderByUser(User user, HashMap<Integer, Order> ordersLoaded) throws InvalidParameterException {
        // reading orders can only be done by registered users
        if (!user.isRegisteredUser())
            throw new InvalidParameterException("Cannot get order history for guest users!");

        String getOrderQuery = "SELECT OrderID, OrderPayDate, TotalAmt, PayMethod, \"4CreditDigits\", BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, TrackingNumber, ShipDate, ShipAddress, ShipCity, ShipCountry, ShipPostalCode FROM `order` WHERE UserID = ?";
        String getOrderItemsQuery = "SELECT ProductSKU, Name, Description, Price, Vendor, Slug, Quantity FROM orderitem WHERE OrderID = ?";
        HashMap<Integer, Order> orderResults = new HashMap<>();

        try {
            // Open database connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Query the database to get all orders of this customer
            PreparedStatement pstmt = db_conn.prepareStatement(getOrderQuery);
            pstmt.setString(1, user.getUserID());
            ResultSet queryResult = pstmt.executeQuery();

            while (queryResult.next()) {
                // Extract all order information
                int orderID = queryResult.getInt(1);
                // If order is already loaded, skip
                if(ordersLoaded.get(orderID) != null) {
                    continue;
                }
                Date payDate = Date.valueOf(queryResult.getString(2));
                float totalAmount = queryResult.getFloat(3);
                String payMethod = queryResult.getString(4);
                int cardNum = queryResult.getInt(5);
                String billAddress = queryResult.getString(6);
                String billCity = queryResult.getString(7);
                String billCountry = queryResult.getString(8);
                String billPostalCode = queryResult.getString(9);
                String shipStatus = queryResult.getString(10);
                String trackingNum = queryResult.getString(11);
                String shipDate_string = queryResult.getString(12);
                Date shipDate = null;
                if (shipDate_string != null) {
                    shipDate = Date.valueOf(shipDate_string);
                }
                String shipAddress = queryResult.getString(13);
                String shipCity = queryResult.getString(14);
                String shipCountry = queryResult.getString(15);
                String shipPostalCode = queryResult.getString(16);

                // Extract all order items within this order
                HashMap<Product, Integer> orderItems = new HashMap<>();
                PreparedStatement pstmt2 = db_conn.prepareStatement(getOrderItemsQuery);
                pstmt2.setInt(1, orderID);
                ResultSet queryResult2 = pstmt2.executeQuery();

                while (queryResult2.next()) {
                    String productSKU = queryResult2.getString(1);
                    String productName = queryResult2.getString(2);
                    String productDesc = queryResult2.getString(3);
                    Float productPrice = queryResult2.getFloat(4);
                    String productVendor = queryResult2.getString(5);
                    String slug = queryResult2.getString(6);
                    int quantity = queryResult2.getInt(7);
                    Product product = new Product(productSKU, productName, productDesc, productVendor, slug, productPrice);
                    orderItems.put(product, quantity);
                }

                // Create an order and load to result hashmap
                Order newOrder = Order.loadOrder(orderID, totalAmount, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNum, shipDate, orderItems, user.getUserID());
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
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

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
        String getOrderQuery = "SELECT OrderID, OrderPayDate, TotalAmt, PayMethod, \"4CreditDigits\", BillAddress, BillCity, BillCountry, BillPostalCode, ShippingStatus, TrackingNumber, ShipDate, ShipAddress, ShipCity, ShipCountry, ShipPostalCode, GuestID, UserID FROM `order`";
        String getOrderItemsQuery = "SELECT ProductSKU, Name, Description, Price, Vendor, Slug, Quantity FROM orderitem WHERE OrderID = ?";
        HashMap<Integer, Order> orderResults = new HashMap<>();

        try {
            // Open database connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

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
                Date payDate = Date.valueOf(queryResult.getString(2));
                float totalAmount = queryResult.getFloat(3);
                String payMethod = queryResult.getString(4);
                int cardNum = queryResult.getInt(5);
                String billAddress = queryResult.getString(6);
                String billCity = queryResult.getString(7);
                String billCountry = queryResult.getString(8);
                String billPostalCode = queryResult.getString(9);
                String shipStatus = queryResult.getString(10);
                String trackingNum = queryResult.getString(11);
                String shipDate_string = queryResult.getString(12);
                Date shipDate = null;
                if (shipDate_string != null) {
                    shipDate = Date.valueOf(shipDate_string);
                }
                String shipAddress = queryResult.getString(13);
                String shipCity = queryResult.getString(14);
                String shipCountry = queryResult.getString(15);
                String shipPostalCode = queryResult.getString(16);
                String guestCustomerID = queryResult.getString(17);
                String customerID = queryResult.getString(18);

                // Load all order items of this order to memory
                HashMap<Product, Integer> orderItems = new HashMap<>();
                PreparedStatement pstmt2 = db_conn.prepareStatement(getOrderItemsQuery);
                pstmt2.setInt(1, orderID);
                ResultSet queryResult2 = pstmt2.executeQuery();

                while (queryResult2.next()) {
                    String productSKU = queryResult2.getString(1);
                    String productName = queryResult2.getString(2);
                    String productDesc = queryResult2.getString(3);
                    Float productPrice = queryResult2.getFloat(4);
                    String productVendor = queryResult2.getString(5);
                    String slug = queryResult2.getString(6);
                    int quantity = queryResult2.getInt(7);
                    Product product = new Product(productSKU, productName, productDesc, productVendor, slug, productPrice);
                    orderItems.put(product, quantity);
                }

                
                // Create an order and load to result hashmap
                Order newOrder = Order.loadOrder(orderID, totalAmount, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNum, shipDate, orderItems, (customerID != null) ? customerID : guestCustomerID);
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
    public static boolean shipOrder(int orderID, String tracking, Date shipDate, String shipStatus) {
        String modifyOrderQuery = "UPDATE `order` SET TrackingNumber = ?, ShipDate = ?, ShippingStatus = ? WHERE OrderID = ?";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Save checkpoint to rollback in case update fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt = db_conn.prepareStatement(modifyOrderQuery);
            pstmt.setString(1, tracking);
            pstmt.setDate(2, shipDate);
            pstmt.setString(3, shipStatus);
            pstmt.setInt(4, orderID);

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

    /**
     * Set an order userID to a registered userID, in case a registered customer wants to claim their order.
     * 
     * @param orderID The ID of the order to be reclaimed
     * @param customerID The ID of the registered user to be put on the order
     * @return true on success, false on failure.
     */
    public static synchronized boolean setOrderCustomerID(int orderID, String customerID) {
        String updateOrderQuery = "UPDATE `order` SET UserID = ?,GuestID = ? WHERE OrderID = ?";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Save checkpoint to rollback in case update fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt = db_conn.prepareStatement(updateOrderQuery);
            pstmt.setString(1, customerID);
            pstmt.setNull(2, java.sql.Types.VARCHAR);
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
