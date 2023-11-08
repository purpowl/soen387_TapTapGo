package com.taptapgo.repository;

import java.sql.*;

import com.taptapgo.Staff;
public class StaffRepository{
    private static Connection db_conn;

    public boolean create(Object staff) {
        if(!(staff instanceof Staff)){
            return false;
        }

        Staff staffToCreate = (Staff) staff;

        if (!((Staff) staff).getUserID().contains("s")) {
            return false;
        }

        String insertQuery = "INSERT INTO staff (StaffID, Username) VALUES (?, ?)";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(insertQuery);

            pstmt.setString(1, staffToCreate.getUserID());
            pstmt.setString(2, staffToCreate.getUserName());

            int result = pstmt.executeUpdate();

            db_conn.close();

            if (result == 1){
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public Object read(Object userID) {
        return true;
    }

    
    public boolean update(Object object) {
        return true;
    }

    
    public boolean delete(Object object) {
        return true;
    }

    public Integer readMaxID() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            String getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(StaffID, ' ', -1)), 2) FROM staff";

            Statement stmt = db_conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(getQuery);

            if (queryResult.next()) {
                return Integer.parseInt(queryResult.getString(1));
            } else {
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
