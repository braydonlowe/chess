package service;

//Imports
import model.Game;
import model.Auth;
import handlers.ListOfGamesRecord;
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class ListGameService {
    private final AuthDataAccess authData;
    //I don't think it needs game data
    private final GameDataAccess gameData;
    private final SQLUserDataAccess userData;


    public ListGameService(AuthDataAccess authData, GameDataAccess gameData, SQLUserDataAccess userData) {
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

