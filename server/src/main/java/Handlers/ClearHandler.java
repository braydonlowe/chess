package Handlers;

//Imports
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import java.util.Map;
import Services.ClearService;
import server.JsonUtil;

public class ClearHandler {

    private final ClearService clearServ;

    public ClearHandler(ClearService clearService) {
        clearServ = clearService;
    }

    public Object handleClear(Request req, Response res) throws DataAccessException {
        clearServ.clearData();
        res.status(200);
        return JsonUtil.toJson(Map.of("message", "Database cleared"));
    }
}
