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
class CachedGameMatrixImpl extends GameMatrixImpl {

    /** Buffered free masks per row.
     * A set 1-bit means that the digit 1 is free for use.
     * A set 2-bit means that the digit 2 is free for use.
     * And so on.
     */
    private final int[] rowFree;

    /** Buffered free masks per column.
     * @see #rowFree
     */
    private final int[] columnFree;

    /** Buffered free masks per block.
     * @see #rowFree
     */
    private final int[][] blockFree;

    /** The count of non-{@link GameSchema#getUnsetValue() unset} cells.
     * @see #getSetCount()
     */
    private int setCount;

    /** Row index for each currently unset cell. */
    private final int[] freeCellRows;

    /** Column index for each currently unset cell. */
    private final int[] freeCellColumns;

    /** Position lookup of a cell in {@link #freeCellRows} / freeCellColumns.
     * A value of -1 means the cell is currently set.
     */
    private final int[][] freeCellIndex;

    /** Number of entries used in freeCellRows/freeCellColumns. */
    private int freeCellCount;

    /**
     * Creates an empty full-writable riddle.
     * @param schema the game schema that defines the dimensions.
     */
    CachedGameMatrixImpl(final GameSchema schema) {
        super(schema);
        final int blockCount = schema.getBlockCount();
        final int width = schema.getWidth();
        final int totalFields = schema.getTotalFields();

        blockFree = new int[blockCount][blockCount];
        rowFree = new int[width];
        columnFree = new int[width];
        freeCellRows = new int[totalFields];
        freeCellColumns = new int[totalFields];
        freeCellIndex = new int[width][width];

        for (int i = 0; i < width; i++) {
            rowFree[i] = schema.getBitMask();
            columnFree[i] = schema.getBitMask();
        }

        for (int i = 0; i < blockCount; i++) {
            for (int j = 0; j < blockCount; j++) {
                blockFree[i][j] = schema.getBitMask();
            }
        }

        int freeIndex = 0;
        for (int row = 0; row < width; row++) {
            for (int column = 0; column < width; column++) {
                freeCellRows[freeIndex] = row;
                freeCellColumns[freeIndex] = column;
                freeCellIndex[row][column] = freeIndex;
                freeIndex++;
            }
        }
        freeCellCount = freeIndex;
    }

    /**
     * Clone constructor.
     * @param source the source to initialize with.
     */
    CachedGameMatrixImpl(final CachedGameMatrixImpl source) {
        super(source);

        blockFree = QuadraticArrays.cloneArray(source.blockFree);
        columnFree = Arrays.copyOf(source.columnFree, source.columnFree.length);
        rowFree = Arrays.copyOf(source.rowFree, source.rowFree.length);
        freeCellRows = Arrays.copyOf(source.freeCellRows,
                source.freeCellRows.length);
        freeCellColumns = Arrays.copyOf(source.freeCellColumns,
                source.freeCellColumns.length);
        freeCellIndex = QuadraticArrays.cloneArray(source.freeCellIndex);
        setCount = source.setCount;
        freeCellCount = source.freeCellCount;
    }

    @Override
    FreeCellResult findLeastFreeCell(final CellIndex rowColumnResult) {
        if (freeCellCount == 0) {
            return FreeCellResult.NONE_FREE;
        }

        int minimumBits = Integer.MAX_VALUE;
        int minimumRow = -1;
        int minimumColumn = -1;
        final int width = getSchema().getWidth();
        final int blockWidth = getSchema().getBlockWidth();

        for (int i = 0; i < freeCellCount; i++) {
            int row = freeCellRows[i];
            int column = freeCellColumns[i];
            int free = rowFree[row]
                    & columnFree[column]
                    & blockFree[row / blockWidth][column / blockWidth];
            if (free == 0) {
                return FreeCellResult.CONTRADICTION;
            }

            int bits = Integer.bitCount(free);
            assert bits > 0 && bits <= width;

            if (bits < minimumBits) {
                minimumBits = bits;
                minimumRow = row;
                minimumColumn = column;
                if (minimumBits == 1) {
                    break;
                }
            }
        }

        rowColumnResult.row = minimumRow;
        rowColumnResult.column = minimumColumn;
        return FreeCellResult.FOUND;
    }

