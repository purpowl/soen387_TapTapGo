package com.taptapgo;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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

    public Customer(String sessionID) {
        super(sessionID);
        this.cart = new HashMap<>();
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.email = null;
        this.customerType = customerTypes.Anonymous;
    }

    public Customer(String username, String password, String firstName, String lastName, String phone, String email) {
        super("registered", username, password);
        this.cart = new HashMap<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.customerType = customerTypes.Registered;
    }

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

    public String customerTypeToString() {
        if (this.customerType == customerTypes.Anonymous) {
            return "anonymous";
        } else {
            return "registered";
        }
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public void createOrder(String shippingAddress) {

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

//    public Order getOrder(int orderID) {
//
//    }
}