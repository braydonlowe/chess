package dataaccess.DAO;


//Imports
import java.util.Map;
import java.util.HashMap;
import Model.User;

public class UserDataAccess implements DataAccessInterface<User> {
    private static final Map<String, User> userTable = new HashMap<>();

    @Override
    public void clear() {
        userTable.clear();
    }

    @Override
    public void create(String ID, User user) {
        userTable.put(ID, user);
    }

    @Override
    public User read(String ID) {
        return userTable.get(ID);
    }


    @Override
    public void update(String ID, User user) {
        //I created this in two tables just in case we need to add some validatio here.
        create(ID, user);
    }


    @Override
    public void delete(String ID) {
        userTable.remove(ID);
    }

    public int size() {
        return userTable.size();
    }
}
