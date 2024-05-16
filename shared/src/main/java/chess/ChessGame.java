package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turnColor = TeamColor.WHITE;
    private ChessBoard board;



    public ChessGame() {
        //setting the gameboard at its initial gamestate.
        this.board = new ChessBoard();
        //We need to set the board as a reset board but it's interesting., we can't just set it equal to a reset board here.
        resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //We also need to consider if a move will put into check or checkmate.
        return board.getPiece(startPosition).pieceMoves(board, startPosition);
    }


    public boolean goodMove() {
        return true;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Retrieve the piece at start position
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();

        // Chess Piece information
        ChessPiece piece = this.board.getPiece(startPos);
        ChessGame.TeamColor color = piece.getTeamColor();

        //Check for a promotion piece:
        if(move.getPromotionPiece() != null) {
            ChessPiece.PieceType type = move.getPromotionPiece();
            piece = new ChessPiece(color, type);
        }

        // Remove the start position from the board and collection where we keep the color positions.
        this.board.addPiece(startPos, null);
        this.board.getPositionColor(color).remove(startPos);

        // Create piece at end position
        this.board.addPiece(endPos, piece);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPosition> opositePositions;
        // Retrieve peices of the opposite color:
        if (teamColor == TeamColor.WHITE) {
            opositePositions = board.getPositionColor(TeamColor.WHITE);
        }
        else {
            opositePositions = board.getPositionColor(TeamColor.BLACK);
        }
        //Retrieve the position of the king.
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        return opositePositions.contains(kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Used in the constructor. Calls the resetBoard function in board.
     */
    public void resetBoard() {
        this.board.resetBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ChessGame gameObject = (ChessGame)obj;
        return this.board.equals(gameObject.board);

    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turnColor);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "turnColor=" + turnColor +
                ", board=" + board +
                '}';
    }

}




