package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;

    //We might need to add some dataset so that we can keep track of each players peices.

    public ChessBoard() {
        board = new ChessPiece[8][8]; //The game board is an 8 by 8 collection of pieces.
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //We might have to add a check to make sure we can add the piece here.
        //If and when we do use getPiece to implement it.
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Reset board
        this.board = new ChessPiece[8][8];

        //Add pawns
        //for(int i = 0; i < board.length; i++) {
        //    this.addPiece();
        //}

        //Add pieces.
    }
}
