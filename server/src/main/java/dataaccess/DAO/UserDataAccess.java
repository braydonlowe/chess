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
}
