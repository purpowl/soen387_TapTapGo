package com.taptapgo;

import java.util.concurrent.atomic.AtomicInteger;

import com.taptapgo.repository.OrderIdentityMap;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {
    public enum ShipmentStatus {Packing, Shipped, Delivered, Canceled};
    private static AtomicInteger orderCount = new AtomicInteger(0);
    protected int orderID;
    protected float totalPrice;
    protected String billAddress;
    protected String billCity;
    protected String billCountry;
    protected String billPostalCode;
    protected ShipmentStatus shippingStatus;
    protected String shippingAddress;
    protected String shippingCity;
    protected String shippingCountry;
    protected String shippingPostalCode;
    protected String trackingNumber;
    protected Date payDate;
    protected Date shipDate;
    protected String paymentMethod;
    protected int cardNum;      // Last 4 digits only
    protected Customer customer;
    protected HashMap<Product, Integer> orderProducts;

    private Order(Customer customer, String billAddress, String billCity, String billCountry, String billPostalCode, String payMethod, int cardNum, String shipAddress, String shipCity, String shipCountry, String shipPostalCode) {
        orderCount.incrementAndGet();
        orderID = Integer.parseInt(String.format("%05d", orderCount.get()));
        this.billAddress = billAddress;
        this.billCity = billCity;
        this.billCountry = billCountry;
        this.billPostalCode = billPostalCode;
        this.shippingAddress = shipAddress;
        this.shippingCity = shipCity;
        this.shippingCountry = shipCountry;
        this.shippingPostalCode = shipPostalCode;
        this.shippingStatus = ShipmentStatus.Packing;
        this.trackingNumber = null;
        this.payDate = new Date(System.currentTimeMillis());
        this.shipDate = null;
        this.paymentMethod = payMethod;
        this.cardNum = cardNum;
        this.customer = customer;
        this.orderProducts = customer.getCart();
        customer.clearCart();
        this.totalPrice = this.calculateOrderTotal();
    }

    private Order(int orderID, float orderTotal, String billAddress, String billCity, String billCountry, String billPostalCode, String payMethod, int cardNum, Date payDate, String shipAddress, String shipCity, String shipCountry, String shipPostalCode, String shipStatus, String trackingNumber, Date shipDate, Customer customer, HashMap<Product, Integer> products) {
        this.orderID = orderID;
        this.totalPrice = orderTotal;
        this.billAddress = billAddress;
        this.billCity = billCity;
        this.billCountry = billCountry;
        this.billPostalCode = billPostalCode;
        this.shippingAddress = shipAddress;
        this.shippingCity = shipCity;
        this.shippingCountry = shipCountry;
        this.shippingPostalCode = shipPostalCode;
        this.trackingNumber = trackingNumber;
        this.payDate = payDate;
        this.shipDate = shipDate;
        this.paymentMethod = payMethod;
        this.cardNum = cardNum;
        this.customer = customer;
        this.orderProducts = products;

        if (shipStatus.equals("packing")){
            this.shippingStatus = ShipmentStatus.Packing;
        } else if(shipStatus.equals("shipped")) {
            this.shippingStatus = ShipmentStatus.Shipped;
        } else if(shipStatus.equals("delivered")) {
            this.shippingStatus = ShipmentStatus.Delivered;
        } else {
            this.shippingStatus = ShipmentStatus.Canceled;
        }
    }

    public static Order loadOrder(int orderID, float orderTotal, String billAddress, String billCity, String billCountry, String billPostalCode, String payMethod, int cardNum, Date payDate, String shipAddress, String shipCity, String shipCountry, String shipPostalCode, String shipStatus, String trackingNumber, Date shipDate, Customer customer, HashMap<Product, Integer> products) {
        return new Order(orderID, orderTotal, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNumber, shipDate, customer, products);
    }

    public static Order createOrder(Customer customer, String billAddress, String billCity, String billCountry, String billPostalCode, String payMethod, int cardNum, String shipAddress, String shipCity, String shipCountry, String shipPostalCode) {
        Order newOrder = new Order(customer, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, shipAddress, shipCity, shipCountry, shipPostalCode);
        
        return newOrder;
    }

    public static boolean addOrderToDB(Order order) {
        return OrderIdentityMap.createOrder(order);
    }


    public float calculateOrderTotal() {
        float total = 0;
        for(Map.Entry<Product, Integer> productEntry : this.orderProducts.entrySet()) {
            Product product = productEntry.getKey();
            int amount = productEntry.getValue();

            total += product.getPrice() * amount;
        }

        return total;
    }

    public static void updateOrderCounter(int startingValue) {
        orderCount = new AtomicInteger(startingValue);
    }


    public String shipStatusToString(){
        if (this.shippingStatus == ShipmentStatus.Packing)
            return "packing";
        if (this.shippingStatus == ShipmentStatus.Shipped)
            return "shipped";
        if (this.shippingStatus == ShipmentStatus.Delivered)
            return "delivered";
        else
            return "canceled";
    }

    public int getOrderID() {
        return this.orderID;
    }

    public float getTotalPrice() {
        return this.totalPrice;
    }

    public String getBillAddress() {
        return this.billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
    }

    public String getBillCity() {
        return this.billCity;
    }

    public void setBillCity(String billCity) {
        this.billCity = billCity;
    }

    public String getBillCountry() {
        return this.billCountry;
    }

    public void setBillCountry(String billCountry) {
        this.billCountry = billCountry;
    }

    public String getBillPostalCode() {
        return this.billPostalCode;
    }

    public void setBillPostalCode(String billPostalCode) {
        this.billPostalCode = billPostalCode;
    }

    public ShipmentStatus getShippingStatus() {
        return this.shippingStatus;
    }

    public void setShippingStatus(ShipmentStatus shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
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

    public Date getShipDate() {
        return this.shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
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