package service;

//Imports
import handlers.CreateGameRecord;
import model.Auth;
import model.Game;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class CreateGameService {
    private final SQLAuthDataAccess authData;
    //I don't think it needs game data
    private final GameDataAccess gameData;
    private final SQLUserDataAccess userData;


    public CreateGameService(SQLAuthDataAccess authData, GameDataAccess gameData, SQLUserDataAccess userData) {
        this.authData = authData;
        this.gameData = gameData;
        this.userData = userData;
    }
    public CreateGameRecord createGame(String authToken, String gameName) throws DataAccessException {
        Auth currentAuth = authData.read(authToken);
        if (currentAuth == null) {
            throw new DataAccessException("unauthorized");
        }
        if(gameName == null) {
            throw new DataAccessException("bad request");
        }
        String gameID = authData.createAuth();
        gameData.create(gameID, new Game(gameID, null, null, gameName, new ChessGame()));
        return new CreateGameRecord(gameID);
    }
}

