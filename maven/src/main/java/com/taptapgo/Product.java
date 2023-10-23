package com.taptapgo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Product
 */
public class Product {
    protected String SKU;
    protected String name;
    protected String description;
    protected String vendor;
    protected String URL_slug;
    protected double price;

    public Product(){
        this.SKU = null;
        this.name = null;
        this.description = null;
        this.vendor = null;
        this.URL_slug = null;
        this.price = -1;
    }

    public Product(String sku, String name, double price, String slug){
        this.SKU = sku;
        this.name = name;
        this.price = price;
        this.description = "";
        this.vendor = "";
        this.URL_slug = slug;
    }

    public Product(String sku, String name, String description, String vendor, String url_slug, double price){
        this.SKU = sku;
        this.name = name;
        this.price = price;
        this.description = description;
        this.vendor = vendor;
        this.URL_slug = url_slug;
    }

    public String getSKU(){
        return this.SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return this.vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getSlug() {
        return this.URL_slug;
    }

    public void setSlug(String URL_slug) {
        this.URL_slug = URL_slug;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Compare between two products to determine if they are the same product.
     * Comparison is made based on name, SKU, URL slug, and price.
     *
     * @param anotherObject the product that we want to compare to
     * @return True if it is the same product. Else, return False.
     */
    @Override
    public boolean equals(Object anotherObject){
        if(anotherObject == null || getClass() != anotherObject.getClass()) {
            return false;
        }

        Product anotherProduct = (Product) anotherObject;

        if (!this.name.equals(anotherProduct.name) || !this.SKU.equals(anotherProduct.SKU) || !this.URL_slug.equals(anotherProduct.URL_slug) || this.price != anotherProduct.price) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * An utility function to round up prices
     * @param value the value/price that you want to round up (to 2 decimal places)
     * @return the string representation of the value, to be printed out
     */
    public static String roundPrice(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        double result = bd.doubleValue();

        return String.format("%.2f", result);
    }
}
