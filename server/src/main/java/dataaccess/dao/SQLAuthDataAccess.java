package dataaccess.dao;

import model.Auth;

import java.util.HashMap;
import java.util.Map;

public class SQLAuthDataAccess {
    private static final Map<String, Auth> AUTHTABLE = new HashMap<>();
    private int authNumber = 0;
    public void clear() {
        AUTHTABLE.clear();
    }

    //Create
    public void create(String id, Auth auth) {
        AUTHTABLE.put(id, auth);
    }


    //Read
    public Auth read(String id) {
        return AUTHTABLE.get(id);
    }


    //Update
    //This isn't right yet but we'll come back to this one on each DB.
    public void update(String id, Auth newAuth) {
        create(id, newAuth);
    }

    //Delete
    public void delete(String id) {
        AUTHTABLE.remove(id);
    }


    public String createAuth() {
        authNumber++;
        return Integer.toString(authNumber);
    }

    public int size() {
        return AUTHTABLE.size();
    }
}
