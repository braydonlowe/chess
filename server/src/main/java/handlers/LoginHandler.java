package handlers;

//Imports
import model.Auth;
import dataaccess.DataAccessException;
import server.JsonUtil;
import spark.Request;
import spark.Response;
import model.User;
import service.LoginService;

public class LoginHandler {
    private final LoginService loginServ;

    public LoginHandler(LoginService loginService) {
        loginServ = loginService;
    }
    public Object login(Request req, Response res) {
        User currentUser = JsonUtil.fromJson(req.body(), User.class);
        //Try logging in
        try {
            Auth auth = loginServ.loginUser(currentUser);
            res.status(200);
            return JsonUtil.toJson(auth);
        } catch (DataAccessException e) {
            if (!(e.getMessage() == "unauthorized")) {
                res.status(500);
            }
            else {
                res.status(401);
            }
            return JsonUtil.toJson(new ErrorRespone("Error: " +  e.getMessage()));
        }

    }
}
