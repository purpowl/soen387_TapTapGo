package com.taptapgo.repository;

import java.util.HashMap;

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
        instance.orderMap.put(order.getOrderID(), order);

        return OrderRepository.createOrder(order);
    }
    
}
