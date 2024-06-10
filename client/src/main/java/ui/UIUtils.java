package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.util.HashMap;

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
}
