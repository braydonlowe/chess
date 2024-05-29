package server;


//Imports
import spark.*;
import Handlers.*;
import dataaccess.DAO.*;
import Services.*;

public class Server {
    //Private variables

    private final ClearService clearService;
    private final RegistrationService regService;
    private final LoginService loginService;


    public Server() {
        UserDataAccess userData = new UserDataAccess();
        GameDataAccess gameData = new GameDataAccess();
        AuthDataAccess authData = new AuthDataAccess();

        //Instantiate the services with the data.
        regService = new RegistrationService(authData, gameData, userData);
        clearService = new ClearService(authData, gameData, userData);
        loginService = new LoginService(authData, gameData, userData);


    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new RegistrationHandler(regService).registerUser(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler(loginService).login(req, res));
        Spark.delete("/session", LogoutHandler::logout);
        Spark.get("/game", ListGameHandler::listGames);
        Spark.post("/game", CreateGameHandler::createGame);
        Spark.put("/game", JoinGameHandler::joinGame);
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
