package backend;

import java.util.HashMap;
import java.util.Map;

public class Warehouse {
    private static Warehouse warehouse_instance = null;
    private HashMap<Product, Integer> product_list;


    private Warehouse() {
        product_list = new HashMap<Product, Integer>();
    }


    public static synchronized Warehouse getInstance() {
        if (warehouse_instance == null){
            warehouse_instance = new Warehouse();
        }

        return warehouse_instance;
    }

    public HashMap<Product, Integer> getProductList() {
        return product_list;
    }

    /**
     * Adds a new product to the warehouse, with a specified amount
     * 
     * @param new_product the product to be added to warehouse
     * @param amount the amount of the product to add
     * @return False if product already exists in the warehouse. Else, return True on success
     */
    public static boolean addProduct(Product new_product, int amount){
        if (warehouse_instance.product_list.get(new_product) == null){
            warehouse_instance.product_list.put(new_product, amount);
            return true;
        }
        
        return false;
    }

    /**
     * Increment the amount of an existing product in the warehouse.
     * 
     * @param product the product to increment/stock-up
     * @param amount the amount to add for this product
     * @return False if product doesn't exist in warehouse. Else, return True on success.
     */
    public static boolean incrementProduct(Product product, int amount) {
        Integer amount_avail = warehouse_instance.product_list.get(product);

        if (amount_avail == null) {
            return false;
        } else {
            int amount_total = amount_avail + amount;
            warehouse_instance.product_list.replace(product, amount_total);
            return true;
        }
    }

    /**
     * Looks for a product in the warehouse based on its SKU
     * 
     * @param SKU the identifier for the product
     * @return the product object found. Null if the product is not found.
     */
    public static Product findProductBySKU(String SKU){
        for(Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()){
            Product product = product_entry.getKey();
            if(product.getSKU().equals(SKU)) {
                return product;
            }
        }

        return null;
    }

    /**
     * Looks for amount of a product in the warehouse based on its SKU
     * 
     * @param SKU the identifier for the product
     * @return the product object found. Null if the product is not found.
     */
    public static Integer findProductInventoryBySKU(String SKU){
        for(Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()){
            Product product = product_entry.getKey();
            if(product.getSKU().equals(SKU)) {
                return product_entry.getValue();
            }
        }

        return null;
    }

    /**
     * Looks for a product in the warehouse based on its slug
     * 
     * @param slug the identifier for the product
     * @return the product object found. Null if the product is not found.
     */
    public static Product findProductBySlug(String slug){
        for (Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()){
            Product product = product_entry.getKey();
            if (product.getSlug().equals(slug)){
                return product;
            }
        }

        return null;
    }

    /**
     * Removes a certain amount of a product (specified by its SKU) from the warehouse
     * 
     * @param SKU the identifier for the product you want to remove
     * @param amount the amount that you want to remove
     * @return False if there is not enough product left to remove or product is not found. Else, return True on success.
     */
    public static boolean removeProductBySKU(String SKU, int amount){
        for (Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()) {
            Product product = product_entry.getKey();
            if (product.getSKU().equals(SKU)) {
                int amount_avail = product_entry.getValue();

                if(amount_avail < amount) {
                    return false;
                } else {
                    int amount_left = amount_avail - amount;
                    warehouse_instance.product_list.replace(product, amount_left);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Removes a certain amount of a product (specified by its slug) from the warehouse
     * 
     * @param slug the identifier for the product you want to remove
     * @param amount the amount that you want to remove
     * @return False if there is not enough product left to remove or product is not found. Else, return True on success.
     */
    public static boolean removeProductBySlug(String slug, int amount){
        for (Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()) {
            Product product = product_entry.getKey();
            if (product.getSlug().equals(slug)) {
                int amount_avail = product_entry.getValue();

                if(amount_avail < amount) {
                    return false;
                } else {
                    int amount_left = amount_avail - amount;
                    warehouse_instance.product_list.replace(product, amount_left);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Removes a certain amount of a product (specified by Product object) from the warehouse
     * 
     * @param product the product to be removed
     * @param amount the amount of product that you want to remove
     * @return False if the product is not found or there is not enough product left to remove. Else, return True on success.
     */
    public static boolean removeProduct(Product product, int amount){
        Integer amount_avail = warehouse_instance.product_list.get(product);

        if (amount_avail == null) {
            return false;
        } else if (amount_avail < amount) {
            return false;
        } else {
            int amount_left = amount_avail - amount;
            warehouse_instance.product_list.replace(product, amount_left);
            return true;
        }
    }

    /**
     * Completely deletes a product from the warehouse, regardless of the amount left
     * 
     * @param SKU the identifier for the product you want to delete
     * @return False if product is not found. Else, return True on success.
     */
    public static boolean deleteProductBySKU(String SKU) {
        for (Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()) {
            Product product = product_entry.getKey();
            if (product.getSKU().equals(SKU)) {
                warehouse_instance.product_list.remove(product);
                return true;
            }
        }

        return false;
    }

    /**
     * Completely deletes a product from the warehouse, regardless of the amount left
     * 
     * @param product the product to be deleted
     * @return True if the operation succeed. False if the product was not found.
     */
    public static boolean deleteProduct(Product product) {
        Integer result = warehouse_instance.product_list.remove(product);

        if (result == null){
            return false;
        } else {
            return true;
        }
    }
}
