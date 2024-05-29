package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;
    private HashSet<ChessPosition> blackPosition;
    private HashSet<ChessPosition> whitePosition;
    private ChessPosition whiteKing;
    private ChessPosition blackKing;

    //We might need to add some dataset so that we can keep track of each players peices.

    public ChessBoard() {
        /**
         * A normal gameboard is 8 by 8.
         * However, the tests run it with a index starting at 1. So we have to make it 9 by 9.
         */
        board = new ChessPiece[9][9];
        blackPosition = new HashSet<>();
        whitePosition = new HashSet<>();
    }



    public HashSet<ChessPosition> getPositionColor(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return whitePosition;
        }
        return blackPosition;
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return whiteKing;
        }
        return blackKing;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // When we add a piece I want to keep track of a list of pieces of that color.
        if (piece != null) {
            ChessGame.TeamColor color = piece.getTeamColor();
            if (color == ChessGame.TeamColor.WHITE) {
                whitePosition.add(position);
            } else {
                blackPosition.add(position);
            }
            //I want to also keep track of where the king is:
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                if (color == ChessGame.TeamColor.WHITE) {
                    whiteKing = position;
                } else {
                    blackKing = position;
                }
            }
        }
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
        //IMPORTANT!! REMEMBER THAT THE TESTS ARE SET UP FOR A 1 BASED INDEX AND NOT 0!!
        //Reset board
        this.board = new ChessPiece[9][9];
        //Add white pawns
        for (int i = 1; i <= 8; i++) {
            this.addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        //Add black pawns
        for (int i = 1; i <= 8; i++) {
            this.addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        //Add back-row pieces.
        //White
        this.addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //Black which is a mirror image of white.
        this.addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ChessBoard newBoard = (ChessBoard) obj;
        //Check individual spaces:
        for (int y = 1; y <= 8; y ++) {
            for (int z = 1; z <= 8; z++) {
                if (board[y][z] == null && newBoard.board[y][z] == null) {
                    continue;
                }
                if (board[y][z] == null || newBoard.board[y][z] == null) {
                    return false;
                }

                if (!board[y][z].getPieceType().equals(newBoard.board[y][z].getPieceType())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.toString(board) +
                '}';
    }
}