    /** Remove an unset cell from the compact free-cell list.
     * @param row the row of the cell.
     * @param column the column of the cell.
    */
    private void removeFreeCell(final int row, final int column) {
        int index = freeCellIndex[row][column];
        assert index >= 0 && index < freeCellCount;

        int lastIndex = freeCellCount - 1;
        if (index != lastIndex) {
            int movedRow = freeCellRows[lastIndex];
            int movedColumn = freeCellColumns[lastIndex];
            freeCellRows[index] = movedRow;
            freeCellColumns[index] = movedColumn;
            freeCellIndex[movedRow][movedColumn] = index;
        }

        freeCellIndex[row][column] = -1;
        freeCellCount--;
    }

    /** Add a newly unset cell to the compact free-cell list.
     * @param row the row of the cell.
     * @param column the column of the cell.
     *
    */
    private void addFreeCell(final int row, final int column) {
        assert freeCellIndex[row][column] == -1;

        freeCellRows[freeCellCount] = row;
        freeCellColumns[freeCellCount] = column;
        freeCellIndex[row][column] = freeCellCount;
        freeCellCount++;
    }

    @Override
    int getBlockFreeMask(final int row, final int column) {
        final int blockWidth = getSchema().getBlockWidth();
        return blockFree[row / blockWidth][column / blockWidth];
    }

    @Override
    int getColumnFreeMask(final int column) {
        return columnFree[column];
    }

    @Override
    int getRowFreeMask(final int row) {
        return rowFree[row];
    }

    @Override
    int getFreeMask(final int row, final int column) {
        final int blockWidth = getSchema().getBlockWidth();
        return rowFree[row]
                & columnFree[column]
                & blockFree[row / blockWidth][column / blockWidth];
    }

    @Override
    public void set(final int row, final int column, final byte value) {
        GameSchema schema = getSchema();
        assert schema.validValue(value);
        byte oldValue = super.get(row, column);
        assert schema.validValue(oldValue);

        final byte unset = schema.getUnsetValue();
        final int blockWidth = schema.getBlockWidth();

        if (oldValue != unset) {
            int bitMask = 1 << oldValue;
            rowFree[row] |= bitMask;
            columnFree[column] |= bitMask;
            blockFree[row / blockWidth][column / blockWidth] |= bitMask;
            setCount--;
            assert setCount >= 0;
        }
        if (value != unset) {
            assert (getFreeMask(row, column)
                    & (1 << value)) != 0 // NOSONAR
                    : "Passed value " + value
                    + " is already used, would destroy class invariant";
            int bitMask = ~(1 << value);
            rowFree[row] &= bitMask;
            columnFree[column] &= bitMask;
            blockFree[row / blockWidth][column / blockWidth] &= bitMask;
            setCount++;
            assert setCount <= getSchema().getTotalFields();
        }

        if (oldValue == unset && value != unset) {
            removeFreeCell(row, column);
        } else if (oldValue != unset && value == unset) {
            addFreeCell(row, column);
        }
        assert freeCellCount == getSchema().getTotalFields() - setCount;

        assert getSchema().validBitMask(rowFree[row]) // NOSONAR
                : "Row free mask is invalid: " + rowFree[row];
        assert getSchema().validBitMask(columnFree[column]) // NOSONAR
                : "Column free mask is invalid: " + columnFree[column];
        assert getSchema().validBitMask(// NOSONAR
                blockFree[row / blockWidth][column / blockWidth])
                : "Block free mask is invalid: "
                    + blockFree[row / blockWidth][column / blockWidth];
        super.set(row, column, value);
    }

    @Override
    public int getSetCount() {
        return setCount;
    }

    @Override
    public CachedGameMatrixImpl clone() {
        return new CachedGameMatrixImpl(this);
    }
}
