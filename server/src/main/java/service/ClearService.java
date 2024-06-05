package service;

//Imports
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class ClearService {
    private final AuthDataAccess authData;
    private final GameDataAccess gameData;
    private final SQLUserDataAccess userData;


    public ClearService(AuthDataAccess authData, GameDataAccess gameData, SQLUserDataAccess userData) {
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

