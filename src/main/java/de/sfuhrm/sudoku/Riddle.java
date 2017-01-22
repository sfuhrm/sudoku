package de.sfuhrm.sudoku;

/**
 * The Sudoku game field.
 */
public class Riddle extends GameMatrix implements Cloneable {

    /**
     * Whether the cell is writable. Pre-defined cells are only readable, use
     * settable cells are writable.
     */
    private boolean writeable[][];

    /**
     * Creates an empty full-writable riddle.
     */
    public Riddle() {
        writeable = new boolean[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                setWritable(i, j, true);
            }
        }
    }

    /**
     * Set a certain field writable.
     */
    public final void setWritable(final int row, final int col, final boolean set) {
        writeable[row][col] = set;
    }
    
    @Override
    public Object clone() {
        Riddle clone;
        clone = (Riddle) super.clone();
        clone.writeable = cloneArray(writeable);
        return clone;
    }
}
