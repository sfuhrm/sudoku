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

/**
 * The quadratic matrix.
 *
 * @author Stephan Fuhrmann
 */
public interface GameMatrixInterface {

    /**
     * A mask that has bits 1 to 9 set (decimal 1022).
     */
    int MASK_FOR_NINE_BITS = 1022;

    /**
     * The value that is assigned to unset fields.
     */
    byte UNSET = 0;

    /**
     * The valid value that is the minimum (1).
     */
    byte MINIMUM_VALUE = 1;

    /**
     * The valid value that is the maximum (9).
     */
    byte MAXIMUM_VALUE = 9;

    /**
     * The size in one dimension.
     */
    int SIZE = 9;

    /**
     * The total number of fields.
     */
    int TOTAL_FIELDS = SIZE * SIZE;

    /**
     * The edge dimension of a 3x3 block.
     *
     */
    int BLOCK_SIZE = 3;

    /**
     * The total number of blocks in one dimension.
     */
    int BLOCK_COUNT = SIZE / BLOCK_SIZE;

    /**
     * Clear the cells.
     */
    void clear();

    /**
     * Get the value of a field.
     *
     * @param row the row of the cell to get the value for.
     * @param column the column of the cell to get the value for.
     * @return the cell value ranging from 0 to 9.
     */
    byte get(final int row, final int column);

    /**
     * Set the value of a field.
     *
     * @param column the column of the field.
     * @param row the row of the field.
     * @param value the value of the field.
     */
    void set(final int row, final int column, final byte value);

    /**
     * Sets all cells to the given values.
     *
     * @param initializationData initialization data with the first dimension
     * being the rows and the second dimension being the columns.
     */
    void setAll(byte[][] initializationData);

    /**
     * Get the number of set cells.
     *
     * @return the number of fields with a number in. Can be in the range
     * between 0 and 81.
     */
    int getSetCount();

    /**
     * Gets a copy of the underlying array.
     *
     * @return the data array containing numbers between 0 and 9. The first
     * index is the row index, the second index is the column index.
     */
    byte[][] getArray();

    /**
     * Checks if the whole play field is valid.
     *
     * @return {@code true} if the filled rows, columns and blocks contain no
     * duplicate numbers.
     */
    boolean isValid();

    /**
     * Checks if the effect of one set operation is valid. This is much quicker
     * than {@link #isValid()}.
     *
     * @param row the row of the cell to test validity for.
     * @param column the column of the cell to test validity for.
     * @param value the value to simulate setting for.
     * @return {@code true} if the given cell can be set to {@code value}
     * without violating the game rules.
     */
    boolean canSet(
            final int row,
            final int column,
            final byte value);

    /** Is the value passed in valid for a field?
     * @param b value to check.
     * @return {@code true} if valid.
     */
    static boolean validValue(final byte b) {
        return b == UNSET || (b >= MINIMUM_VALUE && b <= MAXIMUM_VALUE);
    }

    /** Is the coordinate pair passed valid?
     * @param row the row index.
     * @param column the column index.
     * @return {@code true} if valid.
     */
    static boolean validCoords(final int row, final int column) {
        return row >= 0 && row < SIZE && column >= 0 && column < SIZE;
    }
}
