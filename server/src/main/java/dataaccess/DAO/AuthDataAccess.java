package dataaccess.DAO;

import Model.Auth;
import java.util.HashMap;
import java.util.Map;

public class AuthDataAccess implements DataAccessInterface<Auth> {
    private static final Map<String, Auth> authTable = new HashMap<>();
    public void clear() {
        authTable.clear();
    }

    //Create
    public void create(String ID, Auth auth) {
        authTable.put(ID, auth);
    }


    //Read
    public Auth read(String ID) {
        return authTable.get(ID);
    }


    //Update
    //This isn't right yet but we'll come back to this one on each DB.
    public void update(String ID, Auth newAuth) {
        create(ID, newAuth);
    }

    //Delete
    public void delete(String authUniqueID) {
        authTable.remove(authUniqueID);
    }

}
