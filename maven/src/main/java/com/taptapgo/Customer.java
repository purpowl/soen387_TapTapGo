package com.taptapgo;

import java.util.HashMap;
import java.util.Map.Entry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.taptapgo.exceptions.InsufficientInventoryException;
import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.exceptions.ProductNotFoundException;

public class Customer extends User {
    protected enum customerTypes {Anonymous, Registered};

    protected HashMap<Product, Integer> cart;
    protected customerTypes customerType;
    protected String firstName;
    protected String lastName;
    protected String phone;
    protected String email;

    private Customer(String firstName, String lastName, String phone, String email) {
        super();
        this.cart = new HashMap<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.customerType = customerTypes.Anonymous;
    }

    private Customer(String username, String password, String firstName, String lastName, String phone, String email) {
        super("registered", username, password);
        this.cart = new HashMap<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.customerType = customerTypes.Registered;
    }

    private Customer(String userID, String firstName, String lastName, String phone, String email) {
        super(userID);
        this.cart = new HashMap<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.customerType = customerTypes.Anonymous;
    }

    private Customer(String userID, String username, String password, String firstName, String lastName, String phone, String email) {
        super("registered", userID, username, password);
        this.cart = new HashMap<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.customerType = customerTypes.Registered;
    }

    public static Customer loadGuestCustomer(String GCID, String firstName, String lastName, String phone, String email) {
        return new Customer(GCID, firstName, lastName, phone, email);
    }

    public static Customer loadRegisteredCustomer(String CustomerID, String username, String password, String firstName, String lastName, String phone, String email) {
        return new Customer(CustomerID, username, password, firstName, lastName, phone, email);
    }

    public static Customer createGuestCustomer(String firstName, String lastName, String phone, String email) {
        return new Customer(firstName, lastName, phone, email);
    }

    public static Customer createRegisteredCustomer(String username, String password, String firstName, String lastName, String phone, String email) {

        return new Customer(username, password, firstName, lastName, phone, email);
    }

    public HashMap<Product, Integer> getCart() {
        return this.cart;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null){
            return false;
        }
        if (!(object.getClass().equals(this.getClass()))){
            return false;
        }

        Customer otherCustomer = (Customer) object;

        if(otherCustomer.userID.equals(this.userID) && otherCustomer.firstName.equals(this.firstName) && otherCustomer.lastName.equals(this.lastName)){
            return true;
        }
        return false;
    }
}