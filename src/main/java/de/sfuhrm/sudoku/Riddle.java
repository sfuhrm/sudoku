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

/**
 * The Sudoku game field.
 * This is a game matrix that has the information whether fields
 * may be written to or not.
 * @author Stephan Fuhrmann
 */
public class Riddle extends GameMatrix implements Cloneable {

    /**
     * Whether the cell is writable. Pre-defined cells are only readable, use
     * settable cells are writable.
     */
    private boolean writeable[][];

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

    /**
     * Get whether a certain field is writable.
     */
    public final boolean getWritable(final int row, final int col) {
        return writeable[row][col];
    }
    
    /**
     * Set a certain field writable.
     */
    public final void setWritable(final int row, final int col, final boolean set) {
        writeable[row][col] = set;
    }
    
    @Override
    public Object clone() {
        Riddle clone;
        clone = (Riddle) super.clone();
        clone.writeable = cloneArray(writeable);
        return clone;
    }
}
