package Handlers;

//Imports
import spark.Request;
import spark.Response;
import Model.User;
import server.JsonUtil;

public class RegistrationHandler {
    public static Object registerUser(Request req, Response res) {
        User user = JsonUtil.fromJson(req.body(), User.class);

        try {
            res.status(200);
            user =
        }
        //We are returning a line from the auth table.

    }
}
