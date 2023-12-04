//package com.taptapgo.repository;
//
//import com.taptapgo.Order;
//
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Savepoint;
//import java.util.ArrayList;
//
//
//
//public class TestDriver {
//    public static void main(String[] args) {
//        String updateQuery = "UPDATE registeredUser SET LastName = ? WHERE UserID = ?";
//        Savepoint savepoint = null;
//        Connection db_conn = null;
//
//        try {
//            // Open DB connection
//            Class.forName("org.sqlite.JDBC");
//            db_conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/taptapgo.db");
//
//            // Save checkpoint to rollback in case update fail
//            db_conn.setAutoCommit(false);
//            savepoint = db_conn.setSavepoint();
//
//            // Load the values into prepared statement
//            PreparedStatement pstmt = db_conn.prepareStatement(updateQuery);
//            pstmt.setString(1, "Smith");
//            pstmt.setString(2, "rc00001");
//            int result = pstmt.executeUpdate();
//            if (result < 1) {
//                throw new SQLException("User information update failed!");
//            }
//
//            db_conn.commit();
//            db_conn.setAutoCommit(true);
//            db_conn.close();
//
//        } catch (SQLException e) {
//            try {
//                if (db_conn != null && savepoint != null) {
//                    db_conn.rollback(savepoint); // Rollback the transaction if an exception occurs
//                    db_conn.setAutoCommit(true);
//                    db_conn.close();
//                }
//
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}
