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
 * GameMatrix with additional free bit masking features.
 * The free bit masks are usually not used by applications.
 *
 * @author Stephan Fuhrmann
 */
interface BitFreeMatrixInterface extends GameMatrix, Cloneable {

    /**
     * A mask that has bits 1 to 9 set (decimal 1022).
     * @deprecated use GameSchema
     */
    int MASK_FOR_NINE_BITS = 1022;

    /** Gets the free mask for the given cell.
     * @param row the row of the cell to get the free mask for.
     * @param column the column of the to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getFreeMask(
            int row,
            int column);

    /** Gets the free mask for the given block.
     * @param row the row of the block start to get the free mask for.
     * @param column the column of the block start to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getBlockFreeMask(int row, int column);

    /** Gets the free mask for the given column.
     * @param column the column to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getColumnFreeMask(int column);

    /** Gets the free mask for the given row.
     * @param row the row to get the free mask for.
     * @return bit mask with the bit 1 telling whether the number 1 is free,
     * the bit 2 telling whether the number 2 is free, and so on. The bit 0
     * is not used.
     */
    int getRowFreeMask(int row);

    /** Result for {@linkplain #findLeastFreeCell(int[])}. */
    enum FreeCellResult {
        /** A free cell was found. */
        FOUND,
        /** No free cell was found. */
        NONE_FREE,
        /** There's a contradiction in the matrix that can't be solved.
         * */
        CONTRADICTION;
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
    default FreeCellResult findLeastFreeCell(final int[] rowColumnResult) {
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
        rowColumnResult[0] = minimumRow;
        rowColumnResult[1] = minimumColumn;
        return minimumBits != -1
                ? FreeCellResult.FOUND
                : FreeCellResult.NONE_FREE;
    }

}
