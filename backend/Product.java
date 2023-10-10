package backend;

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

    public Product(String sku, String name, double price){
        
    }
}