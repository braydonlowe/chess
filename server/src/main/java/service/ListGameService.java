package service;

//Imports
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import dataaccess.sql.SQLUserDataAccess;
import model.Game;
import model.Auth;
import model.ListOfGamesRecord;
import dataaccess.DataAccessException;

public class ListGameService {
    private final SQLAuthDataAccess authData;
    //I don't think it needs game data
    private final SQLGameDataAccess gameData;
    private final SQLUserDataAccess userData;


    public ListGameService(SQLAuthDataAccess authData, SQLGameDataAccess gameData, SQLUserDataAccess userData) {
        this.authData = authData;
        this.gameData = gameData;
        this.userData = userData;
    }
    public ListOfGamesRecord listGame(String authToken) throws DataAccessException {
        Auth currentAuth = authData.read(authToken);
        if (currentAuth == null) {
            throw new DataAccessException("unauthorized");
        }
        Game[] gameList = gameData.listGames();
        return new ListOfGamesRecord(gameList);
    }
}

