package com.taptapgo.repository;

import java.util.HashMap;
import java.util.Map;

import com.taptapgo.Customer;
import com.taptapgo.Order;

public class OrderIdentityMap {
    private HashMap<Integer, Order> orderMap;
    private static OrderIdentityMap instance = null;

    private OrderIdentityMap() {
        orderMap = new HashMap<Integer, Order>();
    }

    public static OrderIdentityMap getInstance() {
        if (instance == null) {
            instance = new OrderIdentityMap();
        }
        return instance;
    }

    public static synchronized boolean createOrder(Order order) {
        boolean dbResult = OrderRepository.createOrder(order);

        if (dbResult) {
            OrderIdentityMap.getInstance().orderMap.put(order.getOrderID(), order);
            return true;
        }

        return false;
    }

    public static synchronized int getMaxOrderID() {
        return OrderRepository.getMaxOrderID();
    }

    public static synchronized Order getOrderByID(int orderID) {
        Order orderMapResult = OrderIdentityMap.getInstance().orderMap.get(orderID);

        if(orderMapResult == null){
            return OrderRepository.readOrderByID(orderID);
        } else {
            return orderMapResult;
        }
    }

    public static synchronized HashMap<Integer, Order> getOrdersByCustomer(Customer customer) {
        HashMap<Integer, Order> orderMapResults = new HashMap<Integer, Order>();

        for (Map.Entry<Integer, Order> orderEntry : OrderIdentityMap.getInstance().orderMap.entrySet()) {
            Order order = orderEntry.getValue();

            if (order.getCustomer().equals(customer)) {
                orderMapResults.put(order.getOrderID(), order);
            }
        }

        HashMap<Integer, Order> orderRepoResults = OrderRepository.readOrderByCustomer(customer, orderMapResults);
        orderRepoResults.putAll(orderMapResults);

        return orderRepoResults;
    }
    
}
