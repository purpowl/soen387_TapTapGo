package com.taptapgo.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Date;

import com.taptapgo.Customer;
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

        String insertQuery = "INSERT INTO staff (StaffID) VALUES (?)";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(insertQuery);

            pstmt.setString(1, staffToCreate.getUserID());

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
}
