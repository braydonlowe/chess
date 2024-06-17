package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

public class UIUtils {
    private static final HashMap<ChessPiece.PieceType, String> WHITE_PIECES = new HashMap<>();
    private static final HashMap<ChessPiece.PieceType, String> BLACK_PIECES = new HashMap<>();

    static {
        WHITE_PIECES.put(ChessPiece.PieceType.KING, " ♔ ");
        WHITE_PIECES.put(ChessPiece.PieceType.QUEEN, " ♕ ");
        WHITE_PIECES.put(ChessPiece.PieceType.BISHOP, " ♗ ");
        WHITE_PIECES.put(ChessPiece.PieceType.KNIGHT, " ♘ ");
        WHITE_PIECES.put(ChessPiece.PieceType.ROOK, " ♖ ");
        WHITE_PIECES.put(ChessPiece.PieceType.PAWN, " ♙ ");

        BLACK_PIECES.put(ChessPiece.PieceType.KING, " ♚ ");
        BLACK_PIECES.put(ChessPiece.PieceType.QUEEN, " ♛ ");
        BLACK_PIECES.put(ChessPiece.PieceType.BISHOP, " ♝ ");
        BLACK_PIECES.put(ChessPiece.PieceType.KNIGHT, " ♞ ");
        BLACK_PIECES.put(ChessPiece.PieceType.ROOK, " ♜ ");
        BLACK_PIECES.put(ChessPiece.PieceType.PAWN, " ♟ ");
    }

    public static String getSymbol(ChessGame.TeamColor color, ChessPiece.PieceType type) {
        if (color == ChessGame.TeamColor.WHITE) {
            return WHITE_PIECES.get(type);
        }
        else {
            return BLACK_PIECES.get(type);
        }
    }

    private static void resetColors(PrintStream out) {
        out.print(EscapeSequences.RESET_BG_COLOR);
        out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private static void setColors(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_BOLD);
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }

    public static void clearTerminal(PrintStream out) {
        out.print("\033[H\033[2J"); //Still isn't working as I would like. I'll come back.
        out.flush();
    }


    private static void setMenu(PrintStream out, String[] options, boolean listOption) {
        for (String op : options) {
            int length = op.length();
            if (listOption) {
                out.print(" - " + op);
            }
            else {
                out.print(op);
                length-=3;
            }
            for (int i = length; i <= 50; i++) {
                out.print(" ");
            }
            resetColors(out);
            out.println();
            setColors(out);
        }
    }

    public static void printOneLiners(PrintStream out, String line) {
        String[] input1 = {line};
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        UIUtils.setMenu(out, input1, false);
        UIUtils.resetColors(out);
    }

    public static void printMenu(PrintStream outThing, String[] options, String menuLine) {
        UIUtils.setColors(outThing);
        UIUtils.printOneLiners(outThing, menuLine);
        UIUtils.setMenu(outThing, options, true);
        UIUtils.printOneLiners(outThing,"Please Type Selection:");
        UIUtils.resetColors(outThing);
    }
}
