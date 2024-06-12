package websocket;

import ui.UIUtils;

import java.io.PrintStream;
import java.util.Scanner;

public class GamePlay {
    //Set variables here
    private static final String[] menuOptions = {"HELP", "REDRAW CHESS BOARD", "LEAVE", "MAKEMOVE","RESIGN","HIGHLIGHT LEGAL MOVES"};


    public static boolean gamePlayLoop(Scanner scan, PrintStream out) {
        boolean inGameLoop = true;
        while(inGameLoop) {
            String input = scan.nextLine();
            String inputSwitch = input.toUpperCase();
            switch (inputSwitch) {
                case "HELP":
                    help();
                    continue;
                case "LEAVE":
                    return true;
                case "MAKE MOVE":
                    makeMove();
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



    private static void help() {
        return;
    }

    private static void makeMove() {

    }
    private static void resign() {

    }

    private static void legalMoves() {

    }
}
