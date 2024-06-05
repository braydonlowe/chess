package service;

//Imports
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class LogoutService {
    private final AuthDataAccess authData;
    //I don't think it needs game data
    private final GameDataAccess gameData;
    private final SQLUserDataAccess userData;


    public LogoutService(AuthDataAccess authData, GameDataAccess gameData, SQLUserDataAccess userData) {
        this.authData = authData;
        this.gameData = gameData;
        this.userData = userData;
    }
    public void logoutUser(String authToken) throws DataAccessException {
        //Check to see if the user is in the database if not throw a 403 error
        if (authData.read(authToken) == null) {
            throw new DataAccessException("unauthorized");
        }
        authData.delete(authToken);
    }
}

