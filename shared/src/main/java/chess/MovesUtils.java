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
        for (int[] position : positions) {
            int space = spaceClear(board, row + position[0], column + position[1], pieceColor);
            if (space != 3) {
                //Except the king can only move into spaces that aren't into check....
                ChessPosition newPosition = new ChessPosition(row + position[0], column + position[1]);
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
        boolean start = false;
        int[][] forwardPositions = {{1,0},{2,0}};
        int[][] attackPositions = {{1,-1},{1,1}};
        ChessPiece.PieceType[] promotions = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
        //Check to see if it's on the opening line.
        if (row == 2) {
            start = true;
        }
        //Check to see if it's a black pawn, if it is also change the opening line.
        if (pieceColor == TeamColor.BLACK) {
            for (int i = 0; i < forwardPositions.length; i++) {
                forwardPositions[i][0] = -1 * forwardPositions[i][0];
                attackPositions[i][0] = -1 * attackPositions[i][0];
            }
            if (row == 7) {
                start = true;
            }
        }
        //Forward
        for (int i = 0; i < forwardPositions.length; i++) {
            int forward = spaceClear(board, row + forwardPositions[i][0], column + forwardPositions[i][1], pieceColor);
            int endPosition = row + forwardPositions[i][0];
            if (forward == 1) {
                if (endPosition != 8 && endPosition != 1) {
                    ChessPosition newPosition = new ChessPosition(row + forwardPositions[i][0], column + forwardPositions[i][1]);
                    ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                    moves.add(newMove);
                    if (!start) {
                        break;
                    }
                }
                else {
                    ChessPosition newPosition = new ChessPosition(row + forwardPositions[i][0], column + forwardPositions[i][1]);
                    for (int j = 0; j < promotions.length; j++) {
                        ChessMove newMove = new ChessMove(myPosition, newPosition, promotions[j]);
                        moves.add(newMove);
                    }
                }
            }
            else {
                break;
            }
        }
        //Attack right
        for (int i = 0; i < forwardPositions.length; i++) {
            int attack = spaceClear(board, row + attackPositions[i][0], column + attackPositions[i][1], pieceColor);
            int endPosition = row + attackPositions[i][0];
            if (attack == 2) {
                if (endPosition != 8 && endPosition != 1) {
                    ChessPosition newPosition = new ChessPosition(row + attackPositions[i][0], column + attackPositions[i][1]);
                    ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                    moves.add(newMove);
                }
                else {
                    ChessPosition newPosition = new ChessPosition(row + attackPositions[i][0], column + attackPositions[i][1]);
                    for (int j = 0; j < promotions.length; j++) {
                        ChessMove newMove = new ChessMove(myPosition, newPosition, promotions[j]);
                        moves.add(newMove);
                    }
                }
            }
        }
        return moves;
    }


    static public Collection<ChessMove> moveRook(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(moveRecurse(board, myPosition, 1,0));
        moves.addAll(moveRecurse(board, myPosition, -1,0));
        moves.addAll(moveRecurse(board, myPosition, 0,1));
        moves.addAll(moveRecurse(board, myPosition, 0,-1));
        return moves;
    }

    static public Collection<ChessMove> moveQueen(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        moves.addAll(moveRook(board, myPosition));
        moves.addAll(moveBishop(board, myPosition));
        return moves;
    }

}
