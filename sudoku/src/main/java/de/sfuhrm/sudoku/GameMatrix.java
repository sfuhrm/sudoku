/*
Sudoku - a fast Java Sudoku game creation library.
Copyright (C) 2017-2018  Stephan Fuhrmann

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

import static de.sfuhrm.sudoku.GameMatrix.roundToBlock;
import java.util.Arrays;

/**
 * The quadratic matrix.
 * @author Stephan Fuhrmann
 */
public class GameMatrix implements Cloneable, GameMatrixInterface {

    /**
     * The game field. The first dimension is the row, the second the column.
     * The value 0 means unallocated (see {@link #UNSET}).
     * The values 1-9 mean the corresponding cell
     * value.
     */
    private byte[][] data;

    /**
     * Creates an empty riddle.
     * @see #setAll(byte[][])
     */
    public GameMatrix() {
        data = new byte[SIZE][SIZE];
    }

    /** Is the value passed in valid for a field?
     * @param b value to check.
     * @return {@code true} if valid.
     */
    protected static boolean validValue(final byte b) {
        return b == UNSET || (b >= MINIMUM_VALUE && b <= MAXIMUM_VALUE);
    }

    /** Is the coordinate pair passed valid?
     * @param row the row index.
     * @param column the column index.
     * @return {@code true} if valid.
     */
    protected static boolean validCoords(final int row, final int column) {
        return row >= 0 && row < SIZE && column >= 0 && column < SIZE;
    }

