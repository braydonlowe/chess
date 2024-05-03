package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int rowPos;
    private int columnPos;

    public ChessPosition(int row, int col) {
        this.rowPos = row;
        this.columnPos = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return rowPos;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return columnPos;
    }

    @Override
    public boolean equals(Object obj) {
        //Make sure that the objects are the same type of objects
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        //This is the real test to see if they are equal to eachother.
        ChessPosition other = (ChessPosition) obj;
        return rowPos == other.rowPos && columnPos == other.columnPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowPos, columnPos);
    }
}
