package Handlers;

//Imports
import Model.Auth;
import dataaccess.DataAccessException;
import server.JsonUtil;
import spark.Request;
import spark.Response;
import Model.User;
import Services.LoginService;

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
            if (e.getMessage() == "User not found" || e.getMessage() == "Incorrect password") {
                res.status(400);
            }
            else {
                res.status(500);
            }
            throw new RuntimeException(e);
        }

    }
}
