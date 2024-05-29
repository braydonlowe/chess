package dataaccess.dao;

public interface DataAccessInterface<T> {
    //CRUD
    void clear();
    void create(String id, T type);
    T read(String id);
    void update(String id, T type);
    void delete(String id);
}
