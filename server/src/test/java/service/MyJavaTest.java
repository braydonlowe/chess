package service;


//Import
import dataaccess.dao.SQLAuthDataAccess;
import handlers.CreateGameRecord;
import handlers.ListOfGamesRecord;
import model.Auth;
import model.Game;
import model.User;
import chess.ChessGame;
import dataaccess.dao.AuthDataAccess;
import dataaccess.dao.GameDataAccess;
import dataaccess.dao.SQLUserDataAccess;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MyJavaTest {
    private SQLAuthDataAccess authData;
    private GameDataAccess gameData;
    private SQLUserDataAccess users;

    @BeforeEach
    void dataAccessReset() {
        authData = new AuthDataAccess();
        gameData = new GameDataAccess();
        users = new SQLUserDataAccess();
        authData.clear();
        gameData.clear();
        users.clear();
    }



    //Clear Database
    @Test
    void clearTest() throws DataAccessException {
        authData.create(authData.createAuth(), new Auth(authData.createAuth(), "GoodUserName"));
        gameData.create(authData.createAuth(), new Game(authData.createAuth(), "yourMom", "myMom", "TheEnd", new ChessGame()));
        users.create("yourMom", new User("yourMom", "1", "*"));
        users.create("myMom", new User("myMom", "2", "*"));

        ClearService sirCleansAlot = new ClearService(authData, gameData, users);
        sirCleansAlot.clearData();

        assertEquals(0, authData.size());
        assertEquals(0, gameData.size());
        assertEquals(0, users.size());

    }


    //CreateGameService

    @Test
    void createServiceTestPos() throws DataAccessException {
        assertEquals(0, gameData.size());
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));

        String gameName = "TheEnd";

        CreateGameService gameServ = new CreateGameService(authData, gameData, users);
        gameServ.createGame(auth, gameName);

        assertEquals(1, gameData.size());

    }

    @Test
    void createServiceTestNeg() throws DataAccessException {
        assertEquals(0, gameData.size());
        String auth = null;
        String gameName = "TheEnd";

        assertThrows(DataAccessException.class, () -> {
            CreateGameService gameServ = new CreateGameService(authData, gameData, users);
            gameServ.createGame(auth, gameName);
        });

    }

    //Join game
    @Test
    void joinGameServicePos() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));
        String gameName = "TheEnd";
        CreateGameService gameServ = new CreateGameService(authData, gameData, users);
        CreateGameRecord gameID = gameServ.createGame(auth, gameName);
        JoinGameService joinServ = new JoinGameService(authData, gameData, users);
        joinServ.joinGame(auth, gameID.gameID(), "WHITE");

        assertEquals("GoodUserName", gameData.read(gameID.gameID()).whiteUsername());
    }

    @Test
    void joinGameServiceNeg() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));

        users.create("BadUserName", new User("BadUserName", "1", "*"));
        String badAuth = authData.createAuth();

        String gameName = "TheEnd";
        CreateGameService gameServ = new CreateGameService(authData, gameData, users);
        CreateGameRecord gameID = gameServ.createGame(auth, gameName);
        JoinGameService joinServ = new JoinGameService(authData, gameData, users);

        joinServ.joinGame(auth, gameID.gameID(), "WHITE");

        assertThrows(DataAccessException.class, () -> {
            joinServ.joinGame(badAuth, gameID.gameID(), "WHITE");
        });
    }

    @Test
    void listGameServiceTestPos() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));
        users.create("BadUserName", new User("BadUserName", "1", "*"));
        ListGameService listGame = new ListGameService(authData, gameData, users);
        ListOfGamesRecord games = listGame.listGame(auth);
        assertNotNull(games);
    }

    @Test
    void listGamesServiceNeg() throws DataAccessException{
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));
        users.create("BadUserName", new User("BadUserName", "1", "*"));
        ListGameService listGame = new ListGameService(authData, gameData, users);
        assertThrows(DataAccessException.class, () -> {
            listGame.listGame(null);
        });
    }

    //Login Service
    //Make sure that auth token is generated each login
    @Test
    void loginServicePos() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));
        User currentUser = users.read("GoodUserName");

        LoginService loginServ = new LoginService(authData, gameData, users);

        assertNotEquals(auth, loginServ.loginUser(currentUser));
    }

    @Test
    void loginServiceNeg() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));

        LoginService loginServ = new LoginService(authData, gameData, users);
        assertThrows(NullPointerException.class, () -> {
            loginServ.loginUser(null);
        });
    }


    @Test
    void logoutTest() throws DataAccessException {
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));

        LogoutService logoutServ = new LogoutService(authData, gameData, users);
        logoutServ.logoutUser(auth);

        assertEquals(0, authData.size());
    }

    @Test
    void logoutTestNeg() throws DataAccessException {
        String auth = authData.createAuth();
        authData.create(auth, new Auth(auth, "GoodUserName"));

        LogoutService logoutServ = new LogoutService(authData, gameData, users);

        assertThrows(DataAccessException.class, () -> {
            logoutServ.logoutUser(null);
        });
    }

    @Test
    void registerTestPos() throws DataAccessException{
        RegistrationService regServ = new RegistrationService(authData, gameData, users);
        User newUser = new User("GoodUserName", "1", "*");
        regServ.createUser(newUser);
        assertEquals(1, users.size());
    }

    @Test
    void registerTestNeg() throws DataAccessException {
        RegistrationService regServ = new RegistrationService(authData, gameData, users);
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        User user = users.read("GoodUserName");
        assertThrows(DataAccessException.class, () -> {
            regServ.createUser(user);
        });


    }
}
