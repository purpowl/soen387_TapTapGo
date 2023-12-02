package com.taptapgo;

import java.util.concurrent.atomic.AtomicInteger;

import com.taptapgo.repository.OrderIdentityMap;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    public enum ShipmentStatus {Packing, Shipped, Delivered, Canceled}
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
    protected HashMap<Product, Integer> orderProducts;
    protected String customerID;

    private Order(String billAddress, String billCity, String billCountry, String billPostalCode, String payMethod, int cardNum, String shipAddress, String shipCity, String shipCountry, String shipPostalCode, HashMap<Product, Integer> products, String customerID) {
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
        this.orderProducts = products;
        this.totalPrice = this.calculateOrderTotal();
        this.customerID = customerID;
    }

    private Order(int orderID, float orderTotal, String billAddress, String billCity, String billCountry, String billPostalCode, String payMethod, int cardNum, Date payDate, String shipAddress, String shipCity, String shipCountry, String shipPostalCode, String shipStatus, String trackingNumber, Date shipDate, HashMap<Product, Integer> products, String customerID) {
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
        this.orderProducts = products;
        this.customerID = customerID;

        switch (shipStatus) {
            case "packing":
                this.shippingStatus = ShipmentStatus.Packing;
                break;
            case "shipped":
                this.shippingStatus = ShipmentStatus.Shipped;
                break;
            case "delivered":
                this.shippingStatus = ShipmentStatus.Delivered;
                break;
            default:
                this.shippingStatus = ShipmentStatus.Canceled;
                break;
        }
    }

    public static Order loadOrder(int orderID, float orderTotal, String billAddress, String billCity, String billCountry, String billPostalCode, String payMethod, int cardNum, Date payDate, String shipAddress, String shipCity, String shipCountry, String shipPostalCode, String shipStatus, String trackingNumber, Date shipDate, HashMap<Product, Integer> products, String customerID) {
        return new Order(orderID, orderTotal, billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, payDate, shipAddress, shipCity, shipCountry, shipPostalCode, shipStatus, trackingNumber, shipDate, products, customerID);
    }

    public static Order createOrder(String billAddress, String billCity, String billCountry, String billPostalCode, String payMethod, int cardNum, String shipAddress, String shipCity, String shipCountry, String shipPostalCode, HashMap<Product, Integer> products, String customerID) {
        return new Order(billAddress, billCity, billCountry, billPostalCode, payMethod, cardNum, shipAddress, shipCity, shipCountry, shipPostalCode, products, customerID);
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

    public void setShippingStatus(String shipStatus) {
        switch (shipStatus) {
            case "packing":
                this.shippingStatus = ShipmentStatus.Packing;
                break;
            case "shipped":
                this.shippingStatus = ShipmentStatus.Shipped;
                break;
            case "delivered":
                this.shippingStatus = ShipmentStatus.Delivered;
                break;
            default:
                this.shippingStatus = ShipmentStatus.Canceled;
                break;
        }
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

    public HashMap<Product,Integer> getOrderProducts() {
        return this.orderProducts;
    }

    public void setOrderProducts(HashMap<Product,Integer> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getCustomerID() {return this.customerID;}

    public void setCustomerID(String customerID) {this.customerID = customerID;}

    public static List<Order> sortOrdersBy(List<Order> orders, String sortCriteria, String sortOrder) {
        boolean sortByPayDate = false;
        boolean sortByID = false;
        boolean sortByShipDate = false;
        boolean ascending = false;
        boolean descending = false;

        switch(sortCriteria){
            case "PayDate":
                sortByPayDate = true;
                break;
            case "ID":
                sortByID = true;
                break;
            case "ShipDate":
                sortByShipDate = true;
                break;
            default:
                System.out.println("ERROR: Order.SortOrderBy() : Invalid choice of sort criteria \"" + sortCriteria + "\"");
                System.out.println("Available sort criteria: PayDate, ID, ShipDate");
                return orders;
        }

        if(sortOrder.equals("ascending"))
            ascending = true;
        else if(sortOrder.equals("descending"))
            descending = true;
        else {
            System.out.println("ERROR: Order.SortOrderBy() : Invalid choice of sort order \"" + sortOrder + "\"");
            System.out.println("Available sort order: ascending, descending");
            return orders;
        }

        for(int i = 0; i < orders.size(); i++) {
            Order currentItem = orders.get(i);
            for(int k = i+1; k < orders.size(); k++) {
                Order nextItem = orders.get(k);
                if(ascending) {
                    if(sortByPayDate) {
                        if(nextItem.getPayDate().before(currentItem.getPayDate())) {
                            Collections.swap(orders, i, k);
                            currentItem = nextItem;
                        }
                    } else if(sortByID) {
                        if(nextItem.getOrderID() < currentItem.getOrderID()) {
                            Collections.swap(orders, i, k);
                            currentItem = nextItem;
                        }
                    } else {
                        if(nextItem.getShipDate() == null && currentItem.getShipDate() != null) {
                            Collections.swap(orders, i, k);
                            break;
                        } else if (nextItem.getShipDate() != null && currentItem.getShipDate() != null && nextItem.getShipDate().before(currentItem.getShipDate())) {
                            Collections.swap(orders, i, k);
                            currentItem = nextItem;
                        }
                    }
                } else if(descending) {
                    if(sortByPayDate) {
                        if(nextItem.getPayDate().after(currentItem.getPayDate())) {
                            Collections.swap(orders, i, k);
                            currentItem = nextItem;
                        }
                    } else if(sortByID) {
                        if(nextItem.getOrderID() > currentItem.getOrderID()) {
                            Collections.swap(orders, i, k);
                            currentItem = nextItem;
                        }
                    } else {
                        if (nextItem.getShipDate() != null && currentItem.getShipDate() != null && nextItem.getShipDate().after(currentItem.getShipDate())) {
                            Collections.swap(orders, i, k);
                            currentItem = nextItem;
                        } else if(nextItem.getShipDate() != null && currentItem.getShipDate() == null) {
                            Collections.swap(orders, i, k);
                            currentItem = nextItem;
                        }
                    }
                }
            }
        }

        // if(sortByShipDate) {
        //     if(ascending) {
        //         List<Order> sublist = orders.subList(0, shipDateNotNullCounter);
        //         Order.sortOrdersBy(sublist, "PayDate", sortOrder);
        //     } else if(descending) {
        //         List<Order> sublist = orders.subList(shipDateNotNullCounter, orders.size()-1);
        //         Order.sortOrdersBy(sublist, "PayDate", sortOrder);
        //     }
        // }

        return orders;
    }
}