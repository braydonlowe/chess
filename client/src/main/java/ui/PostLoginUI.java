package ui;

import model.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import ui.*;

public class PostLoginUI {

    private PrintStream outThing;
    private static final String[] options = {"HELP", "LOGOUT", "CREATE GAME", "PLAY GAME", "LIST GAMES", "OBSERVE GAME"};

    private ServerFacade facade;

    public HashMap<String, Game> listOfgames;
    private GameplayUI ui;

    public PostLoginUI(Auth auth) {
        this.outThing = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        listOfgames = new HashMap<>();
        try {
            ListOfGamesRecord gameList = facade.listGames(auth.authToken());
            Game[] games = gameList.games();
            int counter = 1;
            for (Game game : games) {
                listOfgames.put(String.valueOf(counter), game);
                counter++;
            }
        } catch (Exception exe) {
            UIUtils.printOneLiners(outThing,"Unable to list games.");
        }
    }
    public void printMenu() {
        UIUtils.setColors(outThing);
        String[] menuTitle = {"Menu"};
        UIUtils.setMenu(outThing,menuTitle, false);
        UIUtils.setMenu(outThing, options, true);
    }

    public void menuToInput() {
        printMenu();
        String[] input = {"Please type selection"};
        outThing.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        UIUtils.setMenu(outThing, input, false);
        UIUtils.resetColors(outThing);
    }

    public void menuLoop(ServerFacade facade, Auth auth) throws Exception {
        this.facade = facade;
        boolean toggle = false;
        Scanner scan = UIUtils.getInput();
        menuToInput();
        while (!toggle) {
            String line = scan.nextLine();
            String scannerInput = line.toUpperCase();
            switch (scannerInput) {
                case "HELP":
                    help();
                    continue;
                case "LOGOUT":
                    toggle = logout(auth);
                    continue;
                case "CREATE GAME":
                    createGame(scan, auth);
                    continue;
                case "PLAY GAME":
                    toggle = playGame(scan, auth);
                case "LIST GAMES":
                    listGames(auth);
                    if (!toggle) {
                        menuToInput();
                    }
                    continue;
                case "OBSERVE GAME":
                    observeGame(scan);
                default:
                    UIUtils.printOneLiners(outThing, "Please type an appropriate command.");
            }
        }
    }

    public void help() {
        String[] title = {"Available options:"};
        String[] list = {"Help - you're doing it", "Logout - leave program", "Create game - creates game", "Play game - joins game", "Observe Game - observes game"};
        UIUtils.setMenu(outThing, title, false);
        UIUtils.setMenu(outThing, list, true);
    }


    public boolean createGame(Scanner scan, Auth auth) {
        UIUtils.printOneLiners(outThing,"Game Name");
        String gameName = scan.nextLine();
        if (Objects.equals(gameName, "")) {
            return false;
        }
        try {
            CreateGameRecord record = new CreateGameRecord(gameName);
            CreateGameRecord otherRecord = facade.createGame(auth.authToken(), record);

            return true;
        } catch (Exception e) {
            UIUtils.printOneLiners(outThing,"Invalid login. Please try again.");
            return false;
        }
    }

    public boolean logout(Auth auth) {
        try {
            facade.logout(auth.authToken());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean playGame(Scanner scan, Auth auth) {
        UIUtils.printOneLiners(outThing,"Game name");
        String gameID = scan.nextLine();
        if (Objects.equals(gameID, "")) {
            return false;
        }
        UIUtils.printOneLiners(outThing,"Color of choice");
        String playerColor = scan.nextLine();
        playerColor = playerColor.toUpperCase();
        if (Objects.equals(playerColor, "WHITE") || Objects.equals(playerColor, "BLACK")) {
            return false;
        }

        try {
            Game theGame = listOfgames.get(gameID);
            gameID = theGame.gameID();
            JoinGameRecord record = new JoinGameRecord(gameID, playerColor);
            facade.joinGame(auth.authToken(), record);
            return true;
        } catch (Exception e) {
            UIUtils.printOneLiners(outThing,"Invalid game. Please try again.");
            return false;
        }
    }

    public void listGames(Auth auth) throws Exception {
        listOfgames.clear();
        try {
            ListOfGamesRecord gameList = facade.listGames(auth.authToken());
            Game[] games = gameList.games();
            int counter = 1;
            for (Game game : games) {
                listOfgames.put(String.valueOf(counter), game);
                UIUtils.printOneLiners(outThing,String.valueOf(counter) + ": " + game.gameName());
                counter++;
            }
        } catch (Exception exe) {
            UIUtils.printOneLiners(outThing,"Welcome to the Jungle!");
        }
    }

    public boolean observeGame(Scanner scan) {
        UIUtils.printOneLiners(outThing,"GameID");
        String gameID = scan.nextLine();
        if (Objects.equals(gameID, "")) {
            return false;
        }
        try {
            Game game = listOfgames.get(gameID);
            ui.printBoardWhite(game.game().getBoard());
            ui.printBoardBlack(game.game().getBoard());
            return true;
        } catch (Exception exe) {
            return false;
        }
    }
}
