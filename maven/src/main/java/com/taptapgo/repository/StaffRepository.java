package com.taptapgo.repository;

import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

import com.taptapgo.Customer;
import com.taptapgo.Staff;
import org.json.JSONArray;
import org.json.JSONObject;

public class StaffRepository{
    private static Connection db_conn;

    public boolean createStaffInDatabase(Staff staff) {

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

    public static Staff readByUsername(String username) {

        String getQuery = "SELECT UserID, Username FROM registereduser WHERE Username = ?";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");

            PreparedStatement pstmt = db_conn.prepareStatement(getQuery);
            pstmt.setString(1, username);

            ResultSet queryResult = pstmt.executeQuery();

            if (queryResult.next()) {
                String id = queryResult.getString(1);
                String passwordDB = "";
                String content = "";

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream is = classLoader.getResourceAsStream("/credentials.json");

                assert is != null;
                Scanner reader = new Scanner(is, "UTF-8");

                //Scanner reader = new Scanner(new File("/credentials.json"));
                while (reader.hasNextLine()) {
                    content += reader.nextLine() + "\n";
                }
                reader.close();

                if(!content.isEmpty()) {
                    JSONArray usersJsonArray = new JSONArray(content);
                    for (int i = 0; i < usersJsonArray.length(); i++) {
                        JSONObject userObj = usersJsonArray.getJSONObject(i);
                        String usernameInFile = userObj.getString("username");
                        if (usernameInFile.equals(username)) {
                            passwordDB = userObj.getString("password");
                            break;
                        }
                    }
                }

                queryResult.close();
                db_conn.close();

                if (passwordDB.isEmpty()) {
                    return null;
                }
                return new Staff(id, username, passwordDB);
            }
            else {
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public boolean update(Object object) {
        return true;
    }

    
    public boolean delete(Object object) {
        return true;
    }

//    public static Integer getMaxID() {
//
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//
//            db_conn = DriverManager.getConnection("jdbc:mysql://taptapgo.mysql.database.azure.com:3306/taptapgo?characterEncoding=UTF-8", "soen387_taptapgo", "T@pT@pG0387");
//
//            String getQuery = "SELECT SUBSTRING(MAX(SUBSTRING_INDEX(StaffID, ' ', -1)), 2) FROM registereduser WHERE Staff = 1";
//
//            Statement stmt = db_conn.createStatement();
//            ResultSet queryResult = stmt.executeQuery(getQuery);
//
//            if (queryResult.next()) {
//                return Integer.parseInt(queryResult.getString(1));
//            } else {
//                return 0;
//            }
//
//        } catch(Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
}
