package chess;

import java.util.HashSet;

public class GameUtils {

    /*
       Takes in the color of a team and the board.
       Returns the moves associated to that team.
     */
    public static HashSet<ChessMove> teamsMoves(ChessGame.TeamColor color, ChessBoard board) {
        HashSet<ChessMove> allChessMoves = new HashSet<>();
        HashSet<ChessPosition> positions = board.getPositionColor(color);
        for (ChessPosition pos : positions) {
            allChessMoves.addAll(board.getPiece(pos).pieceMoves(board, pos));
        }
        return allChessMoves;
    }



    public static boolean moveChangeCheck(ChessMove move, ChessBoard board) {
        //Positions
        boolean inCheck = false;
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        //Grab pieces
        ChessPiece pieceInQuestion = board.getPiece(startPosition);
        ChessPiece capturedPiece = board.getPiece(endPosition);

        ChessGame.TeamColor color = pieceInQuestion.getTeamColor();
        ChessGame.TeamColor badColor;
        //Set the other team color:
        if (color == ChessGame.TeamColor.WHITE) {
            badColor = ChessGame.TeamColor.BLACK;
        }
        else {
            badColor = ChessGame.TeamColor.WHITE;
        }

        //Make the move
        board.addPiece(startPosition, null);
        board.getPositionColor(color).remove(startPosition);

        board.addPiece(endPosition, pieceInQuestion);
        if (capturedPiece != null) {
            board.getPositionColor(badColor).remove(endPosition);
        }

        //Check to see if in check
        HashSet<ChessMove> badGuyMoves = teamsMoves(badColor, board);

        for (ChessMove badmoves : badGuyMoves) {
            //Aparently we need to make sure that the king is even on the board.
            if (board.getKingPosition(color) == null) {
                break;
            }
            if (badmoves.getEndPosition().getRow() == board.getKingPosition(color).getRow() && badmoves.getEndPosition().getColumn() == board.getKingPosition(color).getColumn()) {
                inCheck = true;
            }
        }

        //Revert move
        board.addPiece(endPosition, null);
        board.getPositionColor(color).remove(endPosition);
        if (capturedPiece != null) {
            board.addPiece(endPosition, capturedPiece);
        }

        board.addPiece(startPosition, pieceInQuestion);
        return inCheck;
    }
}