    /** Sets all cells to the given values.
     * @param initializationData initialization data with the first dimension
     * being the rows and the second dimension being the columns.
     */
    @Override
    public final void setAll(final byte[][] initializationData) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                set(j, i, initializationData[j][i]);
            }
        }
    }

    /** Gets a copy of the given row.
     * @param index the row index to get the array for.
     * @param target a 9-element array to receive the row data.
     */
    protected final void row(final int index, final byte[] target) {
        assert target.length == SIZE;
        System.arraycopy(data[index], 0, target, 0, SIZE);
    }

    /** Gets a copy of the given column.
     * @param index the column index to get the array for.
     * @param target a 9-element array to receive the column data.
     */
    protected final void column(final int index, final byte[] target) {
        assert target.length == SIZE;
        for (int i = 0; i < SIZE; i++) {
            target[i] = data[i][index];
        }
    }

    /** Gets a copy of the given block.
     * @param row start row of the block (0..6).
     * @param column start column of the block (0..6).
     * @param target a 9-element array to receive the block data.
     */
    protected final void block(final int row,
            final int column,
            final byte[] target) {
        assert target.length == SIZE;
        assert validCoords(row, column);
        int k = 0; // target index
        int roundRow = roundToBlock(row);
        int roundColumn = roundToBlock(column);
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                target[k++] = data[roundRow + i][roundColumn + j];
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
    public static byte[][] parse(final String... rows) {
        if (rows.length != SIZE) {
            throw new IllegalArgumentException("Array must have "
                    + SIZE + " elements");
        }

        byte[][] result = new byte[SIZE][SIZE];

        for (int r = 0; r < rows.length; r++) {
            if (rows[r].length() != SIZE) {
                throw new IllegalArgumentException(
                        "Row " + r
                                + " must have "
                                + SIZE + " elements: "
                                + rows[r]);
            }

            for (int c = 0; c < SIZE; c++) {
                char v = rows[r].charAt(c);

                if (v >= '0' && v <= '9') {
                    result[r][c] = (byte) (v - '0');
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
    @Override
    public final void clear() {
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
    @Override
    public final byte get(final int row, final int column) {
        assert validCoords(row, column);
        return data[row][column];
    }

    /**
     * Set the value of a field.
     * @param column the column of the field.
     * @param row the row of the field.
     * @param value the value of the field.
     */
    @Override
    public void set(final int row, final int column, final byte value) {
        assert validCoords(row, column);
        assert validValue(value)
                : "Value out of range: " + value;
        data[row][column] = value;
    }

    /**
     * Get the number of set cells.
     * @return the number of fields with a number in. Can be in the range
     * between 0 and 81.
     */
    @Override
    public int getSetCount() {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                assert validValue(data[i][j]);
                if (data[i][j] != UNSET) {
                    count++;
                }
            }
        }
        assert count >= 0 && count <= TOTAL_FIELDS;
        return count;
    }

    /** Gets a copy of the underlying array.
     * @return the data array containing numbers between 0 and 9.
     * The first index is the row index, the second index is the column
     * index.
     */
    @Override
    public final byte[][] getArray() {
        return cloneArray(data);
    }

    /** Clones the given two-dimensional int array.
     * @param array the array to clone.
     * @return a clone of the input array with same dimensions and content.
     */
    protected static int[][] cloneArray(final int[][] array) {
        int[][] result = new int[SIZE][SIZE];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    /** Clones the given two-dimensional byte array.
     * @param array the array to clone.
     * @return a clone of the input array with same dimensions and content.
     */
    protected static byte[][] cloneArray(final byte[][] array) {
        byte[][] result = new byte[SIZE][SIZE];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    /** Clones the given two-dimensional boolean array.
     * @param array the array to clone.
     * @return a clone of the input array with same dimensions and content.
     */
    protected static boolean[][] cloneArray(final boolean[][] array) {
        boolean[][] result = new boolean[SIZE][SIZE];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                byte v = get(i, j);
                assert validValue(v);
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
        return Arrays.deepHashCode(this.data);
    }

    @Override
    public final boolean equals(final Object obj) {
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
        return Arrays.deepEquals(this.data, other.data);
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
    protected static int findDuplicateBits(final byte[] data) {
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
    protected static int getNumberMask(final byte[] data) {
        int currentMask = 0;
        for (int i = 0; i < data.length; i++) {
            currentMask |= 1 << data[i];
        }
        // mask out UNSET (1 == 1<<0)
        return currentMask & (~1);
    }

    /**
     * Checks if the whole play field is valid.
     * @return {@code true} if the filled rows, columns and blocks
     * contain no duplicate numbers.
     */
    @Override
    public final boolean isValid() {
        boolean result = true;

        byte[] tmpData = new byte[GameMatrixInterface.SIZE];

        for (int i = 0; i < SIZE && result; i++) {
            row(i, tmpData);
            result &= findDuplicateBits(tmpData) == 0;
        }

        for (int i = 0; i < SIZE && result; i++) {
            column(i, tmpData);
            result &= findDuplicateBits(tmpData) == 0;
        }

        for (int i = 0; i < SIZE && result; i += BLOCK_SIZE) {
            for (int j = 0; j < SIZE && result; j += BLOCK_SIZE) {
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
    protected int getRowFreeMask(final int row) {
        byte[] tmpData = new byte[GameMatrixInterface.SIZE];
        row(row, tmpData);
        return (~getNumberMask(tmpData)) & MASK_FOR_NINE_BITS;
    }

    /** Gets the free mask for the given column.
     * @param column the column to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    protected int getColumnFreeMask(final int column) {
        byte[] tmpData = new byte[GameMatrixInterface.SIZE];
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
    protected int getBlockFreeMask(final int row, final int column) {
        byte[] tmpData = new byte[GameMatrixInterface.BLOCK_SIZE
                * GameMatrixInterface.BLOCK_SIZE];
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
    protected int getFreeMask(
            final int row,
            final int column) {
        int free = MASK_FOR_NINE_BITS;
        assert validCoords(row, column);
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
     * @return {@code true} if the given cell can be set to
     * {@code value} without
     * violating the game rules.
     */
    @Override
    public final boolean canSet(
            final int row,
            final int column,
            final byte value) {
        assert validCoords(row, column);
        assert validValue(value);
        if (value == UNSET) {
            return true;
        }
        int free = getFreeMask(row, column);
        return (free & (1 << value)) != 0;
    }

    /** Round the given column/row to the next block boundary.
     * @param in the column/row index to round.
     * @return a row/column index at a block boundary.
     */
    protected static int roundToBlock(final int in) {
        return in - in % BLOCK_SIZE;
    }

    /** Find the cell with the lest number of possible candidates.
     * @param rowColumnResult a two-element int array receiving the
     * row and column of the result. First element will be the row index,
     * the second the column index.
     * @return {@code true} if the minimum free cell could be found.
     * Can be {@code false} in the case of a fully-filled matrix.
     */
    protected final boolean findLeastFreeCell(final int[] rowColumnResult) {
        int minimumBits = -1;
        int minimumRow = -1;
        int minimumColumn = -1;
        for (int row = 0; row < GameMatrixInterface.SIZE; row++) {
            for (int column = 0; column < GameMatrixInterface.SIZE; column++) {
                if (get(row, column) != GameMatrixInterface.UNSET) {
                    continue;
                }
                int free = getFreeMask(row, column);
                int bits = Integer.bitCount(free);

                if (bits != 0 && (minimumBits == -1 || bits < minimumBits)) {
                    minimumColumn = column;
                    minimumRow = row;
                    minimumBits = bits;
                }
            }
        }
        rowColumnResult[0] = minimumRow;
        rowColumnResult[1] = minimumColumn;
        return minimumBits != -1;
    }
}
