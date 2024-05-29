package Handlers;

//Imports
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import service.ListGameService;
import server.JsonUtil;

public class ListGameHandler {
    private final ListGameService listServ;

    public ListGameHandler(ListGameService listService) {
        listServ = listService;
    }

    public Object listGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        try {
            ListOfGamesRecord listRecord = listServ.listGame(authToken);
            res.status(200);
            return JsonUtil.toJson(listRecord);
        } catch(DataAccessException e) {
            if (e.getMessage() == "unauthorized") {
                res.status(401);
            }
            else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorRespone("Error: " + e.getMessage()));
        }

    }
}
