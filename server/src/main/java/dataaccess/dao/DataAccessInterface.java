package dataaccess.dao;

import dataaccess.DataAccessException;

public interface DataAccessInterface<T> {
    //CRUD
    void clear();
    void create(String id, T type);
    T read(String id) throws DataAccessException;
    void update(String id, T type);
    void delete(String id);
}
