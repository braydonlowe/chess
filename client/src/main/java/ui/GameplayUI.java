package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class GameplayUI {
    private PrintStream out;

    private String background = EscapeSequences.SET_BG_COLOR_BLACK;
    private String textColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
    private String pieceCharacter = "";
    private static final String EMPTY_SPACE = " " + "\u200A" + "  " + "\u2009";
    /*
     * I think what I want to do is pass in a gameboard, and have this print it out.
     */
    public GameplayUI() {
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    //WhiteStuff
    private static final String BORDER_CHARS_ROW_WHITE[] = {"A","B","C","D","E","F","G","H"};
    private static final String BORDER_CHARS_COL_WHITE[] = {"8","7","6","5","4","3","2","1"};

    //BlackStuff
    private static final String BORDER_CHARS_ROW_BLACK[] = {"H","G","F","E","D","C","B","A"};
    private static final String BORDER_CHARS_COL_BLACK[] = {"1","2","3","4","5","6","7","8"};

    public void printBoardWhite(ChessBoard board) {
        boolean endline = false;
        printBoarderRow(BORDER_CHARS_ROW_WHITE);
        background = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        for (int row = 8; row >= 1; row--) {
            background = switchColor(background);
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            printSpace(EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_BG_COLOR_DARK_GREY, " " + BORDER_CHARS_COL_WHITE[row-1] + " ", false);
            for (int column = 1; column <= 8; column++) {
                setColors(board, row, column);
                printSpace(textColor, background, pieceCharacter , endline);
            }
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            printEndRow(row, 1);
        }
        printBoarderRow(BORDER_CHARS_ROW_WHITE);
    }

    public void printBoardBlack(ChessBoard board) {
        boolean endline = false;
        printBoarderRow(BORDER_CHARS_ROW_BLACK);
        background = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        for (int row = 1; row <= 8; row++) {
            background = switchColor(background);
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            printSpace(EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_BG_COLOR_DARK_GREY, " " + BORDER_CHARS_COL_BLACK[row-1] + " ", false);
            for (int column = 8; column >= 1; column--) {
                setColors(board, row, column);
                printSpace(textColor, background, pieceCharacter , endline);
            }
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            printEndRow(row, 8);
        }
        printBoarderRow(BORDER_CHARS_ROW_WHITE);
    }

    private void setColors(ChessBoard board, int row, int column) {
        background = switchColor(background);
        ChessPiece piece = board.getPiece(new ChessPosition(row, column));
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                textColor = EscapeSequences.SET_TEXT_COLOR_WHITE;
                pieceCharacter = UIUtils.getSymbol(ChessGame.TeamColor.WHITE, piece.getPieceType());
            }
            else {
                textColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
                pieceCharacter = UIUtils.getSymbol(ChessGame.TeamColor.BLACK, piece.getPieceType());
            }
        }
        else {
            pieceCharacter = EMPTY_SPACE;//EscapeSequences.WHITE_PAWN;
            if (background.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)) {
                textColor = EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
            }
            else {
                textColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
            }
        }
    }

    private void printEndRow(int row, int endVar) {
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        if (row == endVar) {
            printSpace(EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_BG_COLOR_DARK_GREY, " " + BORDER_CHARS_COL_BLACK[row-1] + " ", true);
        }
        else {
            printSpace(EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_BG_COLOR_DARK_GREY, " " + BORDER_CHARS_COL_WHITE[row-1] + " ", true);
        }
    }

    public void printBoarderRow(String[] border) { //Will be a good idea to move this to private
        boolean isEnd = false;
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE); //We have to make sure the board stays uniform
        printSpace(EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_BG_COLOR_DARK_GREY, "   ", isEnd);
        for (int i = 0; i < border.length; i++) {
            if (i == border.length-1) {
                isEnd = true;
            }
            printSpace(EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_BG_COLOR_DARK_GREY, " " + "\u200A" +border[i] + " " + "\u2009" , false);
        }
        printSpace(EscapeSequences.SET_TEXT_BOLD , EscapeSequences.SET_BG_COLOR_DARK_GREY, "   ", isEnd); // Setting the empty space at the beginning

    }

    public void printSpace(String text, String background,String contents, boolean isEnd) {
        out.print(background);
        out.print(text);
        out.print(contents);
        if (isEnd) {
            UIUtils.resetColors(out);
            out.println();
        }
    }

    public String switchColor(String color) {
        if (color.equals(EscapeSequences.SET_BG_COLOR_BLACK)) {
            return EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        }
        return EscapeSequences.SET_BG_COLOR_BLACK;
    }
}
