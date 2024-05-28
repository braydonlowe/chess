package dataaccess.DAO;

public interface DataAccessInterface<T> {
    //CRUD
    void clear();
    void create(String ID, T type);
    T read(String ID);
    void update(String ID, T type);
    void delete(String ID);
}
