package com.taptapgo.repository;

import com.mysql.jdbc.Connection;
import com.taptapgo.Order;

public class OrderRepository implements Repository{
    private static Connection db_conn;

    @Override
    public boolean create(Object order) {
        if(!(order instanceof Order)){
            return false;
        }

        Order orderToCreate = (Order) order;

        
        return true;
    }

    @Override
    public Object read(Object object) {
        return null;
    }

    @Override
    public boolean update(Object object) {
        return true;
    }

    @Override
    public boolean delete(Object object) {
        return true;
    }
}
