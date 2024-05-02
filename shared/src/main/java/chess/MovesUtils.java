package chess;

//Imports
import java.util.Collection;
import java.util.HashSet;
import chess.ChessGame.*;

public class MovesUtils {

    static private int spaceClear(ChessBoard board, int row, int column, TeamColor peiceColor) {
        //Three means just no
        //Two means that we can capture but no more after
        //1 is clear.
        if (row > 8 || row < 1 || column > 8 || column < 1) {
            return 3;
        }
        ChessPosition spacePosition = new ChessPosition(row, column);
        ChessPiece space = board.getPiece(spacePosition);
        if (space != null && space.getTeamColor() != peiceColor) {
            return 2;
        }
        else if (space != null && space.getTeamColor() == peiceColor) {
            return 3;
        }
        return 1;

    }

    /*
    Let us write a recursive function here to check the desired direction recursively.
    Hopefully this will let us get rid of a bunch of our code.
    We need to take it a direction parameter. we'll do this by just taking in the increment in that direction.
    for example directly up will take in +1 column +0 row.
     */
    static private Collection<ChessMove> moveRecurse(ChessBoard board, ChessPosition pos, int rowInc, int colInc) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPiece piece = board.getPiece(pos);
        int rowAt = pos.getRow() + rowInc;
        int colAt = pos.getColumn() + colInc;

        while (0 < rowAt && rowAt < 9 && 0 < colAt && colAt < 9) {
            int space = spaceClear(board, rowAt, colAt, piece.getTeamColor());
            if (space != 3) {
                ChessPosition newPosition = new ChessPosition(rowAt,colAt);
                ChessMove newMove = new ChessMove(pos, newPosition, null);
                moves.add(newMove);
            }
            if (space == 2 || space == 3) {
                break;
            }
            rowAt += rowInc;
            colAt += colInc;
        }
        return moves;
    }

    static public Collection<ChessMove> moveBishop(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>(); //Parenthesis are needed to construct
        //Quad1
        moves.addAll(moveRecurse(board, myPosition, 1,1));
        //Quad2
        moves.addAll(moveRecurse(board, myPosition, 1,-1));
        //Quad3
        moves.addAll(moveRecurse(board, myPosition, -1,-1));
        //Quad4
        moves.addAll(moveRecurse(board, myPosition, -1,1));

        return moves;
    }

    static public Collection<ChessMove> moveKing(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();
        int[][] positions = {{1,-1},{1,0},{1,1},{0,-1},{0,1},{-1,-1},{-1,0},{-1,1}};
        for (int i = 0; i < positions.length; i++) {
            int space = spaceClear(board, row + positions[i][0],column + positions[i][1], pieceColor);
            if (space != 3) {
                //Except the king can only move into spaces that aren't into check....
                ChessPosition newPosition = new ChessPosition(row + positions[i][0], column + positions[i][1]);
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moves.add(newMove);
            }
        }
        return moves;
    }

    static public Collection<ChessMove> moveKnight(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();
        int[][] positions = {{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1},{-2,1},{1,2},{-1,2}};
        for (int i = 0; i < positions.length; i++) {
            int space = spaceClear(board, row + positions[i][0],column + positions[i][1], pieceColor);
            if (space != 3) {
                //Except the king can only move into spaces that aren't into check....
                ChessPosition newPosition = new ChessPosition(row + positions[i][0], column + positions[i][1]);
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moves.add(newMove);
            }
        }
        return moves;
    }

    static public Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();
        boolean blocked = false;
        boolean start = false;
        int[][] positions = {{1,0},{2,0},{1,-1},{1,1}};
        //Check to see if it's on the opening line.
        //Promotion logic. If a piece is on the back row then it has to be promoted. No need to look after color.
        ChessPiece.PieceType promo = null;
        if (row == 1 || row == 8) {
            //Just sample code because I don't want to do this now.
            promo = ChessPiece.PieceType.QUEEN;
        }
        if (row == 2) {
            start = true;
        }
        //Check to see if it's a black pawn.
        if (pieceColor == TeamColor.BLACK) {
            for (int i = 0; i < positions.length; i++) {
                positions[i][0] = -1 * positions[i][0];
            }
            if (row == 7) {
                start = true;
            }
        }
        //Forward
        int forward = spaceClear(board, row + positions[0][0], column + positions[0][1], pieceColor);
        if (forward == 1) {
            ChessPosition newPosition = new ChessPosition(row + positions[0][0], column + positions[0][1]);
            ChessMove newMove = new ChessMove(myPosition, newPosition, promo);
            moves.add(newMove);
        }
        else {
            blocked = true;
        }
        //Forward 2
        int forward2 = spaceClear(board, row + positions[1][0], column + positions[1][1], pieceColor);
        if (!blocked && (forward2 == 1) && start) {
            ChessPosition newPosition = new ChessPosition(row + positions[1][0], column + positions[1][1]);
            ChessMove newMove = new ChessMove(myPosition, newPosition, promo);
            moves.add(newMove);
        }
        //Attack right
        int attackRight = spaceClear(board, row + positions[2][0], column + positions[2][1], pieceColor);
        if (attackRight == 2) {
            ChessPosition newPosition = new ChessPosition(row + positions[2][0], column + positions[2][1]);
            ChessMove newMove = new ChessMove(myPosition, newPosition, promo);
            moves.add(newMove);
        }

        //Attack left
        int attackLeft = spaceClear(board, row + positions[3][0], column + positions[3][1], pieceColor);
        if (attackLeft == 2) {
            ChessPosition newPosition = new ChessPosition(row + positions[3][0], column + positions[3][1]);
            ChessMove newMove = new ChessMove(myPosition, newPosition, promo);
            moves.add(newMove);
        }
        return moves;
    }
}
