package com.taptapgo;

import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.exceptions.ProductNotFoundException;
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

    public void createProduct(String SKU, String name, String price, String vendor, String desc) {
        double product_price = Double.parseDouble(price);
        String slug = name.split(" ")[0] + "_" + SKU;
        Warehouse.getInstance().addProduct(new Product(SKU, name, desc, vendor, slug, product_price), 0);
    }

    public void updateProduct(String SKU, HashMap<String, Object> fieldsToUpdate) throws ProductNotFoundException, InvalidParameterException {
        Product productToUpdate = Warehouse.getInstance().findProductBySKU(SKU);
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
                    case "URL_slug":
                        productToUpdate.setSlug(field.getValue().toString());
                        break;
                    case "price":
                        productToUpdate.setPrice(Double.parseDouble(field.getValue().toString()));
                        break;
                    default:
                        throw new InvalidParameterException("Invalid parameters. Cannot update product.");
                }
            }
        }
    }

    public void downloadProductCatalog() throws IOException{
        // get product list
        HashMap<Product, Integer> productList = Warehouse.getInstance().getProductList();

        // headers for CSV file
        String[] headers = {"SKU", "name", "vendor", "URL_slug", "price", "description", "quantity"};

        // create CSV data, with headers and information of each product in product list
        // and their quantities in warehouse
        File csvOutputFile = new File("Product_List.csv");
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(headers);

        for (Entry<Product, Integer> product : productList.entrySet()) {
            dataLines.add(new String[] {product.getKey().getSKU(), product.getKey().getName(), product.getKey().getVendor(), product.getKey().getSlug(), String.valueOf(product.getKey().getPrice()), product.getKey().getDescription(), String.valueOf(product.getValue()) });
        }

        // escape special chars and write to file
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream().map(this::convertToCSV).forEach(pw::println);
        }
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data).map(this::escapeSpecialCharacters).collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        if (data != null) {
            String escapedData = data.replaceAll("\\R", " ");
            if (data.contains(",") || data.contains("\"") || data.contains("'")) {
                data = data.replace("\"", "\"\"");
                escapedData = "\"" + data + "\"";
            }
            return escapedData;
        }
        else return data;
    }
}