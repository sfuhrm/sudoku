package de.sfuhrm.sudoku;

import java.util.Arrays;

/**
 * The quadratix matrix.
 */
public class GameMatrix implements Cloneable {

    /**
     * The game field. The first dimension is the row, the second the column.
     * The value 0 means unallocated. The values 1-9 mean the corresponding cell
     * value.
     *
     */
    private byte data[][];

    /**
     * The value that is assigned to unset fields.
     *
     */
    public final static byte UNSET = 0;

    /**
     * The size in one dimension.
     */
    public final static int SIZE = 9;

    /**
     * The edge dimension of a 3x3 block.
     *
     */
    protected final static int BLOCK_SIZE = 3;

    /**
     * Creates an empty full-writable riddle.
     */
    public GameMatrix() {
        data = new byte[SIZE][SIZE];
    }

    /** Sets all cells to the given values.
     */
    public final void setAll(byte initializationData[][]) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                set(j, i, initializationData[j][i]);
            }
        }
    }
    
    /** Gets a stream of the given row. 
     * @param index the row index to get the stream for.
     * @param target a 9-element array to receive the row data.
     */
    protected void row(final int index, final byte[] target) {
        System.arraycopy(data[index], 0, target, 0, SIZE);
    }
    
    /** Gets a stream of the given column. 
     * @param index the column index to get the stream for.
     * @param target a 9-element array to receive the column data.
     */
    protected void column(final int index, final byte[] target) {
        for (int i=0; i < SIZE; i++) {
            target[i] = data[i][index];
        }
    }
    
    /** Gets a stream of the given block. 
     * @param row start row of the block (0..6).
     * @param column start column of the block (0..6).
     * @param target a 9-element array to receive the block data.
     */
    protected void block(final int row, final int column, final byte[] target) {
        int k = 0; // target index
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                target[k++] = data[row+i][column+j];
            }
        }
    }
    
    /**
     * Parses a string based field descriptor.
     *
     * @param rows array of strings with each string describing a row. Digits
     * get converted to the element values, everything else gets converted to
     * UNSET.
     * @return the parsed array.
     * @throws IllegalArgumentException if one of the rows has a wrong size.
     */
    public final static byte[][] parse(String... rows) {
        if (rows.length != SIZE) {
            throw new IllegalArgumentException("Array must have " + SIZE + " elements");
        }

        byte result[][] = new byte[SIZE][SIZE];

        for (int r = 0; r < rows.length; r++) {
            if (rows[r].length() != SIZE) {
                throw new IllegalArgumentException("Row " + r + " must have " + SIZE + " elements: " + rows[r]);
            }

            for (int c = 0; c < SIZE; c++) {
                char v = rows[r].charAt(c);

                if (v >= '0' && v <= '9') {
                    result[r][c] = (byte)(v - '0');
                } else {
                    result[r][c] = UNSET;
                }
            }
        }
        return result;
    }

    /**
     * Clear the cells.
     */
    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                set(j, i, UNSET);
            }
        }
    }

    /**
     * Get the value of a field.
     */
    public final byte get(int row, int column) {
        return data[row][column];
    }

    /**
     * Set the value of a field.
     * @param column the column of the field.
     * @param row the row of the field.
     * @param value the value of the field.
     */
    public void set(final int row, final int column, final byte value) {
        data[row][column] = value;
    }
    
    /**
     * Get the number of set cells.
     */
    public int getSetCount() {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                count += data[i][j] != UNSET ? 1 : 0;
            }
        }
        return count;
    }

    static int[][] cloneArray(int array[][]) {
        int result[][] = new int[SIZE][SIZE];
        for (int i=0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }
    
    static byte[][] cloneArray(byte array[][]) {
        byte result[][] = new byte[SIZE][SIZE];
        for (int i=0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }
    
    static boolean[][] cloneArray(boolean array[][]) {
        boolean result[][] = new boolean[SIZE][SIZE];
        for (int i=0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                byte v = get(i, j);
                if (v != UNSET) {
                    sb.append(Integer.toString(get(i, j)));
                } else {
                    sb.append('_');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Arrays.deepHashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameMatrix other = (GameMatrix) obj;
        if (!Arrays.deepEquals(this.data, other.data)) {
            return false;
        }
        return true;
    }
    
    @Override
    public Object clone() {
        GameMatrix clone;
        try {
            clone = (GameMatrix) super.clone();
            clone.data = cloneArray(data);
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException();
        }
        
        return clone;
    }
}
