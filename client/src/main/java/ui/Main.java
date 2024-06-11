package ui;

import model.Auth;

public class Main {
    public static void main(String[] args) throws Exception {
        //Later I'll add something to this function to not run on local.
        Status.USER_STATUS status = Status.USER_STATUS.LOGGEDOUT;
        String localhost = "http://localhost:8080";
        ServerFacade facade = new ServerFacade(localhost);
        PreLoginUI login = new PreLoginUI();
        boolean mainToggleLoop = false;
        while (!mainToggleLoop) {
            mainToggleLoop = login.menuLoop(facade);
            Auth auth = login.getAuth();
            if (auth == null) {
                return;
            }
            status = Status.USER_STATUS.LOGGEDIN;
            PostLoginUI post = new PostLoginUI(auth);
            post.menuLoop(facade, auth);
            login.setAuthNull();
        }
    }
}
