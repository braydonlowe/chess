package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row_pos;
    private int column_pos;

    public ChessPosition(int row, int col) {
        this.row_pos = row;
        this.column_pos = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row_pos;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return column_pos;
    }

    @Override
    public boolean equals(Object obj) {
        //Reference to the object in the same memory location?
        if (this == obj) {
            return true;
        }
        //Make sure that the objects are the same type of objects
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        //This is the real test to see if they are equal to eachother.
        ChessPosition other = (ChessPosition) obj;
        return row_pos == other.row_pos && column_pos == other.column_pos;
    }
}
