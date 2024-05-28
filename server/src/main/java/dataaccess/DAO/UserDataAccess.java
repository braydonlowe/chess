package dataaccess.DAO;


//Imports
import java.util.Map;
import java.util.HashMap;
import Model.User;

public class UserDataAccess {
    private static final Map<String, User> userTable = new HashMap<>();

    public static void clearUserData() {
        userTable.clear();
    }

    //Create
    public static void createUser(String ID, User user) {
        userTable.put(ID, user);
    }

    //Read
    public static User readUser(String ID) {
        return userTable.get(ID);
    }

    //Update
    public static void updateUser(String ID, User user) {
        //I created this in two tables just in case we need to add some validatio here.
        createUser(ID, user);
    }

    //Delete
    public static void deleteUser(String ID) {
        userTable.remove(ID);
    }
}
