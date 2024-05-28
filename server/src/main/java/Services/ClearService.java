package Services;

//Imports
import dataaccess.DataAccessException;
import dataaccess.DAO.*;

public class ClearService {
    private final AuthDataAccess authData;
    private final GameDataAccess gameData;
    private final UserDataAccess userData;


    public ClearService(AuthDataAccess authData, GameDataAccess gameData, UserDataAccess userData) {
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

