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
 * The Sudoku game field.
 * This is a game matrix that has the information whether fields
 * may be written to or not.
 * @author Stephan Fuhrmann
 */
class RiddleImpl extends GameMatrixImpl implements Riddle {

    /**
     * Whether the cell is writable. Pre-defined cells are only readable, use
     * settable cells are writable.
     */
    private boolean[][] writeable;

    /**
     * Creates an empty full-writable riddle.
     * @param inGameSchema the dimensions of the game to
     *                     create.
     */
    RiddleImpl(final GameSchema inGameSchema) {
        super(inGameSchema);
        int width = inGameSchema.getWidth();
        writeable = new boolean[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                setWritable(i, j, true);
            }
        }
    }

    /**
     * Get whether a certain field is writable.
     * @param row the row of the cell to get the writability for.
     * @param column the column of the cell to get the writability for.
     * @return {@code true} if the cell is writable.
     */
    @Override
    public final boolean getWritable(final int row, final int column) {
        return writeable[row][column];
    }

    /**
     * Set a certain field writable.
     * @param row the row of the cell to set the writability for.
     * @param column the column of the cell to set the writability for.
     * @param set the value to set for the cell, {@code true} means writable.
     */
    @Override
    public final void setWritable(final int row,
            final int column,
            final boolean set) {
        writeable[row][column] = set;
    }

    @Override
    public final RiddleImpl clone() {
        RiddleImpl clone;
        clone = (RiddleImpl) super.clone();
        clone.writeable = QuadraticArrays.cloneArray(writeable);
        return clone;
    }
}
