package com.taptapgo;

import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.exceptions.ProductAreadyExistsException;
import com.taptapgo.exceptions.ProductNotFoundException;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Staff extends User{
    private static AtomicInteger staffID = new AtomicInteger(0);

    public Staff(String username, String password) {
        super(username, password);
        staffID.incrementAndGet();
    }

    public void createProduct(String SKU, String name, double price, String vendor, String desc, int amount) throws ProductAreadyExistsException, InvalidParameterException {
        // check product doesn't already exist in warehouse
        if (Warehouse.getInstance().findProductBySKU(SKU) != null) 
            throw new ProductAreadyExistsException();
        // check SKU and name are not blank
        else if (SKU == null || SKU.equals("") || name == null || name.equals("")) 
            throw new InvalidParameterException("Product SKU and Name cannot be blank.");
        // otherwise add new product to warehouse
        else {
            String slug = name.split(" ")[0] + "_" + SKU;
            Warehouse.getInstance().addProduct(new Product(SKU, name, desc, vendor, slug, price), amount);
        }
    }

    public void updateProduct(String slug, HashMap<String, Object> fieldsToUpdate) throws ProductNotFoundException, InvalidParameterException {
        Product productToUpdate = Warehouse.getInstance().findProductBySlug(slug);
        // check if product exists in warehouse
        if (productToUpdate == null) throw new ProductNotFoundException();
        else {
            // if product exists, check the fields to update and update them
            for (Entry<String, Object> field : fieldsToUpdate.entrySet()) {
                switch (field.getKey()) {
                    case "name" :
                        productToUpdate.setName(field.getValue().toString());
                        break;
                    case "description":
                        productToUpdate.setDescription(field.getValue().toString());
                        break;
                    case "vendor":
                        productToUpdate.setVendor(field.getValue().toString());
                        break;
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
                        Warehouse.getInstance().setProductInventory(productToUpdate, amount);
                        break;
                    case "price":
                        String price_str = field.getValue().toString();
                        double price;

                        try {
                            price = Double.parseDouble(price_str);
                            if (price <= 0) {
                                throw new InvalidParameterException("Price entered is negative.");
                            }
                        } catch (Exception e) {
                            throw new InvalidParameterException("Price entered is not a number.");
                        }
                        productToUpdate.setPrice(price);
                        break;
                    default:
                        throw new InvalidParameterException("Invalid parameters. Cannot update product.");
                }
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

    // method to write to csv file locally
    public void downloadProductCatalog(String[] data) throws IOException {
        File csvOutputFile = new File("Product_List.csv");

        // get the String for product list
        String productCatalog = getProductCatalog();

        // write the product catalog to a csv file
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.print(productCatalog);
        }
    }

    private String escapeSpecialCharacters(String data) {
        if (data != null && !data.equals("")) {
            data = data.replaceAll("\\R", " ");
            if (data.contains(",") || data.contains("\"") || data.contains("'")) {
                data = data.replace("\"", "\"\"");
            }
            return data;
        }
        else return "";
    }
}