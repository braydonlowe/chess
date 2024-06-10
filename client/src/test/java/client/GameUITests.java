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
    void boarderPrintWhite() {
        ui.printBoardWhite(board);
    }
}
