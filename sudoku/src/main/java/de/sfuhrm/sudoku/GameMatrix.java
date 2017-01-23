/*
Sudoku - a fast Java Sudoku game creation library.
Copyright (C) 2017  Stephan Fuhrmann

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
Boston, MA  02110-1301, USA.
*/
package de.sfuhrm.sudoku;

import static de.sfuhrm.sudoku.Riddle.roundToBlock;
import java.util.Arrays;

/**
 * The quadratic matrix.
 * @author Stephan Fuhrmann
 */
public class GameMatrix implements Cloneable {
    
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
     * The game field. The first dimension is the row, the second the column.
     * The value 0 means unallocated. The values 1-9 mean the corresponding cell
     * value.
     */
    private byte data[][];

    /**
     * The value that is assigned to unset fields.
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
     * Creates an empty riddle.
     * @see #setAll(byte[][]) 
     */
    public GameMatrix() {
        data = new byte[SIZE][SIZE];
    }

    /** Sets all cells to the given values.
     * @param initializationData initialization data with the first dimension
     * being the rows and the second dimension being the columns.
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
    protected void block(int row, int column, final byte[] target) {
        int k = 0; // target index
        row = roundToBlock(row);
        column = roundToBlock(column);
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
     * @param row the row of the cell to get the value for.
     * @param column the column of the cell to get the value for.
     * @return the cell value ranging from 0 to 9.
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
     * @return the number of fields with a number in. Can be in the range
     * between 0 and 81.
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
    
    /** Gets a copy of the underlying array. 
     * @return the data array containing numbers between 0 and 9.
     * The first index is the row index, the second index is the column
     * index.
     */
    public byte[][] getArray() {
        return cloneArray(data);
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
        if (!(obj instanceof GameMatrix)) {
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
    
    /** Finds the duplicate bits. 
     * @param data the cell data from 0-9.
     * @return a mask with bits 1-9 set if the numbers 1-9 occur multiple times.
     */
    static int findDuplicateBits(final byte data[]) {
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
     * Checks if the whole play field is valid.
     * @return {@code true} if the filled rows, columns and blocks contain no duplicate numbers.
     */
    public final boolean isValid() {
        boolean result = true;

        byte tmpData[] = new byte[9];
        
        for (int i = 0; i < SIZE && result; i++) {
            row(i, tmpData);
            result &= findDuplicateBits(tmpData) == 0;
        }

        for (int i = 0; i < SIZE && result; i++) {
            column(i, tmpData);
            result &= findDuplicateBits(tmpData) == 0;
        }

        for (int i = 0; i < BLOCK_SIZE && result; i++) {
            for (int j = 0; j < BLOCK_SIZE && result; j++) {
                block(i, j, tmpData);
                result &= findDuplicateBits(tmpData) == 0;
            }
        }

        return result;
    }
    
    /** Gets the free mask for the given row. 
     * @param row the row to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.     
     */
    int getRowFreeMask(int row) {
        byte tmpData[] = new byte[9];
        row(row, tmpData);
        return (~getNumberMask(tmpData)) & MASK_FOR_NINE_BITS;
    }
    
    /** Gets the free mask for the given column. 
     * @param column the column to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getColumnFreeMask(int column) {
        byte tmpData[] = new byte[9];
        column(column, tmpData);
        return (~getNumberMask(tmpData)) & MASK_FOR_NINE_BITS;
    }
    
    /** Gets the free mask for the given block. 
     * @param row the row of the block start to get the free mask for.
     * @param column the column of the block start to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getBlockFreeMask(int row, int column) {
        byte tmpData[] = new byte[9];
        block(row, column, tmpData);
        return (~getNumberMask(tmpData)) & MASK_FOR_NINE_BITS;
    }
    
    /** Gets the free mask for the given cell.
     * @param row the row of the cell to get the free mask for.
     * @param column the column of the to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getFreeMask(int row, int column) {
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
     * @param row the row of the cell to test validity for.
     * @param column the column of the cell to test validity for.
     * @param value the value to simulate setting for.
     * @return {@code true} if the given cell can be set to  {@code value} without
     * violating the game rules.
     */
    public final boolean canSet(int row, int column, byte value) {
        if (value == 0) {
            return true;
        }
        int free = getFreeMask(row, column);
        return (free & (1<<value)) != 0;
    }
    
    /** Round the given column/row to the next block bound.
     */
    static int roundToBlock(int in) {
        return in - in % BLOCK_SIZE;
    }
}
