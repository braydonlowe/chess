package ui;

import chess.ChessBoard;
import model.*;
import websocket.GamePlay;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class PostLoginUI {

    private PrintStream outThing;
    private static final String[] OPTIONS = {"HELP", "LOGOUT", "CREATE GAME", "PLAY GAME", "LIST GAMES", "OBSERVE GAME"};

    private ServerFacade facade;

    public HashMap<String, Game> listOfgames;
    private GameplayUI ui;

    public PostLoginUI(Auth auth) {
        this.outThing = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        listOfgames = new HashMap<>();
        ui = new GameplayUI();
        try {
            ListOfGamesRecord gameList = facade.listGames(auth.authToken());
            Game[] games = gameList.games();
            int counter = 1;
            for (Game game : games) {
                listOfgames.put(String.valueOf(counter), game);
                counter++;
            }
        } catch (Exception exe) {
            UIUtils.printOneLiners(outThing,"Welcome to the jungle!");
        }
    }
    public void printMenu() {
        UIUtils.setColors(outThing);
        String[] menuTitle = {"Menu"};
        UIUtils.setMenu(outThing,menuTitle, false);
        UIUtils.setMenu(outThing, OPTIONS, true);
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
                    break;
                case "PLAY GAME":
                    playGame(scan, auth);
                    break;
                case "LIST GAMES":
                    listGames(auth);
                    break;
                case "OBSERVE GAME":
                    observeGame(scan);
                    break;
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
            facade.createGame(auth.authToken(), record);
            UIUtils.printOneLiners(outThing,"Game created");
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
        UIUtils.printOneLiners(outThing,"Enter game number:");
        String gameID = scan.nextLine();
        if (Objects.equals(gameID, "")) {
            return false;
        }
        UIUtils.printOneLiners(outThing,"Color of choice");
        String playerColor = scan.nextLine();
        playerColor = playerColor.toUpperCase();
        if ((playerColor == "WHITE") || (playerColor == "BLACK")) {
            return false;
        }

        try {
            //Instead here I think we call the gameplay class....
            Game theGame = listOfgames.get(gameID);
            gameID = theGame.gameID();
            JoinGameRecord record = new JoinGameRecord(gameID, playerColor);
            facade.joinGame(auth.authToken(), record);
            UIUtils.printOneLiners(outThing,"Game successfully joined");
            if ((playerColor.equals("WHITE"))) {
                ui.printBoardWhite(theGame.game().getBoard());
            }
            else {
                ui.printBoardBlack(theGame.game().getBoard());
            }
            //This is where we go into gameplay.
            GamePlay.gamePlayLoop(scan, outThing);
            menuToInput();
            return true;
        } catch (Exception e) {
            UIUtils.printOneLiners(outThing,"Invalid input. Please try again.");
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
                UIUtils.printOneLiners(outThing,String.valueOf(counter) + ": " + game.gameName() + " Player 1: " + game.whiteUsername() + " Player 2: " + game.blackUsername());
                counter++;
            }
        } catch (Exception exe) {
            UIUtils.printOneLiners(outThing,"Unable to list games.");
        }
    }

    public void observeGame(Scanner scan) {
        UIUtils.printOneLiners(outThing,"Enter game number:");
        String gameID = scan.nextLine();
        if (Objects.equals(gameID, "")) {
            return;
        }
        try {
            Game game = listOfgames.get(gameID);
            ChessBoard board = game.game().getBoard();
            UIUtils.printOneLiners(outThing,"Now observing...");
            UIUtils.printOneLiners(outThing,"From White's Perspective");
            ui.printBoardWhite(game.game().getBoard());
            UIUtils.printOneLiners(outThing,"From Black's Perspective");
            ui.printBoardBlack(game.game().getBoard());
        } catch (Exception exe) {
        }
    }
}
