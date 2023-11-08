package com.taptapgo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private static AtomicInteger orderCount = new AtomicInteger(0);
    protected int orderID;
    protected int shippingAddressNum;
    protected String shippingStreet;
    protected String shippingCity;
    protected String shippingCountry;
    protected String shippingPostalCode;
    protected String trackingNumber;
    protected Date payDate;
    protected String paymentMethod;
    protected int cardNum;      // Last 4 digits only
    protected Customer customer;
    protected HashMap<Product, Integer> orderProducts;

    public Order(Customer customer, int addressNum, String street, String city, String country, String postalCode, String payMethod, int cardNum) {
        orderCount.incrementAndGet();
        orderID = Integer.parseInt(String.format("%05d", orderCount.get()));
        this.shippingAddressNum = addressNum;
        this.shippingStreet = street;
        this.shippingCity = city;
        this.shippingCountry = country;
        this.shippingPostalCode = postalCode;
        this.trackingNumber = null;
        this.payDate = new Date();
        this.paymentMethod = payMethod;
        this.cardNum = cardNum;
        this.orderProducts = customer.getCart();
        customer.clearCart();
    }

    public float getOrderTotal() {
        float total = 0;
        for(Map.Entry<Product, Integer> productEntry : orderProducts.entrySet()) {
            Product product = productEntry.getKey();
            int amount = productEntry.getValue();

            total += product.getPrice() * amount;
        }

        return total;
    }


    public int getOrderID() {
        return this.orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getShippingAddressNum() {
        return this.shippingAddressNum;
    }

    public void setShippingAddressNum(int shippingAddressNum) {
        this.shippingAddressNum = shippingAddressNum;
    }

    public String getShippingStreet() {
        return this.shippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }

    public String getShippingCity() {
        return this.shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingCountry() {
        return this.shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getShippingPostalCode() {
        return this.shippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Date getPayDate() {
        return this.payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getCardNum() {
        return this.cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public HashMap<Product,Integer> getOrderProducts() {
        return this.orderProducts;
    }

    public void setOrderProducts(HashMap<Product,Integer> orderProducts) {
        this.orderProducts = orderProducts;
    }

}