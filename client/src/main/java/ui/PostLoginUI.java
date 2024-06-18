package ui;

import chess.ChessBoard;
import model.*;
import websocket.GamePlay;
import websocket.WebSocket;

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

    private WebSocket socket;

    public PostLoginUI(Auth auth, WebSocket socket) {
        this.socket = socket;
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

    public void menuToInput() {
        UIUtils.printMenu(outThing, OPTIONS, "Menu");
    }

    public void menuLoop(ServerFacade facade, Auth auth) throws Exception {
        this.facade = facade;
        boolean toggle = false;
        Scanner scan = new Scanner(System.in);
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
        String title = "Available options:";
        String[] list = {"Help - you're doing it", "Logout - leave program", "Create game - creates game", "Play game - joins game", "Observe Game - observes game"};
        UIUtils.printMenu(outThing, list, title);
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
            GamePlay.gamePlayLoop(scan, outThing, playerColor, theGame, auth, this.socket, gameID);
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
                UIUtils.printOneLiners(outThing,String.valueOf(counter) + ": " + game.gameName() + " White: " + game.whiteUsername() + " Black: " + game.blackUsername());
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
