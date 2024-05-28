package Handlers;

//Imports
import spark.Request;
import spark.Response;
import Model.User;
import server.JsonUtil;
import Services.RegistrationService;

public class RegistrationHandler {
    private final RegistrationService regServ;

    public RegistrationHandler(RegistrationService registrationService) {
        regServ = registrationService;
    }

    public static Object registerUser(Request req, Response res) {
        User user = JsonUtil.fromJson(req.body(), User.class);
        //We are returning a line from the auth table.
        return null;
    }
}
