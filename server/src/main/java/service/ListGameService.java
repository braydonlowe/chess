package service;

//Imports
import Model.Game;
import Model.Auth;
import Handlers.ListOfGamesRecord;
import dataaccess.DataAccessException;
import dataaccess.DAO.*;

public class ListGameService {
    private final AuthDataAccess authData;
    //I don't think it needs game data
    private final GameDataAccess gameData;
    private final UserDataAccess userData;


    public ListGameService(AuthDataAccess authData, GameDataAccess gameData, UserDataAccess userData) {
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

