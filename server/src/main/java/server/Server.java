package server;


//Imports
import dataaccess.DataAccessException;
import spark.*;
import handlers.*;
import dataaccess.dao.*;
import service.*;
import dataaccess.DatabaseManager;

import java.sql.SQLException;

public class Server {
    //Private variables
    public final DatabaseManager dbManager;
    public final SQLUserDataAccess userData;
    public final SQLAuthDataAccess authData;
    public final SQLGameDataAccess gameData;
    private final ClearService clearService;
    private final RegistrationService regService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CreateGameService createService;
    private final JoinGameService joinService;
    private final ListGameService listService;


    public Server() {
        //Database
        dbManager = new DatabaseManager();

        //DAO's
        userData = new SQLUserDataAccess();
        authData = new SQLAuthDataAccess();
        gameData = new SQLGameDataAccess();

        //Old DAO's
        //UserDataAccess userData = new UserDataAccess();
        //GameDataAccess gameData = new GameDataAccess();
        //AuthDataAccess authData = new AuthDataAccess();

        //Instantiate the services with the data.
        regService = new RegistrationService(authData, gameData, userData);
        clearService = new ClearService(authData, gameData, userData);
        loginService = new LoginService(authData, gameData, userData);
        logoutService = new LogoutService(authData, gameData, userData);
        createService = new CreateGameService(authData, gameData, userData);
        joinService = new JoinGameService(authData, gameData, userData);
        listService = new ListGameService(authData, gameData, userData);


    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            dbManager.createDatabase();
            userData.createTable();
            authData.createTable();
            gameData.createTable();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 1;
        }

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new RegistrationHandler(regService).registerUser(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler(loginService).login(req, res));
        Spark.delete("/session", (req, res) -> new LogoutHandler(logoutService).logout(req, res));
        Spark.get("/game", (req, res) -> new ListGameHandler(listService).listGame(req, res));
        Spark.post("/game", (req, res) -> new CreateGameHandler(createService).createGame(req, res));
        Spark.put("/game", (req, res) -> new JoinGameHandler(joinService).joinGame(req, res));
        Spark.delete("/db", (req, res) -> new ClearHandler(clearService).handleClear(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
    }
}
