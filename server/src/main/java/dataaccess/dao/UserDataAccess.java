package dataaccess.dao;


//Imports
import java.util.Map;
import java.util.HashMap;
import model.User;

public class UserDataAccess implements DataAccessInterface<User> {
    private static final Map<String, User> USERTABLE = new HashMap<>();
    private final String createSQL = """
            CREATE TABLE IF NOT EXISTS user (
            'userID' int NOT NULL AUTO_INCREMENT,
            'username' varchar(200) NOT NULL,
            'password' varchar(200) NOT NULL,
            'email" varChar(200) NOT Null
            UNIQUE KEY 'username_UNIQUE' ('username')
            )""";

    public void createTable() {

    }

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
