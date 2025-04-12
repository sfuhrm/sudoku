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
 * The quadratic matrix.
 * @author Stephan Fuhrmann
 */
class GameMatrixImpl implements Cloneable, GameMatrix {

    /** The game schema that is used to calculate the dimensions
     * of this matrix.
     * */
    private final GameSchema gameSchema;

    /**
     * The game field. The first dimension is the row, the second the column.
     * The value 0 means unallocated
     * (see {@link GameSchema#getUnsetValue() unset}).
     * The values 1-9 mean the corresponding cell
     * value.
     */
    private byte[][] data;

    /**
     * Creates an empty riddle.
     * @param inGameSchema the game schema that defines the dimensions of this
     *                   matrix.
     * @see #setAll(byte[][])
     */
    GameMatrixImpl(final GameSchema inGameSchema) {
        this.gameSchema = inGameSchema;
        data = new byte[inGameSchema.getWidth()][inGameSchema.getWidth()];
    }

    /** Sets all cells to the given values.
     * @param initializationData initialization data with the first dimension
     * being the rows and the second dimension being the columns.
     */
    @Override
    public final void setAll(final byte[][] initializationData) {
        for (int i = 0; i < gameSchema.getWidth(); i++) {
            for (int j = 0; j < gameSchema.getWidth(); j++) {
                set(j, i, initializationData[j][i]);
            }
        }
    }

    /** Get the game schema this matrix was generated with.
     * @return the game schema that defines the matrix dimensions.
     * */
    public GameSchema getSchema() {
        return gameSchema;
    }

    /** Gets a copy of the given row.
     * @param index the row index to get the array for.
     * @param target a 9-element array to receive the row data.
     */
    protected final void row(final int index, final byte[] target) {
        assert target.length == gameSchema.getWidth();
        System.arraycopy(data[index], 0, target, 0, gameSchema.getWidth());
    }

