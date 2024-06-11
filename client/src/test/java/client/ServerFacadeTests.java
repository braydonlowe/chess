//package client; //This is needed for the tests to pass. I'm not sure what I did wrong here.

import model.Auth;
import model.CreateGameRecord;
import model.ListOfGamesRecord;
import model.User;
import org.junit.jupiter.api.*;
import server.Server;
import ui.PreLoginUI;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static PreLoginUI login;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("HTTP://localhost:" + port);
        login = new PreLoginUI();
        System.out.println("Started test HTTP server on " + port);
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
    }


    @Test
    public void clearDatabase() throws Exception {
        facade.clearData();
        assertEquals(0, server.userData.size());
    }

    @Test
    void menuInteractionPreLogin() {
        login.menuLoop(facade);
    }

    @Test
    void createGamePos() throws Exception {
        User user = new User("asdf","jkl;","1234");
        Auth auth = facade.register(user);
        CreateGameRecord record = new CreateGameRecord("CoolGame");
        facade.createGame(auth.authToken(), record);
        assertEquals(1, server.gameData.size());
    }

    @Test
    void listGames() throws Exception {
        User user = new User("asdf","jkl;","1234");
        Auth auth = facade.register(user);
        CreateGameRecord record = new CreateGameRecord("CoolGame");
        facade.createGame(auth.authToken(), record);
        ListOfGamesRecord list = facade.listGames(auth.authToken());
        assertEquals(1, list.games().length);
    }

}
