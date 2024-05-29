package Handlers;


//Imports
import dataaccess.DataAccessException;
import Services.LogoutService;
import server.JsonUtil;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    public final LogoutService logServ;


    public LogoutHandler(LogoutService logoutService) {
        logServ = logoutService;
    }

    public Object logout(Request req, Response res) throws DataAccessException{

        try {
            String authToken = req.headers("authorization");
            logServ.logoutUser(authToken);
            res.status(200);
            return "{}";
        }
        catch (DataAccessException e) {
            if (e.getMessage() == "unauthorized") {
                res.status(401);
            }
            else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorRespone("Error:" + e.getMessage()));
        }
    }
}
