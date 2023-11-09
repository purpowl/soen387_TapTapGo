package com.taptapgo.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.taptapgo.Customer;
import com.taptapgo.Product;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public static Customer getCustomerbyUserName(String username) {
        Customer customerMapResult = null;
        for (Map.Entry<String, Customer> customer : CustomerIdentityMap.getInstance().customerMap.entrySet()) {
            if (customer.getValue().getUserName().equals(username))
                customerMapResult = customer.getValue();
        }

        if (customerMapResult == null) {
            return CustomerRepository.readByUsername(username);
        } else {
            return customerMapResult;
        }
    }

    public static boolean authenticateCustomer(String username, String password) throws FileNotFoundException {
        String content = "";
        Scanner reader = new Scanner(new File("/credentials.json"));
        while (reader.hasNextLine()) {
            content += reader.nextLine() + "\n";
        }
        reader.close();

        if(!content.isEmpty()) {
            JSONArray usersJsonArray = new JSONArray(content);
            for (int i = 0; i < usersJsonArray.length(); i++) {
                JSONObject userObj = usersJsonArray.getJSONObject(i);
                String usernameInFile = userObj.getString("username");
                if (usernameInFile.equals(username)) {
                    return password.equals(userObj.getString("password"));
                }
            }
        }
        return false;
    }
}
