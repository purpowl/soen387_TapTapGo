package com.taptapgo.repository;

import java.util.HashMap;

import com.taptapgo.Customer;

public class CustomerIdentityMap {
    private HashMap<String, Customer> customerMap;
    private static CustomerIdentityMap instance = null;

    private CustomerIdentityMap() {
        customerMap = new HashMap<String, Customer>();
    }

    public static CustomerIdentityMap getInstance() {
        if (instance == null) {
            instance = new CustomerIdentityMap();
        }
        return instance;
    }

    public static boolean createCustomer(Customer customer) {
        boolean addToDBResult = CustomerRepository.create(customer);

        if (addToDBResult) {
            CustomerIdentityMap.getInstance().customerMap.put(customer.getUserID(), customer);
            return true;
        }
        return false;
    }

    public static Customer getCustomerByID(String customerID) {
        Customer customerMapResult = CustomerIdentityMap.getInstance().customerMap.get(customerID);

        if (customerMapResult == null) {
            return CustomerRepository.read(customerID);
        } else {
            return customerMapResult;
        }
    }

    public static Integer getMaxID(String customerType){
        return CustomerRepository.readMaxID(customerType);
    }
}
