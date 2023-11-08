package com.taptapgo.repository;

import java.sql.*;

import javax.naming.spi.DirStateFactory.Result;

public class Database {
    private static Connection database_conn = null;

    private Database() {

    }

    public static synchronized ResultSet readQuery(String query) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            database_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");
            Statement stmt = database_conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
            
            database_conn.close();
            
            return rs;
        } catch(Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    public static synchronized boolean commitQuery(String query) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            database_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");
            Statement stmt = database_conn.createStatement();
            int result = stmt.executeUpdate(query);
            
            database_conn.close();

            if (result == 1)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}
