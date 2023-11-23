package com.taptapgo;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import com.taptapgo.exceptions.InsufficientInventoryException;
import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.exceptions.ProductNotFoundException;
import com.taptapgo.repository.CustomerRepository;
import org.json.JSONArray;
import org.json.JSONObject;

public class Customer {
    protected enum customerTypes {Guest, Registered};

    protected HashMap<Product, Integer> cart;
    protected customerTypes customerType;

    public Customer(String type) throws InvalidParameterException {
        this.cart = new HashMap<>();
        if (type.equals("guest"))
            this.customerType = customerTypes.Guest;
        else if (type.equals("registered"))
            this.customerType = customerTypes.Registered;
        else
            throw new InvalidParameterException("Invalid customer type. Cannot create new customer.");
    }

//    private Customer(String username, String password, String firstName, String lastName, String phone, String email) {
//        super("registered", username, password);
//        this.cart = new HashMap<>();
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phone = phone;
//        this.email = email;
//        this.customerType = customerTypes.Registered;
//    }
//
//    private Customer(String sessionID, String firstName, String lastName, String phone, String email) {
//        super(sessionID);
//        this.cart = new HashMap<>();
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phone = phone;
//        this.email = email;
//        this.customerType = customerTypes.Anonymous;
//    }
//
//    private Customer(String userID, String username, String password, String firstName, String lastName, String phone, String email) {
//        super("registered", userID, username, password);
//        this.cart = new HashMap<>();
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phone = phone;
//        this.email = email;
//        this.customerType = customerTypes.Registered;
//    }

//    public static Customer loadGuestCustomer(String GuestID, String firstName, String lastName, String phone, String email) {
//        return new Customer(GuestID, firstName, lastName, phone, email);
//    }
//
//    public static Customer loadRegisteredCustomer(String CustomerID, String username, String password, String firstName, String lastName, String phone, String email) {
//        return new Customer(CustomerID, username, password, firstName, lastName, phone, email);
//    }
//
//    public static Customer createGuestCustomer(String userID, String firstName, String lastName, String phone, String email) {
//        return new Customer(userID, firstName, lastName, phone, email);
//    }

//    public static Customer createRegisteredCustomer(String username, String password, String firstName, String lastName, String phone, String email) throws IOException {
//        String content = "";
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        InputStream is = classLoader.getResourceAsStream("/credentials.json");
//
//        assert is != null;
//        Scanner reader = new Scanner(is, "UTF-8");
//        //Scanner reader = new Scanner(new File("/credentials.json"));
//        while (reader.hasNextLine()) {
//            content += reader.nextLine() + "\n";
//        }
//        reader.close();
//
//        if(!content.isEmpty()) {
//            JSONArray usersJsonArray = new JSONArray(content);
//            JSONObject newUserObj = new JSONObject();
//            newUserObj.put(username, password);
//
//            // Put the data of each object onto the json object
//            usersJsonArray.put(newUserObj);
//
////            try (OutputStream out = new FileOutputStream;
////                 Writer writer = new OutputStreamWriter(out,"UTF-8")) {
////                writer.write(data);
////            }
//
//            FileWriter output = new FileWriter("/credentials.json");
//            output.write(usersJsonArray.toString());
//            output.close();
//        }
//        return new Customer(username, password, firstName, lastName, phone, email);
//    }

    public HashMap<Product, Integer> getCart() {
        return this.cart;
    }

    public void loadCart(HashMap<Product, Integer> cart) {
        this.cart = cart;
    }

    // find the product and its quantity in cart by SKU
    public Product findCartProductEntryBySKU(String SKU) {
        for (Entry<Product, Integer> entry : cart.entrySet()) {
            Product productToFind = entry.getKey();
            if (productToFind != null && productToFind.getSKU().equals(SKU)) return productToFind;
        }
        return null;
    }

    public void addProductToCart(String SKU, int amount) throws InvalidParameterException, InsufficientInventoryException, ProductNotFoundException {
        // check if amount is postive
        if (amount <= 0) throw new InvalidParameterException("Product amount must be greater than 0.");

        // check that the product exists in the warehouse
        Product product = Warehouse.getInstance().findProductBySKU(SKU);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        // if product exists, check that the amount to add does not exceed warehouse inventory
        else {
            Product productInCart = findCartProductEntryBySKU(SKU);
            Integer amountInWarehouse = Warehouse.getInstance().getProductInventoryBySKU(SKU);
            if ((productInCart != null && cart.get(productInCart)+amount <= amountInWarehouse) || (productInCart == null && amount <= amountInWarehouse)) {
                // add to cart
                if (productInCart != null) cart.merge(productInCart, amount, (oldAmount, newAmount) -> oldAmount + newAmount);
                else cart.put(Warehouse.getInstance().findProductBySKU(SKU), amount);
            }
            else throw new InsufficientInventoryException();
        }
    }

    public void removeProductFromCart(String SKU) throws ProductNotFoundException {
        // check if cart is empty
        if (!cart.isEmpty()) {
            Product productInCart = findCartProductEntryBySKU(SKU);
            // remove product if it's found in cart
            if (productInCart == null) throw new ProductNotFoundException("Product not found in cart.");
            else cart.remove(productInCart);
        }
    }

    public void modifyProductCountInCart(String SKU, int amount) throws InvalidParameterException, ProductNotFoundException, InsufficientInventoryException {
        // check if amount is positive
        if (amount <= 0) throw new InvalidParameterException("Product amount must be greater than 0.");

        // check if cart is empty
        if (!cart.isEmpty()) {
            Product productInCart = findCartProductEntryBySKU(SKU);

            // check if product exists in cart
            if (productInCart == null) throw new ProductNotFoundException("Product not found in cart.");
            else {
                // check if the new amount does not exceed warehouse inventory
                Integer amountInWarehouse = Warehouse.getInstance().getProductInventoryBySKU(SKU);
                if (amount > amountInWarehouse) throw new InsufficientInventoryException();
                else cart.put(productInCart, amount);
            }
        }
    }

    public void clearCart() {
        this.cart = new HashMap<>();
    }

    public String customerTypeToString() {
        if (this.customerType == customerTypes.Guest) {
            return "guest";
        } else {
            return "registered";
        }
    }

}