package dataaccess.dao;


//Imports
import java.util.Map;
import java.util.HashMap;
import model.User;

public class UserDataAccess implements DataAccessInterface<User> {
    private static final Map<String, User> USERTABLE = new HashMap<>();

    @Override
    public void clear() {
        USERTABLE.clear();
    }

    @Override
    public void create(String id, User user) {
        USERTABLE.put(id, user);
    }

    @Override
    public User read(String id) {
        return USERTABLE.get(id);
    }


    @Override
    public void update(String id, User user) {
        //I created this in two tables just in case we need to add some validatio here.
        create(id, user);
    }


    @Override
    public void delete(String id) {
        USERTABLE.remove(id);
    }

    public int size() {
        return USERTABLE.size();
    }
}
