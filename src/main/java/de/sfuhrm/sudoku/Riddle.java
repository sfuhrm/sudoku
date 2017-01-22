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
     * A mask that has bits 1 to 9 set (decimal 1022).
     */
    final static int MASK_FOR_NINE_BITS
            = 1 << 1
            | 1 << 2
            | 1 << 3
            | 1 << 4
            | 1 << 5
            | 1 << 6
            | 1 << 7
            | 1 << 8
            | 1 << 9;

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

    /** Finds the duplicate bits. 
     * @param data the cell data from 0-9.
     * @return a mask with bits 1-9 set if the numbers 1-9 occur multiple times.
     */
    private static int findDuplicateBits(final byte data[]) {
        int currentMask = 0;
        int duplicates = 0;
        for (int i = 0; i < data.length; i++) {
            final int shifted = 1 << data[i];
            duplicates |= currentMask & shifted;
            currentMask |= shifted;
        }
        return duplicates & (~1);
    }
    
    /** Finds the used numbers. 
     * @param data the cell data from 0-9.
     * @return a mask with bits 1-9 set if the numbers 1-9 occur.
     */
    static int getNumberMask(final byte data[]) {
        int currentMask = 0;
        for (int i = 0; i < data.length; i++) {
            currentMask |= 1 << data[i];
        }
        return currentMask & (~1);
    }
    
    /**
     * Checks if the effect of one element is valid. This can be used to check
     * validity after setting one field. This is much quicker than
     * {@link #isValid()}.
     *
     */
    public final boolean isValid(int row, int column) {
        boolean result = true;
        
        byte data[] = new byte[9];

        if (result) {
            row(row, data);
            result &= findDuplicateBits(data) == 0;
        }

        if (result) {
            column(column, data);
            result &= findDuplicateBits(data) == 0;
        }

        if (result) {
            block(roundToBlock(row), roundToBlock(column), data);
            result &= findDuplicateBits(data) == 0;
        }
        return result;
    }

    /**
     * Checks if the whole play field is valid.
     */
    public final boolean isValid() {
        boolean result = true;

        byte data[] = new byte[9];
        
        for (int i = 0; i < SIZE && result; i++) {
            row(i, data);
            result &= findDuplicateBits(data) == 0;
        }

        for (int i = 0; i < SIZE && result; i++) {
            column(i, data);
            result &= findDuplicateBits(data) == 0;
        }

        for (int i = 0; i < BLOCK_SIZE && result; i++) {
            for (int j = 0; j < BLOCK_SIZE && result; j++) {
                block(roundToBlock(i), roundToBlock(j), data);
                result &= findDuplicateBits(data) == 0;
            }
        }

        return result;
    }

    /**
     * Set a certain field writable.
     */
    public final void setWritable(final int row, final int col, final boolean set) {
        writeable[row][col] = set;
    }
    
    /** Gets the free mask for the given row. */
    int getRowFreeMask(int row) {
        byte data[] = new byte[9];
        row(row, data);
        return (~getNumberMask(data)) & MASK_FOR_NINE_BITS;
    }
    
    /** Gets the free mask for the given column. 
     */
    int getColumnFreeMask(int column) {
        byte data[] = new byte[9];
        column(column, data);
        return (~getNumberMask(data)) & MASK_FOR_NINE_BITS;
    }
    
    /** Gets the free mask for the given block. 
     */
    int getBlockFreeMask(int row, int column) {
        byte data[] = new byte[9];
        block(row, column, data);
        return (~getNumberMask(data)) & MASK_FOR_NINE_BITS;
    }
    
    public int getFreeMask(int row, int column) {
        int free = MASK_FOR_NINE_BITS;
        free &= getRowFreeMask(row);
        free &= getColumnFreeMask(column);
        free &= getBlockFreeMask(row, column);
        return free;
    }

    /**
     * Checks if the effect of one set operation is valid. This is much quicker
     * than {@link #isValid()}.
     *
     */
    public final boolean canSet(int row, int col, byte value) {
        int free = getFreeMask(row, col);
        return (free & (1<<value)) != 0;
    }
    
    static int roundToBlock(int in) {
        return in - in % BLOCK_SIZE;
    }

    @Override
    public Object clone() {
        Riddle clone;
        clone = (Riddle) super.clone();
        clone.writeable = cloneArray(writeable);
        return clone;
    }
}
