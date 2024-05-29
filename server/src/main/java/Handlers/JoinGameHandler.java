package Handlers;

//Imports
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import service.JoinGameService;
import server.JsonUtil;

public class JoinGameHandler {
    private final JoinGameService joinServ;

    public JoinGameHandler(JoinGameService joinService) {
        joinServ = joinService;
    }

    public Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        JoinGameRecord gameInformation = JsonUtil.fromJson(req.body(), JoinGameRecord.class);
        try {
            joinServ.joinGame(authToken, gameInformation.gameID(), gameInformation.playerColor());
            res.status(200);
            return "{}";
        } catch(DataAccessException e) {
            if (e.getMessage() == "unauthorized") {
                res.status(401);
            }
            else if(e.getMessage() == "bad request") {
                res.status(400);
            }
            else if(e.getMessage() == "color already taken") {
                res.status(403);
            }
            else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorRespone("Error: " + e.getMessage()));
        }

    }
}
