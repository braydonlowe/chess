package dataaccess;

import chess.ChessGame;
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import dataaccess.sql.SQLUserDataAccess;
import model.Auth;
import model.Game;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
    private SQLAuthDataAccess authData;
    private SQLGameDataAccess gameData;
    private SQLUserDataAccess userData;

    @BeforeEach
    void startNewDAO() {
        authData = new SQLAuthDataAccess();
        gameData = new SQLGameDataAccess();
        userData = new SQLUserDataAccess();
    }

    @AfterEach
    void clearAllDAO() {
        authData.clear();
        gameData.clear();
        userData.clear();
    }

    //I wanted to make an overall test
    @Test
    void dataGood() {
        assertNotNull(gameData);
        assertNotNull(userData);
        assertNotNull(authData);
    }


    //Auth stuff
    @Test
    void authCreatePos() throws DataAccessException{
        String authToken = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "goodUsername"));
        assertEquals(1, authData.size());
    }

    @Test
    void authCreateNeg() throws DataAccessException {
        String authToken = authData.createAuth();
        authData.create(null, new Auth(authToken, "goodUsername"));
        assertEquals(0, authData.size());
    }

    @Test
    void authDataClear() throws DataAccessException{
        String authToken = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "goodUsername"));
        assertEquals(1, authData.size());
        authData.clear();
        assertEquals(0, authData.size());
    }


    @Test
    void authTokenReadPos() throws DataAccessException{
        String authToken = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "goodUsername"));
        Auth auth = authData.read(authToken);
        assertEquals("goodUsername", auth.username());
    }

    @Test
    void authTokenReadNeg() throws DataAccessException {
        String authToken = authData.createAuth();
        String authToken2 = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "thisUserName"));
        authData.create(authToken, new Auth(authToken2, "otherUsername"));
        Auth auth = authData.read(authToken2);
        assertNotEquals("thisUserName", auth.username());
    }

    @Test
    void authTokenDeletePos() throws DataAccessException{
        String authToken = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "goodUsername"));
        assertEquals(1, authData.size());
        authData.delete(authToken);
        assertEquals(0, authData.size());
    }

    @Test
    void authTokenDeleteNeg() throws DataAccessException {
        authData.delete("Doesn'tExist");
        assertEquals(0, authData.size());
    }
    //Users stuff

    @Test
    void userCreatePos() throws DataAccessException{
        userData.create("coolUser", new User("coolUser", "coolPassword", "coolEmail"));
        assertEquals(1, userData.size());
    }

    @Test
    void userCreateNeg() throws DataAccessException {
        assertThrows(NullPointerException.class, () -> {
            userData.create(null, null);
        });
    }

    @Test
    void userClearData() throws DataAccessException {
        userData.create("coolUser", new User("coolUser", "coolPassword", "coolEmail"));
        assertEquals(1, userData.size());
        userData.clear();
        assertEquals(0, userData.size());
    }

    @Test
    void userDataReadPos() throws DataAccessException {
        userData.create("coolUser", new User("coolUser", "coolPassword", "coolEmail"));
        assertEquals("coolEmail", userData.read("coolUser").email());
    }

    @Test
    void userDataReadNeg() throws DataAccessException {
        assertNull(userData.read(null));
    }

    //Game stuff
    @Test
    void gameDataCreatePos() throws DataAccessException{
        gameData.create("1234", new Game("1234", null, null, "NewGame", new ChessGame()));
        assertEquals(1, gameData.size());
    }

    @Test
    void gameDataCreateNeg() throws DataAccessException {
        assertThrows(NullPointerException.class, () -> {
            gameData.create(null, null);
        });
    }

    @Test
    void gameDataClear() throws DataAccessException {
        gameData.create("1234", new Game("1234", null, null, "NewGame", new ChessGame()));
        assertEquals(1, gameData.size());
        gameData.clear();
        assertEquals(0, gameData.size());
    }

    //We can definitly add more tests on the read part.
    @Test
    void readGamePos() throws DataAccessException {
        gameData.create("1234", new Game("1234", null, null, "NewGame", new ChessGame()));
        Game game = gameData.read("1234");
        assertEquals("NewGame", game.gameName());

    }

    @Test
    void readFullGame() throws DataAccessException {
        gameData.create("1234", new Game("1234", "newUsername", "blackUsername", "NewGame", new ChessGame()));
        Game game = gameData.read("1234");
        assertEquals("newUsername", game.whiteUsername());
        assertEquals("blackUsername", game.blackUsername());
    }

    @Test
    void readGameID() throws DataAccessException {
        gameData.create("1234", new Game("1234", "newUsername", "blackUsername", "NewGame", new ChessGame()));
        Game game = gameData.read("1234");
        assertEquals("1234", game.gameID());
    }

    @Test
    void readGameNeg() throws DataAccessException {
        assertThrows(NullPointerException.class, () -> {
            gameData.create(null, null);
        });
    }

    @Test
    void updateGameWhiteJoin() throws DataAccessException {
        gameData.create("1234", new Game("1234", null, null, "NewGame", new ChessGame()));
        gameData.update("1234", new Game("1234", "newUsername", null, "NewGame", new ChessGame()));
        assertEquals("newUsername", gameData.read("1234").whiteUsername());
    }

    @Test
    void updateGameBlackJoin() throws DataAccessException {
        gameData.create("1234", new Game("1234", null, null, "NewGame", new ChessGame()));
        gameData.update("1234", new Game("1234", "newUsername", "blackUsername", "NewGame", new ChessGame()));
        assertEquals("blackUsername", gameData.read("1234").blackUsername());
    }

    @Test
    void updateGameNeg() throws DataAccessException {
        assertThrows(NullPointerException.class, () -> {
            gameData.update(null, null);
        });

    }

    @Test
    void listGamePos() throws DataAccessException {
        gameData.create("1234", new Game("1234", null, null, "NewGame", new ChessGame()));
        Game[] gameList = gameData.listGames();
        assertEquals(1, gameList.length);
    }

    @Test
    void listGameNeg() throws DataAccessException {
        gameData.create(null , new Game("1234", null, null, "NewGame", new ChessGame()));
        Game[] gameList = gameData.listGames();
        assertEquals(0, gameData.size());
    }

}
