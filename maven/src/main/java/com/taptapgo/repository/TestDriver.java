package com.taptapgo.repository;

import com.taptapgo.Order;
import com.taptapgo.Customer;


public class TestDriver {
    public static void main(String[] args) {
        Order testOrder = OrderIdentityMap.getOrderByID(1);

        System.out.println(testOrder.getBillAddress());

        Customer testCustomer = CustomerIdentityMap.getCustomerByID("rc00001");

        System.out.println(testCustomer.getUserName());
    }
}
