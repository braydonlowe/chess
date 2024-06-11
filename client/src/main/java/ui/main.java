package ui;

import model.Auth;

public class main {
    public static void main(String[] args) throws Exception {
        //Later I'll add something to this function to not run on local.
        String localhost = "http://localhost:8080";
        ServerFacade facade = new ServerFacade(localhost);
        PreLoginUI login = new PreLoginUI();
        login.menuLoop(facade);
        Auth auth = login.getAuth();
        PostLoginUI post = new PostLoginUI(auth);

        post.menuLoop(facade,auth);
    }
}
