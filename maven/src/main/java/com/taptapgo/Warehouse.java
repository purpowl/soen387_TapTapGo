package com.taptapgo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public boolean addProduct(Product new_product, int amount){
        if (warehouse_instance.product_list.get(new_product) == null){
            warehouse_instance.product_list.put(new_product, amount);
            return true;
        }

        return false;
    }

    /**
     * Modify the amount of an existing product in the warehouse.
     *
     * @param product the product to modify/stock-up
     * @param amount the amount to add for this product
     * @return False if product doesn't exist in warehouse. Else, return True on success.
     */
    public boolean setProductInventory(Product product, int amount) {
        Integer amount_avail = warehouse_instance.product_list.get(product);

        if (amount_avail == null) {
            return false;
        } else {
            warehouse_instance.product_list.replace(product, amount);
            return true;
        }
    }

    /**
     * Looks for a product in the warehouse based on its SKU
     *
     * @param SKU the identifier for the product
     * @return the product object found. Null if the product is not found.
     */
    public Product findProductBySKU(String SKU){
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
    public Integer getProductInventoryBySKU(String SKU){
        for(Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()){
            Product product = product_entry.getKey();
            if(product.getSKU().equals(SKU)) {
                return product_entry.getValue();
            }
        }

        return null;
    }

    /**
     * Looks for amount of a product in the warehouse based on its slug
     *
     * @param SKU the identifier for the product
     * @return the product object found. Null if the product is not found.
     */
    public Integer getProductInventoryBySlug(String slug){
        for(Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()){
            Product product = product_entry.getKey();
            if(product.getSlug().equals(slug)) {
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
    public Product findProductBySlug(String slug){
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
    public boolean removeProductBySKU(String SKU, int amount){
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
    public boolean removeProductBySlug(String slug, int amount){
        for (Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()) {
            Product product = product_entry.getKey();
            if (product.getSlug().equals(slug)) {
                int amount_avail = product_entry.getValue();

                if(amount_avail < amount) {
                    return false;
                } else if (amount_avail == amount) {
                    deleteProduct(product);
                    return true;
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
    public boolean removeProduct(Product product, int amount){
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
    public boolean deleteProductBySKU(String SKU) {
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
    public boolean deleteProduct(Product product) {
        Integer result = warehouse_instance.product_list.remove(product);

        if (result == null){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Store products data from memory into JSON file
     * @throws IOException
     */
    public static void archiveProducts() throws IOException {
        JSONArray database = new JSONArray();

        for (Map.Entry<Product, Integer> product_entry : warehouse_instance.product_list.entrySet()) {
            Product product = product_entry.getKey();
            int amount = product_entry.getValue();
            JSONObject database_entry = new JSONObject();

            // Put the data of each object onto the json object
            database_entry.put("name", product.getName());
            database_entry.put("description", product.getDescription());
            database_entry.put("vendor", product.getVendor());
            database_entry.put("sku", product.getSKU());
            database_entry.put("slug", product.getSlug());
            database_entry.put("price", Double.toString(product.getPrice()));
            database_entry.put("amount", Integer.toString(amount));

            // Write to database
            database.put(database_entry);
        }

        FileWriter output = new FileWriter("data.json");
        output.write(database.toString());
        output.close();
    }

    /**
     * Load product data from JSON file into memory
     */
    public void loadDatabase() {
        String content = "";
        try {
            Scanner reader = new Scanner(new File("data.json"));
            while (reader.hasNextLine()) {
                content += reader.nextLine() + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!content.isEmpty()){
            JSONArray database = new JSONArray(content);

            for (int i = 0; i < database.length(); i++){
                JSONObject db_entry = database.getJSONObject(i);
                
                Product current_product = new Product(db_entry.getString("sku"), db_entry.getString("name"), db_entry.getString("description"), db_entry.getString("vendor"), db_entry.getString("slug"), Double.parseDouble(db_entry.getString("price")));
                Integer amount = Integer.parseInt(db_entry.getString("amount"));

                warehouse_instance.addProduct(current_product, amount);
            }
        }
        
    }
}
