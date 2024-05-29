package Handlers;

//Imports
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import Model.User;
import Model.Auth;
import server.JsonUtil;
import Services.RegistrationService;
import Handlers.ErrorRespone;

public class RegistrationHandler {
    private final RegistrationService regServ;

    public RegistrationHandler(RegistrationService registrationService) {
        regServ = registrationService;
    }

    public Object registerUser(Request req, Response res) {
        User user = JsonUtil.fromJson(req.body(), User.class);
        //We are returning a line from the auth table.
        try {
            Auth auth = regServ.createUser(user);
            res.status(200);
            return JsonUtil.toJson(auth);
        } catch (DataAccessException e) {
            if (e.getMessage() == "bad request") {
                res.status(400);
            }
            else if (e.getMessage() == "already taken") {
                res.status(403);
            }
            else {
                res.status(500);
            }
            return JsonUtil.toJson(new ErrorRespone("Error:" + e.getMessage()));
        }
    }
}
