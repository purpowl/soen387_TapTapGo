package com.taptapgo.repository;

import java.sql.ResultSet;

public class TestDriver {
    public static void main(String[] args) {
        ResultSet queryResult = Database.readQuery("select * from product");

        try {
            while(queryResult.next()){
                System.out.println(queryResult.getString(1));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
