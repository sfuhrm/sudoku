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

import java.util.Arrays;

/**
 * A version with caching of free candidates for performance purposes.
 * Note that this class expects that no illegal moves are performed.
 * This class is for calculations of a computer, not for playing with
 * a human being.
 * @author Stephan Fuhrmann
 */
class CachedGameMatrix extends GameMatrix implements Cloneable {

    /** Buffered free masks per row.
     * A set 1-bit means that the digit 1 is free for use.
     * A set 2-bit means that the digit 2 is free for use.
     * And so on.
     */
    private int[] rowFree;

    /** Buffered free masks per column.
     * @see #rowFree
     */
    private int[] columnFree;

    /** Buffered free masks per block.
     * @see #rowFree
     */
    private int[][] blockFree;

    /** The count of non-{@link #UNSET} cells.
     * @see #getSetCount()
     */
    private int setCount;

    /**
     * Creates an empty full-writable riddle.
     */
    CachedGameMatrix() {
        blockFree = new int[BLOCK_COUNT][BLOCK_COUNT];
        rowFree = new int[SIZE];
        columnFree = new int[SIZE];

        for (int i = 0; i < SIZE; i++) {
            rowFree[i] = GameMatrix.MASK_FOR_NINE_BITS;
            columnFree[i] = GameMatrix.MASK_FOR_NINE_BITS;
        }

        for (int i = 0; i < BLOCK_COUNT; i++) {
            for (int j = 0; j < BLOCK_COUNT; j++) {
                blockFree[i][j] = GameMatrix.MASK_FOR_NINE_BITS;
            }
        }
    }

    @Override
    protected int getBlockFreeMask(final int row, final int column) {
        return blockFree[row / BLOCK_COUNT][column / BLOCK_COUNT];
    }

    @Override
    protected int getColumnFreeMask(final int column) {
        return columnFree[column];
    }

    @Override
    protected int getRowFreeMask(final int row) {
        return rowFree[row];
    }

    @Override
    public int getFreeMask(final int row, final int column) {
        return rowFree[row]
                & columnFree[column]
                & blockFree[row / BLOCK_COUNT][column / BLOCK_COUNT];
    }

    @Override
    public void set(final int row, final int column, final byte value) {
        byte oldValue = super.get(row, column);

        if (oldValue != UNSET) {
            int bitMask = 1 << oldValue;
            rowFree[row] |= bitMask;
            columnFree[column] |= bitMask;
            blockFree[row / BLOCK_COUNT][column / BLOCK_COUNT] |= bitMask;
            setCount--;
        }
        assert (getFreeMask(row, column) & (1 << value)) != 0
                : "Passed value is already used, would destroy class invariant";
        if (value != UNSET) {
            int bitMask = ~(1 << value);
            rowFree[row] &= bitMask;
            columnFree[column] &= bitMask;
            blockFree[row / BLOCK_COUNT][column / BLOCK_COUNT] &= bitMask;
            setCount++;
        }
        super.set(row, column, value);
    }

    @Override
    public int getSetCount() {
        return setCount;
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
