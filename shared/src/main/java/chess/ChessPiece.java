package chess;

import java.util.Collection;

//Imports
import static chess.MovesUtils.moveBishop;
import static chess.MovesUtils.moveKing;
import static chess.MovesUtils.moveKnight;
import static chess.MovesUtils.movePawn;
import static chess.MovesUtils.moveQueen;
import static chess.MovesUtils.moveRook;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessPiece {
    //Setting private variables
    private final PieceType piece_type;
    private final ChessGame.TeamColor piece_color; //I don't think that ChessGame.TeamColor is necesarry. Figure out a way to do without it without importing.

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.piece_type = type;
        this.piece_color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.piece_color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.piece_type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //Needed to get the ChessBoard finished.
        switch(piece_type) {
            case BISHOP:
                return moveBishop(board, myPosition);
            case KING:
                return moveKing(board, myPosition);
            case KNIGHT:
                return moveKnight(board, myPosition);
            case PAWN:
                return movePawn(board, myPosition);
            case QUEEN:
                return moveQueen(board, myPosition);
            case ROOK:
                return moveRook(board, myPosition);
            default:
                System.out.println("Case not implemented in switch statement");
                return null;
        }

    }
}
