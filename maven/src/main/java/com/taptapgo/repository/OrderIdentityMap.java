package com.taptapgo.repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.taptapgo.Order;
import com.taptapgo.User;
import com.taptapgo.exceptions.InvalidParameterException;

public class OrderIdentityMap {
    private HashMap<Integer, Order> orderMap;
    private static OrderIdentityMap instance = null;

    private OrderIdentityMap() {
        orderMap = new HashMap<>();
    }


    /**
     * Get an instance of OrderIdentityMap
     * @return OrderIdentityMap object
     */
    public static OrderIdentityMap getInstance() {
        if (instance == null) {
            instance = new OrderIdentityMap();
        }
        return instance;
    }


    /**
     * Create an order and store it in the database
     * 
     * @param order The order to be created
     * @return True on success, false on failure
     */
    public static synchronized boolean createOrder(Order order) {
        boolean dbResult = OrderRepository.createOrder(order);

        if (dbResult) {
            OrderIdentityMap.getInstance().orderMap.put(order.getOrderID(), order);
            return true;
        }

        return false;
    }


    /**
     * Get the maximum orderID from database.
     * This function is called when the server loads
     * 
     * @return Maximum orderID present in database
     */
    public static synchronized int getMaxOrderID() {
        return OrderRepository.getMaxOrderID();
    }


    /**
     * Get an order by ID
     * 
     * @param orderID The ID of the order
     * @return An order object with specified ID. Returns null if order is not found.
     */
    public static synchronized Order getOrderByID(int orderID) {
        Order orderMapResult = OrderIdentityMap.getInstance().orderMap.get(orderID);

        if(orderMapResult == null){
            return OrderRepository.readOrderByID(orderID);
        } else {
            return orderMapResult;
        }
    }


    /**
     * Get all orders of a specific customer
     * 
     * @param user The registered user to get orders for
     * @return A Hashmap mapping each orderID to an order object
     */
    public static synchronized HashMap<Integer, Order> getOrdersByUser(User user) throws InvalidParameterException {
        HashMap<Integer, Order> orderMapResults = new HashMap<>();

        for (Map.Entry<Integer, Order> orderEntry : OrderIdentityMap.getInstance().orderMap.entrySet()) {
            Order order = orderEntry.getValue();

            if (order.getCustomerID().equals(user.getUserID())) {
                orderMapResults.put(order.getOrderID(), order);
            }
        }

        HashMap<Integer, Order> orderRepoResults = OrderRepository.readOrderByUser(user, orderMapResults);
        orderMapResults.putAll(orderRepoResults);
        OrderIdentityMap.getInstance().orderMap.putAll(orderRepoResults);

        return orderMapResults;
    }


    /**
     * Load all orders from database into memory.
     * This also means loading all customers from database into memory
     * 
     * @return A Hashmap mapping each orderID to an order object
     */
    public static synchronized HashMap<Integer, Order> loadAllOrders() {
        HashMap<Integer, Order> ordersFromDB = OrderRepository.loadAllOrders(OrderIdentityMap.getInstance().orderMap);

        // Add orders from database to order map
        OrderIdentityMap.getInstance().orderMap.putAll(ordersFromDB);

        return instance.orderMap;
    }

    
    /**
     * Update an order with tracking information and shipping date
     * This function assumes that the order is already in memory (in orderMap).
     * 
     * @param order The order to be updated
     * @param tracking Tracking number
     * @param shipDate Shipping date
     * @return True on success, false on failure.
     */
    public static synchronized boolean shipOrder(Order order, String tracking, String shippingStatus, Date shipDate){
        boolean db_result = OrderRepository.shipOrder(order.getOrderID(), tracking, shipDate, shippingStatus);

        if (db_result) {
            order.setTrackingNumber(tracking);
            order.setShipDate(shipDate);
            order.setShippingStatus(shippingStatus);

            return true;
        }

        return false;
    }
    
}
