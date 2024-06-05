package service;

//Imports
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class ClearService {
    private final SQLAuthDataAccess authData;
    private final SQLGameDataAccess gameData;
    private final SQLUserDataAccess userData;


    public ClearService(SQLAuthDataAccess authData, SQLGameDataAccess gameData, SQLUserDataAccess userData) {
        this.authData = authData;
        this.gameData = gameData;
        this.userData = userData;
    }
    public void clearData() throws DataAccessException {
        authData.clear();
        gameData.clear();
        userData.clear();
    }
}

