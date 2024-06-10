package client;

import chess.ChessBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.GameplayUI;
import chess.ChessGame;

public class GameUITests {
    private ChessGame game;
    private ChessBoard board;
    private GameplayUI ui;

    @BeforeEach
    void getChessBoard() {
        game = new ChessGame();
        board = game.getBoard();
        ui = new GameplayUI();

    }

    @Test
    void printWhite() {
        ui.printBoardWhite(board);
    }

    @Test
    void printBlack() {
        ui.printBoardBlack(board);
    }


    @Test
    void printBoth() {
        ui.printBoardBlack(board);
        System.out.println();
        System.out.println();
        ui.printBoardWhite(board);
    }
}
