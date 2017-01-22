package de.sfuhrm.sudoku;

import java.util.Arrays;

/**
 * A version with caching of free candidates for performance purposes.
 * @author Stephan Fuhrmann
 */
public class CachedGameMatrix extends GameMatrix implements Cloneable {

    /** Buffered free masks per row. */
    private int rowFree[];
    
    /** Buffered free masks per column. */
    private int columnFree[];
    
    /** Buffered free masks per block. */
    private int blockFree[][];

    /**
     * Creates an empty full-writable riddle.
     */
    public CachedGameMatrix() {
        blockFree = new int[3][3];
        rowFree = new int[SIZE];
        columnFree = new int[SIZE];
        
        for (int i = 0; i < SIZE; i++) {
            rowFree[i] = Riddle.MASK_FOR_NINE_BITS;
            columnFree[i] = Riddle.MASK_FOR_NINE_BITS;
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                blockFree[i][j] = Riddle.MASK_FOR_NINE_BITS;
            }
        }
    }

    @Override
    int getBlockFreeMask(int row, int column) {
        return blockFree[row/3][column/3];
    }

    @Override
    int getColumnFreeMask(int column) {
        return columnFree[column];
    }
    
    @Override
    int getRowFreeMask(int row) {
        return rowFree[row];
    }
    
    @Override
    public int getFreeMask(int row, int column) {
        return rowFree[row] & columnFree[column] & blockFree[row/3][column/3];
    }

    @Override
    public void set(int row, int column, byte value) {
        byte oldValue = super.get(row, column);
        
        if (oldValue != UNSET) {
            int bitMask = 1 << oldValue;
            rowFree[row] |= bitMask;
            columnFree[column] |= bitMask;
            blockFree[row/3][column/3] |= bitMask;
        }
        if (value != UNSET) {
            int bitMask = ~(1 << value);
            rowFree[row] &= bitMask;
            columnFree[column] &= bitMask;
            blockFree[row/3][column/3] &= bitMask;
        }        
        super.set(column, row, value); 
    }

    @Override
    public Object clone() {
        CachedGameMatrix clone;
        clone = (CachedGameMatrix) super.clone();
        clone.blockFree = cloneArray(blockFree);
        clone.columnFree = Arrays.copyOf(columnFree, columnFree.length);
        clone.rowFree = Arrays.copyOf(rowFree, rowFree.length);
        return clone;
    }
}
