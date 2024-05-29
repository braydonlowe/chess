package Handlers;

//Imports
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import Services.JoinGameService;
import server.JsonUtil;

public class JoinGameHandler {
    private final JoinGameService joinServ;

    public JoinGameHandler(JoinGameService joinService) {
        joinServ = joinService;
    }

    public Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        String gameName = req.body();
        try {
            CreateGameRecord gameID = joinServ.joinGame(authToken, gameName);
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
