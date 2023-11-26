package com.taptapgo.repository;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.taptapgo.Product;

public class WarehouseRepository {
    private static Connection db_conn;
    
    public static boolean createProduct(Product product, int amount) {
        String insertProductQuery = "INSERT INTO product (ProductSKU, Name, Description, Price, Vendor, Slug, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertWarehouseQuery = "INSERT INTO warehouse (ProductSKU, Stock) VALUES (?, ?)";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Save checkpoint to rollback in case insert fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt1 = db_conn.prepareStatement(insertProductQuery);
            pstmt1.setString(1, product.getSKU());
            pstmt1.setString(2, product.getName());
            pstmt1.setString(3, product.getDescription());
            pstmt1.setFloat(4, product.getPrice());
            pstmt1.setString(5, product.getVendor());
            pstmt1.setString(6, product.getSlug());
            pstmt1.setString(7, product.getImagePath());

            pstmt1.executeUpdate();

            PreparedStatement pstmt2 = db_conn.prepareStatement(insertWarehouseQuery);
            pstmt2.setString(1, product.getSKU());
            pstmt2.setInt(2, amount);

            pstmt2.executeUpdate();

            db_conn.commit();
            db_conn.setAutoCommit(true);
            db_conn.close();

            return true;
        } catch (SQLException e) {
            try {
                if (db_conn != null && savepoint != null) {
                    db_conn.rollback(savepoint); // Rollback the transaction if an exception occurs
                    db_conn.setAutoCommit(true);
                    db_conn.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return false;
        }
    }

    
    public static HashMap<Product, Integer> loadWarehouse() {
        String getProductsQuery = "SELECT ProductSKU, Name, Description, Price, Vendor, Slug, ImagePath FROM product";
        String getProductAmountQuery = "SELECT Stock FROM warehouse WHERE ProductSKU = ?";
        HashMap<Product, Integer> warehouse = new HashMap<Product, Integer>();

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);
            System.out.println("Database URL: " + dbUrl.toString());

            PreparedStatement pstmt1 = db_conn.prepareStatement(getProductsQuery);
            ResultSet productsResult = pstmt1.executeQuery();

            while (productsResult.next()) {
                String productSKU = productsResult.getString(1);
                String productName = productsResult.getString(2);
                String productDesc = productsResult.getString(3);
                Float productPrice = productsResult.getFloat(4);
                String productVendor = productsResult.getString(5);
                String slug = productsResult.getString(6);
                String imagePath = productsResult.getString(7);
                int amount = 0;

                Product newProduct = new Product(productSKU, productName, productDesc, productVendor, slug, productPrice);
                newProduct.setImagePath(imagePath);

                PreparedStatement pstmt2 = db_conn.prepareStatement(getProductAmountQuery);
                pstmt2.setString(1, productSKU);
                ResultSet stockResult = pstmt2.executeQuery();

                if (stockResult.next()) {
                    amount = stockResult.getInt(1);
                } else {
                    throw new SQLException();
                }

                pstmt2.close();
                warehouse.put(newProduct, amount);
            }

            pstmt1.close();
            db_conn.close();

            return warehouse;
        } catch(Exception e) {
            e.printStackTrace();
            return warehouse;
        }
    }

    
    public static boolean modifyProductInventory(Product product, int amount) {
        String modifyQuery = "UPDATE warehouse SET Stock = ? WHERE ProductSKU = ?";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Save checkpoint to rollback in case update fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt = db_conn.prepareStatement(modifyQuery);
            pstmt.setInt(1, amount);
            pstmt.setString(2, product.getSKU());

            pstmt.executeUpdate();
            db_conn.commit();

            db_conn.setAutoCommit(true);
            db_conn.close();

            return true;
        } catch (SQLException e) {
            try {
                if (db_conn != null && savepoint != null) {
                    db_conn.rollback(savepoint); // Rollback the transaction if an exception occurs
                    db_conn.setAutoCommit(true);
                    db_conn.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return false;
        }
    }

    
    public static boolean deleteProduct(Product product) {
        String deleteInventoryQuery = "DELETE FROM warehouse WHERE productSKU = ?";
        String deleteProductQuery = "DELETE FROM product WHERE productSKU = ?";
        Savepoint savepoint = null;

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Save checkpoint to rollback in case update fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            PreparedStatement pstmt1 = db_conn.prepareStatement(deleteInventoryQuery);
            pstmt1.setString(1, product.getSKU());

            pstmt1.executeUpdate();

            PreparedStatement pstmt2 = db_conn.prepareStatement(deleteProductQuery);
            pstmt2.setString(1, product.getSKU());

            pstmt2.executeUpdate();
            db_conn.commit();

            db_conn.setAutoCommit(true);
            db_conn.close();

            return true;

        } catch (SQLException e) {
            try {
                if (db_conn != null && savepoint != null) {
                    db_conn.rollback(savepoint); // Rollback the transaction if an exception occurs
                    db_conn.setAutoCommit(true);
                    db_conn.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return false;
        }
    }

    public static boolean updateProductInfo(String productSKU, HashMap<String, Object> fieldsToUpdate) {
        String updateProductQuery = "UPDATE product SET ";
        ArrayList<Object> valueList = new ArrayList<Object>();
        Integer priceValueIndex = null;
        Integer amount = null;
        Savepoint savepoint = null;

        for (Entry<String, Object> field : fieldsToUpdate.entrySet()) {
            // Add comma when there are multiple fields present
            if (updateProductQuery.length() > 19) {
                updateProductQuery += ", ";
            }

            // Check different fields
            switch (field.getKey()) {
                case "name" :
                    updateProductQuery += "Name = ?";
                    valueList.add(field.getValue());
                    break;
                case "description":
                    updateProductQuery += "Description = ?";
                    valueList.add(field.getValue());
                    break;
                case "vendor":
                    updateProductQuery += "Vendor = ?";
                    valueList.add(field.getValue());
                    break;
                case "price":
                    priceValueIndex = valueList.size();
                    updateProductQuery += "Price = ?";
                    valueList.add(field.getValue());
                    break;
                case "image_path":
                    updateProductQuery += "ImagePath = ?";
                    valueList.add(field.getValue());
                    break;
                case "amount":
                    amount = Integer.parseInt(field.getValue().toString());
                    break;
            }
        }
        updateProductQuery += " WHERE ProductSKU = ?";

        try {
            // Open DB connection
            Class.forName("org.sqlite.JDBC");
            URL dbUrl = WarehouseRepository.class.getClassLoader().getResource("taptapgo.db");
            db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

            // Save checkpoint to rollback in case update fail
            db_conn.setAutoCommit(false);
            savepoint = db_conn.setSavepoint();

            // Load the values into prepared statement
            PreparedStatement pstmt = db_conn.prepareStatement(updateProductQuery);
            for (int i = 0; i < valueList.size(); i++) {
                if(priceValueIndex != null && priceValueIndex == i){
                    Float priceValue = Float.parseFloat(valueList.get(i).toString());
                    pstmt.setFloat((i+1), priceValue);
                } else {
                    String value = valueList.get(i).toString();
                    pstmt.setString((i+1), value);
                }
            }
            pstmt.setString(valueList.size()+1, productSKU);
            pstmt.executeUpdate();

            // Check if amount update is needed
            if (amount != null) {
                PreparedStatement pstmt2 = db_conn.prepareStatement("UPDATE warehouse SET Stock = ? WHERE ProductSKU = ?");
                pstmt2.setInt(1, amount);
                pstmt2.setString(2, productSKU);
                pstmt2.executeUpdate();
            }

            db_conn.commit();
            db_conn.setAutoCommit(true);
            db_conn.close();

            return true;

        } catch (SQLException e) {
            try {
                if (db_conn != null && savepoint != null) {
                    db_conn.rollback(savepoint); // Rollback the transaction if an exception occurs
                    db_conn.setAutoCommit(true);
                    db_conn.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return false;
        }
    } 
}
