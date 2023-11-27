package com.taptapgo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.taptapgo.repository.WarehouseRepository;

public class Warehouse {
    private static Warehouse warehouse_instance = null;
    private HashMap<Product, Integer> product_list;

    private Warehouse() {
        product_list = new HashMap<>();
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
            boolean db_result = WarehouseRepository.createProduct(new_product, amount);

            if(db_result){
                warehouse_instance.product_list.put(new_product, amount);
                return true;
            }
            
            return false;
        }
        return false;
    }

    /**
     * Modify the amount of an existing product in the warehouse.
     * Caller must check if the amount is negative or not
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
            boolean db_result = WarehouseRepository.modifyProductInventory(product, amount, null);
            if (db_result) {
                warehouse_instance.product_list.replace(product, amount);
                return true;
            }
            
            return false;
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
     * @param slug the identifier for the product
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

                    boolean db_result = WarehouseRepository.modifyProductInventory(product, amount, null);
                    
                    if(db_result) {
                        warehouse_instance.product_list.replace(product, amount_left);
                        return true;
                    }
                    
                    return false;
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

                    boolean db_result = WarehouseRepository.modifyProductInventory(product, amount, null);
                    if(db_result) {
                        warehouse_instance.product_list.replace(product, amount_left);
                        return true;
                    }
                    
                    return false;
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

            boolean db_result = WarehouseRepository.modifyProductInventory(product, amount_left, null);
            if (db_result){
                warehouse_instance.product_list.replace(product, amount_left);
                return true;
            }
            
            return false;
        }
    }

    /**
     * Removes a certain amount of a product (specified by Product object) from the warehouse.
     * This function is used when the caller is another repository, which is also trying to write to database.
     * The database connection can be passed in here to prevent opening another database connection.
     *
     * @param product the product to be removed
     * @param amount the amount of product that you want to remove
     * @param db_conn the existing database connection passed in from the caller
     * @return False if the product is not found or there is not enough product left to remove. Else, return True on success.
     */
    public boolean removeProduct(Product product, int amount, Connection db_conn){
        Integer amount_avail = warehouse_instance.product_list.get(product);

        if (amount_avail == null) {
            return false;
        } else if (amount_avail < amount) {
            return false;
        } else {
            int amount_left = amount_avail - amount;

            boolean db_result = WarehouseRepository.modifyProductInventory(product, amount_left, db_conn);
            if (db_result){
                warehouse_instance.product_list.replace(product, amount_left);
                return true;
            }
            
            return false;
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
                boolean db_result = WarehouseRepository.deleteProduct(product);

                if (db_result) {
                    warehouse_instance.product_list.remove(product);
                    return true;
                }
                
                return false;
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
        // Make sure the product is in warehouse
        Integer findProduct = warehouse_instance.product_list.get(product);
        if (findProduct == null) {
            return false;
        }

        boolean db_result = WarehouseRepository.deleteProduct(product);

        if(db_result){
            warehouse_instance.product_list.remove(product);
            return true;
        }
        return false;
    }

    /**
     * Load product data from JSON file into memory
     */
    public void loadDatabase() {
        warehouse_instance.product_list = WarehouseRepository.loadWarehouse();
    }


    /**
     * Update the information of a product in the warehouse.
     * This function will assume the product is valid, and all the fields are valid.
     * Caller has to check if this product exist in the warehouse or not.
     * 
     * @param product the product to be updated
     * @param fieldsToUpdate fields used to update
     * @return true on success, false on failure
     */
    public boolean updateProductInfo(Product product, HashMap<String, Object> fieldsToUpdate) {
        // First, update in the database
        boolean db_result = WarehouseRepository.updateProductInfo(product.getSKU(), fieldsToUpdate);

        if(db_result) {
            for (Entry<String, Object> field : fieldsToUpdate.entrySet()) {
                switch (field.getKey()) {
                    case "name" :
                        product.setName(field.getValue().toString());
                        break;
                    case "description":
                        product.setDescription(field.getValue().toString());
                        break;
                    case "vendor":
                        product.setVendor(field.getValue().toString());
                        break;
                    case "amount":
                        Integer amount = Integer.parseInt(field.getValue().toString());
                        Warehouse.getInstance().setProductInventory(product, amount);
                        break;
                    case "price":
                        Float price = Float.parseFloat(field.getValue().toString());
                        product.setPrice(price);
                        break;
                }
            }

            return true;
        }
        
        return false;
    }
}
