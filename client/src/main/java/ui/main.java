package ui;

public class main {
    public static void main(String[] args) {
        //Later I'll add something to this function to not run on local.
        String localhost = "http://localhost:8081";
        ServerFacade facade = new ServerFacade(localhost);

    }
}
