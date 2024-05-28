package Handlers;

//Imports
import spark.Request;
import spark.Response;
import java.util.Map;
import dataaccess.DAO.UserDataAccess;
import server.JsonUtil;

public class ClearHandler {

    public static Object handleClear(Request req, Response res) {
        //Implement the clearing of the database.
        UserDataAccess.clearUserData();



        res.status(200);

        return JsonUtil.toJson(Map.of("message", "Database cleared"));
    }
}
