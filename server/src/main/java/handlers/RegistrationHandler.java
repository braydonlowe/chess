package handlers;

//Imports
import dataaccess.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;
import model.User;
import model.Auth;
import server.JsonUtil;
import service.RegistrationService;

public class RegistrationHandler {
    private final RegistrationService regServ;

    public RegistrationHandler(RegistrationService registrationService) {
        regServ = registrationService;
    }

    public Object registerUser(Request req, Response res) {
        User user = JsonUtil.fromJson(req.body(), User.class);
        if (user.password() == null) {
            res.status(400);
        } else {
            String betterPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            User hashedUser = new User(user.username(), betterPassword, user.email());
            //We are returning a line from the auth table.
            try {
                Auth auth = regServ.createUser(hashedUser);
                res.status(200);
                return JsonUtil.toJson(auth);
            } catch (DataAccessException e) {
                if (e.getMessage() == "bad request") {
                    res.status(400);
                } else if (e.getMessage() == "already taken") {
                    res.status(403);
                } else {
                    res.status(500);
                }
                return JsonUtil.toJson(new ErrorRespone("Error:" + e.getMessage()));
            }
        }
        return JsonUtil.toJson(new ErrorRespone("Error: " + "bad request"));
    }
}
