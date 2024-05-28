package dataaccess.DAO;

import Model.User;

import java.util.HashMap;
import java.util.Map;

public class AuthDataAccess {
    private static final Map<String, User> authTable = new HashMap<>();
    public static void clearGameData() {
        authTable.clear();
    }

}
