package com.taptapgo.repository;

public class WarehouseRepository implements Repository {
    public WarehouseRepository(){

    }

    @Override
    public boolean create(Object object) {
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
