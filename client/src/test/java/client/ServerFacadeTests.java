package client; //This is needed for the tests to pass. I'm not sure what I did wrong here.

import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.PreLoginUI;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static PreLoginUI login;

    private Auth auth;
    private User user;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("HTTP://localhost:" + port);
        login = new PreLoginUI();
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    @Tag("userRegister")
    void userRegister() throws Exception {
        user = new User("name","pass","email");
        auth = facade.register(user);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void clearDB() {
        server.userData.clear();
        server.authData.clear();
        server.gameData.clear();
        auth = null;
        user = null;
    }


    @Test
    public void clearDatabase() throws Exception {
        facade.clearData();
        assertEquals(0, server.userData.size());
    }

    @Test
    @Tag("userRegister")
    void registerUserPos() throws Exception {
        assertEquals(1,server.userData.size());
    }

    @Test
    void registerUserNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            facade.register(null);
        });
    }

    @Test
    @Tag("userRegister")
    void loginPos() throws Exception {
        facade.logout(auth.authToken());
        facade.login(user);
        //We need to add the status enum.
    }

    @Test
    void loginUserNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            facade.login(new User("happy","birthday","jim"));
        });
    }

    @Test
    @Tag("userRegister")
    void createGamePos() throws Exception {
        CreateGameRecord record = new CreateGameRecord("CoolGame");
        facade.createGame(auth.authToken(), record);
        assertEquals(1, server.gameData.size());
    }

    @Test
    void createGameNeg() throws Exception {
        assertThrows(Exception.class, () -> {
            facade.createGame(null, null);
        });
    }

    @Test
    @Tag("userRegister")
    void userLogoutPos() throws Exception {
        facade.logout(auth.authToken());
        //Again we need to check for status
    }

    @Test
    @Tag("userRegister")
    void userLogoutNeg() throws Exception {
        facade.logout(auth.authToken());
        assertThrows(Exception.class, () -> {
            facade.listGames(auth.authToken());
        });
    }



    @Test
    @Tag("userRegister")
    void listGames() throws Exception {
        CreateGameRecord record = new CreateGameRecord("CoolGame");
        facade.createGame(auth.authToken(), record);
        ListOfGamesRecord list = facade.listGames(auth.authToken());
        assertEquals(1, list.games().length);
    }

    @Test
    @Tag("userRegister")
    void listGamesNeg() throws Exception {
        CreateGameRecord record = new CreateGameRecord("CoolGame");
        facade.createGame(auth.authToken(), record);
        assertThrows(java.lang.Exception.class, () -> {
            ListOfGamesRecord list = facade.listGames(null);
        });

    }

    @Test
    @Tag("userRegister")
    void joinGameNeg() throws Exception {
        CreateGameRecord record = new CreateGameRecord("CoolGame");
        facade.createGame(auth.authToken(), record);
        JoinGameRecord record2 = new JoinGameRecord("CoolGame", "WHITE");
        assertThrows(java.lang.Exception.class, () -> {
            facade.joinGame(auth.authToken(), record2);
                });
    }

    @Test
    @Tag("userRegister")
    void joinGamePos() throws Exception {
        CreateGameRecord record = new CreateGameRecord("CoolGame");
        CreateGameRecord responseRecord = facade.createGame(auth.authToken(), record);
        JoinGameRecord gameRecord = new JoinGameRecord(responseRecord.gameID(), "WHITE");
        //If this doesn't throw an error we are in biz.
        facade.joinGame(auth.authToken(), gameRecord);
    }

}
