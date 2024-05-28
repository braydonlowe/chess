package dataaccess.DAO;

import Model.Auth;

import java.util.HashMap;
import java.util.Map;

public class AuthDataAccess {
    private static final Map<String, Auth> authTable = new HashMap<>();
    public static void clearAuthData() {
        authTable.clear();
    }

    //Create
    public static void createAuth(String ID, Auth auth) {
        authTable.put(ID, auth);
    }


    //Read
    public static Auth readAuth(String ID) {
        return authTable.get(ID);
    }


    //Update
    //This isn't right yet but we'll come back to this one on each DB.
    public static void updateAuth(String ID, Auth newAuth) {
        createAuth(ID, newAuth);
    }

    //Delete
    public static void deleteAuth(String authUniqueID) {
        authTable.remove(authUniqueID);
    }

}
