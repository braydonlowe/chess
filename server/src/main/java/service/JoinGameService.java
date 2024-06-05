package service;

//Imports
import model.Auth;
import model.Game;
import model.User;
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class JoinGameService {
    private final AuthDataAccess authData;
    //I don't think it needs game data
    private final GameDataAccess gameData;
    private final SQLUserDataAccess userData;


    public JoinGameService(AuthDataAccess authData, GameDataAccess gameData, SQLUserDataAccess userData) {
        this.authData = authData;
        this.gameData = gameData;
        this.userData = userData;
    }
    public void joinGame(String authToken, String gameID, String playerColor) throws DataAccessException {
        Auth currentAuth = authData.read(authToken);
        if (currentAuth == null) {
            throw new DataAccessException("unauthorized");
        }
        if (gameID == "0" || gameID == null || playerColor == null) {
            throw new DataAccessException("bad request");
        }
        Game gameToBeJoined = gameData.read(gameID);
        User currentUser = userData.read(currentAuth.username());
        if (playerColor.equals("WHITE")) {
            if (gameToBeJoined.whiteUsername() == null) {
                Game updatedGame = new Game(gameID, currentUser.username(), gameToBeJoined.blackUsername(), gameToBeJoined.gameName(), gameToBeJoined.game());
                gameData.update(gameID, updatedGame);
            } else {
                throw new DataAccessException("color already taken");
            }
        } else if (gameToBeJoined.blackUsername() == null) {
            Game updatedGame = new Game(gameID, gameToBeJoined.whiteUsername(), currentUser.username(), gameToBeJoined.gameName(), gameToBeJoined.game());
            gameData.update(gameID, updatedGame);
        } else {
            throw new DataAccessException("color already taken");
        }
    }
}