    /** Gets a copy of the given column.
     * @param index the column index to get the array for.
     * @param target a 9-element array to receive the column data.
     */
    protected final void column(final int index, final byte[] target) {
        assert target.length == gameSchema.getWidth();
        for (int i = 0; i < gameSchema.getWidth(); i++) {
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
        assert target.length == gameSchema.getWidth();
        assert getSchema().validCoords(row, column);
        int k = 0; // target index
        int roundRow = roundToBlock(row);
        int roundColumn = roundToBlock(column);
        for (int i = 0; i < gameSchema.getBlockWidth(); i++) {
            for (int j = 0; j < gameSchema.getBlockWidth(); j++) {
                target[k++] = data[roundRow + i][roundColumn + j];
            }
        }
    }

    /**
     * Clear the cells.
     */
    @Override
    public final void clear() {
        byte unsetValue = gameSchema.getUnsetValue();
        for (int i = 0; i < gameSchema.getWidth(); i++) {
            for (int j = 0; j < gameSchema.getWidth(); j++) {
                set(j, i, unsetValue);
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
        assert getSchema().validCoords(row, column);
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
        assert getSchema().validCoords(row, column);
        assert getSchema().validValue(value)
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
        for (int i = 0; i < gameSchema.getWidth(); i++) {
            for (int j = 0; j < gameSchema.getWidth(); j++) {
                assert getSchema().validValue(data[i][j]);
                if (data[i][j] != gameSchema.getUnsetValue()) {
                    count++;
                }
            }
        }
        assert count >= 0 && count <= gameSchema.getTotalFields();
        return count;
    }

    /** Gets a copy of the underlying array.
     * @return the data array containing numbers between 0 and 9.
     * The first index is the row index, the second index is the column
     * index.
     */
    @Override
    public final byte[][] getArray() {
        return QuadraticArrays.cloneArray(data);
    }

    @Override
    public final String toString() {
        return QuadraticArrays.toString(this);
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
        if (!(obj instanceof GameMatrixImpl)) {
            return false;
        }
        final GameMatrixImpl other = (GameMatrixImpl) obj;
        return Arrays.deepEquals(this.data, other.data);
    }

    @Override
    public GameMatrixImpl clone() {
        GameMatrixImpl clone;
        try {
            clone = (GameMatrixImpl) super.clone();
            clone.data = QuadraticArrays.cloneArray(data);
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException(ex);
        }

        return clone;
    }

    /** Finds the duplicate bits.
     * @param gameSchema the game dimensions.
     * @param cellData the cell data from 0-9.
     * @return a mask with bits 1-9 set if the
     * numbers 1-9 occur multiple times.
     */
    protected static int findDuplicateBits(
            final GameSchema gameSchema,
                                    final byte[] cellData) {
        int currentMask = 0;
        int duplicates = 0;
        byte unset = gameSchema.getUnsetValue();
        for (int i = 0; i < cellData.length; i++) {
            final byte cellValue = cellData[i];
            if (cellValue != unset) {
                final int shifted = 1 << cellData[i];
                duplicates |= currentMask & shifted;
                currentMask |= shifted;
            }
        }
        return duplicates & (~1);
    }

    /** Finds the used numbers.
     * @param schema the game dimensions.
     * @param cellData the cell data from 0-9.
     * @return a mask with bits 1-9 set if the numbers 1-9 occur.
     */
    protected static int getNumberMask(final GameSchema schema,
                                       final byte[] cellData) {
        int currentMask = 0;
        final byte unset = schema.getUnsetValue();
        for (int i = 0; i < cellData.length; i++) {
            if (cellData[i] != unset) {
                currentMask |= 1 << cellData[i];
            }
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

        byte[] tmpData = new byte[gameSchema.getWidth()];

        for (int i = 0; result && i < gameSchema.getWidth(); i++) {
            row(i, tmpData);
            result &= findDuplicateBits(gameSchema, tmpData) == 0;
        }

        for (int i = 0; result && i < gameSchema.getWidth(); i++) {
            column(i, tmpData);
            result &= findDuplicateBits(gameSchema, tmpData) == 0;
        }

        for (int i = 0;
             result && i < gameSchema.getWidth();
             i += gameSchema.getBlockWidth()) {
            for (int j = 0;
                 result && j < gameSchema.getWidth();
                 j += gameSchema.getBlockWidth()) {
                block(i, j, tmpData);
                result &= findDuplicateBits(gameSchema, tmpData) == 0;
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
    int getRowFreeMask(final int row) {
        byte[] tmpData = new byte[gameSchema.getWidth()];
        row(row, tmpData);
        return (~getNumberMask(gameSchema, tmpData)) & getSchema().getBitMask();
    }

    /** Gets the free mask for the given column.
     * @param column the column to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getColumnFreeMask(final int column) {
        byte[] tmpData = new byte[gameSchema.getWidth()];
        column(column, tmpData);
        return (~getNumberMask(gameSchema, tmpData)) & getSchema().getBitMask();
    }

    /** Gets the free mask for the given block.
     * @param row the row of the block start to get the free mask for.
     * @param column the column of the block start to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getBlockFreeMask(final int row, final int column) {
        byte[] tmpData = new byte[getSchema().getBlockWidth()
                * getSchema().getBlockWidth()];
        block(row, column, tmpData);
        return (~getNumberMask(gameSchema, tmpData)) & getSchema().getBitMask();
    }

    /** Gets the free mask for the given cell.
     * @param row the row of the cell to get the free mask for.
     * @param column the column of the to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getFreeMask(
            final int row,
            final int column) {
        int free = gameSchema.getBitMask();
        assert gameSchema.validCoords(row, column);
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
        assert gameSchema.validCoords(row, column);
        assert gameSchema.validValue(value);
        // can always be set
        if (value == gameSchema.getUnsetValue()) {
            return true;
        }
        int free = getFreeMask(row, column);
        return (free & (1 << value)) != 0;
    }

    /** Round the given column/row to the next block boundary.
     * @param in the column/row index to round.
     * @return a row/column index at a block boundary.
     */
    protected int roundToBlock(final int in) {
        return in - in % gameSchema.getBlockWidth();
    }

    /** Result for {@linkplain #findLeastFreeCell(CellIndex)}. */
    enum FreeCellResult {
        /** A free cell was found. */
        FOUND,
        /** No free cell was found. */
        NONE_FREE,
        /** There's a contradiction in the matrix that can't be solved.
         * */
        CONTRADICTION
    }

    /** Find the cell with the lest number of possible candidates.
     * @param rowColumnResult a two-element int array receiving the
     * row and column of the result. First element will be the row index,
     * the second the column index.
     * @return {@linkplain FreeCellResult#FOUND} if a free cell was found,
     * {@linkplain FreeCellResult#NONE_FREE} if all cells are occupied,
     * {@linkplain FreeCellResult#CONTRADICTION} if cells were free but
     * could not be occupied.
     */
    FreeCellResult findLeastFreeCell(final CellIndex rowColumnResult) {
        int minimumBits = -1;
        int minimumRow = -1;
        int minimumColumn = -1;

        final int width = getSchema().getWidth();
        final byte unset = getSchema().getUnsetValue();
        search:
        for (int row = 0; row < width; row++) {
            int rowMask = getRowFreeMask(row);
            // skip if the row has no free cells
            if (rowMask == 0) {
                continue;
            }
            for (int column = 0; column < width; column++) {
                if (get(row, column) != unset) {
                    continue;
                }
                int free = getFreeMask(row, column);
                if (free == 0) {
                    return FreeCellResult.CONTRADICTION;
                }
                int bits = Integer.bitCount(free);

                assert bits <= width;

                if (bits != 0 && (minimumBits == -1 || bits < minimumBits)) {
                    minimumColumn = column;
                    minimumRow = row;
                    minimumBits = bits;
                    if (minimumBits == 1) {
                        // there is nothing better than 1 bits
                        // 0 bits means UNSET which is no valid solution
                        break search;
                    }
                }
            }
        }
        rowColumnResult.row = minimumRow;
        rowColumnResult.column = minimumColumn;
        return minimumBits != -1
                ? FreeCellResult.FOUND
                : FreeCellResult.NONE_FREE;
    }
}
