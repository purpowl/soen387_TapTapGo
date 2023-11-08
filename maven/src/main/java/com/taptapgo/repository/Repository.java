package com.taptapgo.repository;

public interface Repository {
    public boolean create(Object object);
    
    public Object read(Object object);
    
    public boolean update(Object object);

    public boolean delete(Object object);
}
