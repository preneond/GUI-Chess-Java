package core;

import java.io.Serializable;

/**
 * Created by Ondrej Prenek on 21.3.2016.
 */
public class Position implements Serializable {
    private static final long serialVersionUID = 11L;
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * @return row-number in scale 0-7
     */
    public int getRow() {
        return row;
    }

    /**
     * @return column-number in scale 0-7
     */
    public int getColumn() {
        return column;
    }

    /**
     * Check if the position is equal to another
     *
     * @param position another positon
     * @return true if the positions are equal,false otherwise
     */
    public boolean isEqual(Position position) {
        return (position.getRow() == row && position.getColumn() == column);
    }

    /**
     * Write Position to string letter and number
     * letter is current row in scale A-H
     * number is current column in scale 1-8
     *
     * @return
     */
    @Override
    public String toString() {
        String toStr;
        char letter = (char) (column + 65);
        toStr = letter + "" + (8 - row);
        return toStr.toLowerCase();
    }

}
