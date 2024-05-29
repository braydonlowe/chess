package handlers;

//Imports
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import service.CreateGameService;
import server.JsonUtil;

public class CreateGameHandler {
    private final CreateGameService gameServ;

    public CreateGameHandler(CreateGameService gameService) {
        gameServ = gameService;
    }

    public Object createGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        String gameName = req.body();
        try {
            CreateGameRecord gameID = gameServ.createGame(authToken, gameName);
            res.status(200);
            return JsonUtil.toJson(gameID);
        } catch(DataAccessException e) {
            if (e.getMessage() == "unauthorized") {
                res.status(401);
            }
            else if(e.getMessage() == "bad request") {
                res.status(400);
            }
            else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorRespone("Error: " + e.getMessage()));
        }

    }
}
