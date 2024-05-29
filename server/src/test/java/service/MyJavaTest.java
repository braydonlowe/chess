package service;


//Import
import Handlers.CreateGameRecord;
import Handlers.ListOfGamesRecord;
import Model.Auth;
import Model.Game;
import Model.User;
import chess.ChessGame;
import dataaccess.DAO.AuthDataAccess;
import dataaccess.DAO.GameDataAccess;
import dataaccess.DAO.UserDataAccess;
import dataaccess.DataAccessException;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyJavaTest {
    private AuthDataAccess bananaGrahms;
    private GameDataAccess juniorbaconCheese;
    private UserDataAccess users;

    @BeforeEach
    void dataAccessReset() {
        bananaGrahms = new AuthDataAccess();
        juniorbaconCheese = new GameDataAccess();
        users = new UserDataAccess();
    }

    //Clear Database
    @Test
    void clearTest() throws DataAccessException {
        bananaGrahms.create(bananaGrahms.createAuth(), new Auth(bananaGrahms.createAuth(), "GoodUserName"));
        juniorbaconCheese.create(bananaGrahms.createAuth(), new Game(bananaGrahms.createAuth(), "yourMom", "myMom", "TheEnd", new ChessGame()));
        users.create("yourMom", new User("yourMom", "1", "*"));
        users.create("myMom", new User("myMom", "2", "*"));

        ClearService sirCleansAlot = new ClearService(bananaGrahms, juniorbaconCheese, users);
        sirCleansAlot.clearData();

        assertEquals(0, bananaGrahms.size());
        assertEquals(0, juniorbaconCheese.size());
        assertEquals(0, users.size());

    }


    //CreateGameService

    @Test
    void createServiceTestPos() throws DataAccessException {
        assertEquals(0, juniorbaconCheese.size());
        String auth = bananaGrahms.createAuth();
        String gameName = "TheEnd";

        CreateGameService gameServ = new CreateGameService(bananaGrahms, juniorbaconCheese, users);
        gameServ.createGame(auth, gameName);

        assertEquals(1, juniorbaconCheese.size());

    }

    @Test
    void createServiceTestNeg() throws DataAccessException {
        assertEquals(0, juniorbaconCheese.size());
        String auth = null;
        String gameName = "TheEnd";

        assertThrows(DataAccessException.class, () -> {
            CreateGameService gameServ = new CreateGameService(bananaGrahms, juniorbaconCheese, users);
            gameServ.createGame(auth, gameName);
        });

    }

    //Join game
    @Test
    void joinGameServicePos() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = bananaGrahms.createAuth();
        bananaGrahms.create(auth, new Auth(auth, "GoodUserName"));
        String gameName = "TheEnd";
        CreateGameService gameServ = new CreateGameService(bananaGrahms, juniorbaconCheese, users);
        CreateGameRecord gameID = gameServ.createGame(auth, gameName);
        JoinGameService joinServ = new JoinGameService(bananaGrahms, juniorbaconCheese, users);
        joinServ.joinGame(auth, gameID.gameID(), "WHITE");

        assertEquals("GoodUserName", juniorbaconCheese.read(gameID.gameID()).whiteUsername());
    }

    @Test
    void joinGameServiceNeg() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = bananaGrahms.createAuth();
        bananaGrahms.create(auth, new Auth(auth, "GoodUserName"));

        users.create("BadUserName", new User("BadUserName", "1", "*"));
        String badAuth = bananaGrahms.createAuth();

        String gameName = "TheEnd";
        CreateGameService gameServ = new CreateGameService(bananaGrahms, juniorbaconCheese, users);
        CreateGameRecord gameID = gameServ.createGame(auth, gameName);
        JoinGameService joinServ = new JoinGameService(bananaGrahms, juniorbaconCheese, users);

        joinServ.joinGame(auth, gameID.gameID(), "WHITE");

        assertThrows(DataAccessException.class, () -> {
            joinServ.joinGame(badAuth, gameID.gameID(), "WHITE");
        });
    }

    @Test
    void listGameServiceTestPos() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = bananaGrahms.createAuth();
        bananaGrahms.create(auth, new Auth(auth, "GoodUserName"));
        users.create("BadUserName", new User("BadUserName", "1", "*"));
        ListGameService listGame = new ListGameService(bananaGrahms, juniorbaconCheese, users);
        ListOfGamesRecord games = listGame.listGame(auth);
        assertNotNull(games);
    }

    @Test
    void listGamesServiceNeg() throws DataAccessException{
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = bananaGrahms.createAuth();
        bananaGrahms.create(auth, new Auth(auth, "GoodUserName"));
        users.create("BadUserName", new User("BadUserName", "1", "*"));
        ListGameService listGame = new ListGameService(bananaGrahms, juniorbaconCheese, users);
        assertThrows(DataAccessException.class, () -> {
            listGame.listGame(null);
        });
    }

    //Login Service
    //Make sure that auth token is generated each login
    @Test
    void loginServicePos() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = bananaGrahms.createAuth();
        bananaGrahms.create(auth, new Auth(auth, "GoodUserName"));
        User currentUser = users.read("GoodUserName");

        LoginService loginServ = new LoginService(bananaGrahms, juniorbaconCheese, users);

        assertNotEquals(auth, loginServ.loginUser(currentUser));
    }

    @Test
    void loginServiceNeg() throws DataAccessException {
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        String auth = bananaGrahms.createAuth();
        bananaGrahms.create(auth, new Auth(auth, "GoodUserName"));

        LoginService loginServ = new LoginService(bananaGrahms, juniorbaconCheese, users);
        assertThrows(NullPointerException.class, () -> {
            loginServ.loginUser(null);
        });
    }


    @Test
    void logoutTest() throws DataAccessException {
        String auth = bananaGrahms.createAuth();
        bananaGrahms.create(auth, new Auth(auth, "GoodUserName"));

        LogoutService logoutServ = new LogoutService(bananaGrahms, juniorbaconCheese, users);
        logoutServ.logoutUser(auth);

        assertEquals(0, bananaGrahms.size());
    }

    @Test
    void logoutTestNeg() throws DataAccessException {
        String auth = bananaGrahms.createAuth();
        bananaGrahms.create(auth, new Auth(auth, "GoodUserName"));

        LogoutService logoutServ = new LogoutService(bananaGrahms, juniorbaconCheese, users);

        assertThrows(DataAccessException.class, () -> {
            logoutServ.logoutUser(null);
        });
    }

    @Test
    void registerTestPos() throws DataAccessException{
        RegistrationService regServ = new RegistrationService(bananaGrahms, juniorbaconCheese, users);
        User newUser = new User("GoodUserName", "1", "*");
        regServ.createUser(newUser);
        assertEquals(1, users.size());
    }

    @Test
    void registerTestNeg() throws DataAccessException {
        RegistrationService regServ = new RegistrationService(bananaGrahms, juniorbaconCheese, users);
        users.create("GoodUserName", new User("GoodUserName", "1", "*"));
        User user = users.read("GoodUserName");
        assertThrows(DataAccessException.class, () -> {
            regServ.createUser(user);
        });


    }
}
