package com.taptapgo;

import com.taptapgo.exceptions.DatabaseException;
import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.exceptions.ProductAreadyExistsException;
import com.taptapgo.exceptions.ProductNotFoundException;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Staff {

    public void createProduct(String SKU, String name, float price, String vendor, String desc, int amount) throws ProductAreadyExistsException, InvalidParameterException {
        // check product doesn't already exist in warehouse
        if (Warehouse.getInstance().findProductBySKU(SKU) != null) 
            throw new ProductAreadyExistsException();
        // check SKU and name are not blank
        else if (SKU == null || SKU.isEmpty() || name == null || name.isEmpty())
            throw new InvalidParameterException("Product SKU and Name cannot be blank.");
        // otherwise add new product to warehouse
        else {
            String slug = name.split(" ")[0] + "_" + SKU;
            Warehouse.getInstance().addProduct(new Product(SKU, name, desc, vendor, slug, price), amount);
        }
    }

    public void updateProduct(String slug, HashMap<String, Object> fieldsToUpdate) throws ProductNotFoundException, InvalidParameterException, DatabaseException {
        Product productToUpdate = Warehouse.getInstance().findProductBySlug(slug);
        // check if product exists in warehouse
        if (productToUpdate == null) throw new ProductNotFoundException();
        else {
            // Check for errors in update fields
            for (Entry<String, Object> field : fieldsToUpdate.entrySet()) {
                switch (field.getKey()) {
                    case "amount":
                        String amount_str = field.getValue().toString();
                        int amount;
                        try {
                            amount = Integer.parseInt(amount_str);
                            if (amount <= 0) {
                                throw new InvalidParameterException("Amount entered is negative.");
                            }
                        } catch (Exception e) {
                            throw new InvalidParameterException("Amount entered is not a number.");
                        }
                        break;
                    case "price":
                        String price_str = field.getValue().toString();
                        float price;

                        try {
                            price = Float.parseFloat(price_str);
                            if (price <= 0) {
                                throw new InvalidParameterException("Price entered is negative.");
                            }
                        } catch (Exception e) {
                            throw new InvalidParameterException("Price entered is not a number.");
                        }
                        break;
                }
            }

            boolean warehouseUpdateResult = Warehouse.getInstance().updateProductInfo(productToUpdate, fieldsToUpdate);
            if(!warehouseUpdateResult) {
                throw new DatabaseException("Fail to update product in database");
            }
        }
    }

    public void deleteProduct(String slug) throws ProductNotFoundException{
        Product productToDelete = Warehouse.getInstance().findProductBySlug(slug);

        if (productToDelete == null) throw new ProductNotFoundException();
        else {
            Warehouse.getInstance().deleteProduct(productToDelete);
        }
    }

    public String getProductCatalog() throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        // get product list
        HashMap<Product, Integer> productList = Warehouse.getInstance().getProductList();

        // headers for CSV file
        String[] headers = {"SKU", "name", "vendor", "URL_slug", "price", "description", "quantity"};

        // create CSV data, with headers and information of each product in product list
        // and their quantities in warehouse
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(headers);
        for (Entry<Product, Integer> product : productList.entrySet()) {
            dataLines.add(new String[] {product.getKey().getSKU(), product.getKey().getName(), product.getKey().getVendor(), product.getKey().getSlug(), String.valueOf(product.getKey().getPrice()), product.getKey().getDescription(), String.valueOf(product.getValue()) });
        }

        // build a string with delimited csv data
        for (String[] strArr : dataLines) {
            String concatStr = String.join(", ", strArr);
            stringBuilder.append(escapeSpecialCharacters(concatStr)).append("\n");
        }

        // return built string
        return stringBuilder.toString();
    }

    private String escapeSpecialCharacters(String data) {
        if (data != null && !data.isEmpty()) {
            data = data.replaceAll("\\R", " ");
            if (data.contains(",") || data.contains("\"") || data.contains("'")) {
                data = data.replace("\"", "\"\"");
            }
            return data;
        }
        else return "";
    }
}