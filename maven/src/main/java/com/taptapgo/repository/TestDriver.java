package com.taptapgo.repository;

import com.taptapgo.Order;

public class TestDriver {
    public static void main(String[] args) {
        Order testOrder = OrderIdentityMap.getOrderByID(1);

        System.out.println(testOrder.getBillAddress());
    }
}
