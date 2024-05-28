package Handlers;

//Imports
import spark.Request;
import spark.Response;
import java.util.Map;
import dataaccess.DAO.*;
import server.JsonUtil;

public class ClearHandler {

    public static Object handleClear(Request req, Response res) {
        //Implement the clearing of the database.
        UserDataAccess.clearUserData();
        GameDataAccess.clearGameData();
        AuthDataAccess.clearAuthData();

        res.status(200);
        return JsonUtil.toJson(Map.of("message", "Database cleared"));
    }
}
