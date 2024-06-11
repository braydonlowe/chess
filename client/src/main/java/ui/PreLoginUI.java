package ui;

import model.Auth;
import model.User;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class PreLoginUI {
    private PrintStream out;
    private static final String[] OPTIONS = {"HELP", "QUIT", "LOGIN", "REGISTER"};

    private Auth auth;

    private ServerFacade facade;
    public PreLoginUI() {
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void printMenu() {
        UIUtils.setColors(out);
        String[] menuTitle = {"Menu"};
        UIUtils.setMenu(out,menuTitle, false);
        UIUtils.setMenu(out, OPTIONS, true);
    }

    public void menuToInput() {
        printMenu();
        String[] input = {"Please type selection"};
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        UIUtils.setMenu(out, input, false);
        UIUtils.resetColors(out);
    }



    public boolean menuLoop(ServerFacade facade) {
        this.facade = facade;
        boolean stopLoop = false;
        Scanner scan = UIUtils.getInput();
        menuToInput();
        while (!stopLoop) {
            String line = scan.nextLine();
            String scannerInput = line.toUpperCase();
            switch (scannerInput) {
                case "HELP":
                    help();
                    continue;
                case "QUIT":
                    return true;
                case "LOGIN":
                    stopLoop = login(scan);
                    UIUtils.clearTerminal(out);
                    continue;
                case "REGISTER":
                    stopLoop = register(scan);
                    UIUtils.clearTerminal(out);
                    continue;
                default:
                    UIUtils.printOneLiners(out, "Please type an appropriate command.");
            }
            if (!stopLoop) {
                menuToInput();
            } else {
                return false;
            }
            return false;
        }
        return false;
    }

    public void help() {
        String[] title = {"Available options:"};
        String[] list = {"Help - you're doing it", "Quit - leave program", "Login - login user", "Register - register user"};
        UIUtils.setMenu(out, title, false);
        UIUtils.setMenu(out, list, true);
    }


    public boolean login(Scanner scan) {
        UIUtils.printOneLiners(out,"Username");
        String username = scan.nextLine();
        if (Objects.equals(username, "")) {
            return false;
        }
        UIUtils.printOneLiners(out,"Password");
        String password = scan.nextLine();
        if (Objects.equals(password, "")) {
            return false;
        }
        try {
            User user = new User(username, password, null);
            auth = facade.login(user);
            return true;
        } catch (Exception e) {
            UIUtils.printOneLiners(out, "Invalid login. Please try again.");
            return false;
        }
    }

    public boolean register(Scanner scan) {
        UIUtils.printOneLiners(out,"Please type your username");
        String valid1 = scan.nextLine();
        if (Objects.equals(valid1, "")) {
            return false;
        }
        UIUtils.printOneLiners(out,"Please type your password");
        String valid2 = scan.nextLine();
        if (Objects.equals(valid2, "")) {
            return false;
        }
        UIUtils.printOneLiners(out,"Please type your email");
        String valid3 = scan.nextLine();
        if (Objects.equals(valid3, "")) {
            return false;
        }
        try {
            User user = new User(valid1, valid2, valid3);
            facade.register(user);
            auth = facade.login(user);
            return true;
        } catch (Exception e) {
            UIUtils.printOneLiners(out,"Username already taken");
            return false;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuthNull() {
        auth = null;
    }

}
