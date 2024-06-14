package websocket;

import chess.ChessPosition;
import chess.InvalidMoveException;
import ui.UIUtils;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

public class GamePlay {
    //Set variables here
    private static final String[] menuOptions = {"HELP", "REDRAW CHESS BOARD", "LEAVE", "MAKEMOVE","RESIGN","HIGHLIGHT LEGAL MOVES"};
    private static final String[] helpOptions = {"HELP - you're doing it.", "REDRAW CHESS BOARD - redraws an updated board", "LEAVE - leaves the game", "MAKEMOVE - makes a move. Format: ","RESIGN","HIGHLIGHT LEGAL MOVES: give a piece, get shown moves."};

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
        int letterValid = lettersToInt.get(rowColumns[0]);
        //if not in map
        return null;
        //Else
        //Create & return a chess position.
    }

    public static boolean gamePlayLoop(Scanner scan, PrintStream out) {
        boolean inGameLoop = true;
        UIUtils.printOneLiners(out, "Game Options:");
        UIUtils.setMenu(out, menuOptions, true);
        while(inGameLoop) {
            String input = scan.nextLine();
            String inputSwitch = input.toUpperCase();
            switch (inputSwitch) {
                case "HELP":
                    help(out);
                    continue;
                case "LEAVE":
                    return true;
                case "MAKE MOVE":
                    makeMove(out, scan);
                    continue;
                case "RESIGN":
                    resign();
                    continue;
                case "HIGHLIGHT LEGAL MOVES":
                    legalMoves();
                    continue;
                default:
                    UIUtils.printOneLiners(out, "Please type an appropriate command.");
            }
        }
        return false; //I haven't thought this line through to be honest.
    }



    private static void help(PrintStream out) {
        UIUtils.printOneLiners(out,"Help menu");
        UIUtils.setMenu(out, helpOptions, true);
    }

    private static void makeMove(PrintStream out, Scanner scan) {
        //Found in ChessGame
        makeChessPositionFromInput(out, scan);
        //try {

        //} catch (InvalidMoveException e) {

        //}
    }
    private static void resign() {
        //I wonder if the game is deleted?
    }

    private static void legalMoves() {
        //Found in chess game.
    }
}
