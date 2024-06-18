package websocket;

import chess.*;
import model.Auth;
import model.Game;
import ui.GameplayUI;
import ui.UIUtils;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import com.google.gson.Gson;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class GamePlay {
    //Set variables here
    private static final String[] menuOptions = {"HELP", "REDRAW CHESS BOARD", "LEAVE", "MAKE MOVE","RESIGN","HIGHLIGHT LEGAL MOVES"};
    private static final String[] helpOptions = {"HELP - you're doing it.", "REDRAW CHESS BOARD - redraws an updated board", "LEAVE - leaves the game", "MAKE MOVE - makes a move.","RESIGN - Forfeit the game.","HIGHLIGHT LEGAL MOVES: give a piece, get shown moves."};

    private static final GameplayUI ui = new GameplayUI();

    private PrintStream out;


    private static final HashMap<String, Integer> lettersToInt = new HashMap<>();
    static {
        lettersToInt.put("A",1);
        lettersToInt.put("B",2);
        lettersToInt.put("C",3);
        lettersToInt.put("D",4);
        lettersToInt.put("E",5);
        lettersToInt.put("F",6);
        lettersToInt.put("G",7);
        lettersToInt.put("H",8);
    }
    private static ChessPosition makeChessPositionFromInput(PrintStream out, Scanner scan) {
        //Found in chess game.
        UIUtils.printOneLiners(out, "Enter position in format <row> <column>:");
        String rowColumn = scan.nextLine();
        String[] rowColumns = rowColumn.split(" ");
        //find it in the map
        if (rowColumns.length != 2) {
            return null;
        }
        String columnCap = rowColumns[0].toUpperCase();
        int columnNumber = lettersToInt.get(columnCap);
        //if it's not in the map it'll throw an exception
        return new ChessPosition(Integer.valueOf(rowColumns[0]), columnNumber);
    }

    public static boolean gamePlayLoop(Scanner scan, PrintStream out, String teamColorString, Game theGame, Auth auth, WebSocket socket, String gameID) throws Exception {
        boolean inGameLoop = true;
        UIUtils.printMenu(out, menuOptions, "Game Options");
        while(inGameLoop) {
            String input = scan.nextLine();
            String inputSwitch = input.toUpperCase();
            switch (inputSwitch) {
                case "HELP":
                    help(out);
                    continue;
                case "REDRAW CHESS BOARD":
                    redraw(teamColorString, theGame.game().getBoard());
                    continue;
                case "LEAVE":
                    boolean willLeave = leave(out, auth, socket);
                    if (willLeave) {
                        return true;
                    }
                    continue;
                case "MAKE MOVE":
                    makeMove(out, scan, auth, socket, gameID);
                    continue;
                case "RESIGN":
                    resign(out, auth, socket);
                    continue;
                case "HIGHLIGHT LEGAL MOVES":
                    legalMoves(out, scan, theGame);
                    continue;
                default:
                    UIUtils.printOneLiners(out, "Please type an appropriate command.");
            }
        }
        return false; //I haven't thought this line through to be honest.
    }



    private static void help(PrintStream out) {
        UIUtils.printMenu(out, helpOptions, "Help menu");
    }

    private static void redraw(String teamColor, ChessBoard board) {
        if (teamColor.equals("WHITE")) {
            ui.printBoardWhite(board);
        }
        else {
            ui.printBoardBlack(board);
        }

    }

    private static void makeMove(PrintStream out, Scanner scan, Auth auth, WebSocket socket, String gameID) {
        try {
            UIUtils.printOneLiners(out, "Select peice to move:");
            ChessPosition start = makeChessPositionFromInput(out, scan);
            UIUtils.printOneLiners(out, "Select ending position for piece:");
            ChessPosition end = makeChessPositionFromInput(out, scan);
            ChessMove move = new ChessMove(start, end, null);
            sendMoveToSocket(socket, auth.authToken(), gameID, move);
        } catch (Exception e) {
            UIUtils.printOneLiners(out, "Input position invalid.");
        }
        //Choose a place to put it.
    }

    private static boolean leave(PrintStream out, Auth auth, WebSocket socket) throws Exception {
        try {
            sendToSocket(auth.authToken(), socket, UserGameCommand.CommandType.LEAVE);
            return true;
        } catch (Exception e) {
            UIUtils.printOneLiners(out,"Unable to leave game. Prisoner count++.");
            return false;
        }
    }


    private static void resign(PrintStream out, Auth auth, WebSocket socket) {
        try {
            sendToSocket(auth.authToken(), socket, UserGameCommand.CommandType.RESIGN);
        } catch (Exception e) {
            UIUtils.printOneLiners(out, "Unable to resign. You are trapped");
        }
    }

    private static void legalMoves(PrintStream out, Scanner scan, Game game) {
        ChessPosition peicePos = makeChessPositionFromInput(out, scan);
        Collection<ChessMove> moves = game.game().validMoves(peicePos);


    }


    private static void sendToSocket(String authToken, WebSocket socket, UserGameCommand.CommandType type) throws Exception {
        UserGameCommand command = new UserGameCommand(authToken);
        command.setType(type);
        socket.send(new Gson().toJson(command));
    }

    private static void sendMoveToSocket(WebSocket socket, String authToken, String gameID, ChessMove move) throws Exception {
        MakeMove moveCommand = new MakeMove(authToken, gameID, move);
        moveCommand.setType(UserGameCommand.CommandType.MAKE_MOVE);
        socket.send(new Gson().toJson(moveCommand));
    }
}
