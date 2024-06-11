package ui;

import model.Auth;
import model.User;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class PreLoginUI {
    private PrintStream out;
    private static final String[] options = {"HELP", "QUIT", "LOGIN", "REGISTER"};

    private ServerFacade facade;
    public PreLoginUI() {
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void printMenu() {
        UIUtils.setColors(out);
        String[] menuTitle = {"Menu"};
        UIUtils.setMenu(out,menuTitle, false);
        UIUtils.setMenu(out, options, true);
    }

    public void menuToInput() {
        printMenu();
        String[] input = {"Please type selection"};
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        UIUtils.setMenu(out, input, false);
        UIUtils.resetColors(out);
    }

    public Scanner getInput() {
        return new Scanner(System.in);
    }

    public void printOneLiners(String line) {
        String[] input1 = {line};
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        UIUtils.setMenu(out, input1, false);
        UIUtils.resetColors(out);
    }

    public void menuLoop(ServerFacade facade) {
        this.facade = facade;
        boolean toggle = false;
        Scanner scan = getInput();
        menuToInput();
        while (!toggle) {
            String line = scan.nextLine();
            String scannerInput = line.toUpperCase();
            switch (scannerInput) {
                case "HELP":
                    help();
                    continue;
                case "QUIT":
                    toggle = true;
                    continue;
                case "LOGIN":
                    toggle = login(scan);
                    if (!toggle) {
                        menuToInput();
                    }
                    continue;
                case "REGISTER":
                    toggle = register(scan);
                    if (!toggle) {
                        menuToInput();
                    }
                    continue;
                default:
                    printOneLiners("Please type an appropriate command.");
                }
            }
        }

    public void help() {
        String[] title = {"Available options:"};
        String[] list = {"Help - you're doing it", "Quit - leave program", "Login - login user", "Register - register user"};
        UIUtils.setMenu(out, title, false);
        UIUtils.setMenu(out, list, true);
    }


    public boolean login(Scanner scan) {
        printOneLiners("Username");
        String username = scan.nextLine();
        if (Objects.equals(username, "")) {
            return false;
        }
        printOneLiners("Password");
        String password = scan.nextLine();
        if (Objects.equals(password, "")) {
            return false;
        }
        try {
            User user = new User(username, password, null);
            facade.login(user);
            return true;
        } catch (Exception e) {
            printOneLiners("Invalid login. Please try again.");
            return false;
        }
    }

    public boolean register(Scanner scan) {
        printOneLiners("Please type your username");
        String valid1 = scan.nextLine();
        if (Objects.equals(valid1, "")) {
            return false;
        }
        printOneLiners("Please type your password");
        String valid2 = scan.nextLine();
        if (Objects.equals(valid2, "")) {
            return false;
        }
        printOneLiners("Please type your email");
        String valid3 = scan.nextLine();
        if (Objects.equals(valid3, "")) {
            return false;
        }
        try {
            User user = new User(valid1, valid2, valid3);
            facade.register(user);
            facade.login(user);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
