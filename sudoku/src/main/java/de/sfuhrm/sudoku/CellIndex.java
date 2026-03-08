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

import java.util.Objects;

/** A mutable coordinate pair of row and column. */
class CellIndex {
    /** The row index. */
    int row;
    /** The column index. */
    int column;

    /** New instance with row and column zero. */
    CellIndex() {
    }

    /** New instance with given row and column.
     * @param inRow initial row value.
     * @param inColumn initial column value.
     * */
    CellIndex(final int inRow, final int inColumn) {
        this.row = inRow;
        this.column = inColumn;
    }

    @Override
    public String toString() {
        return "CellIndex{"
                + "row=" + row
                + ", column=" + column
                + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof CellIndex)) {
            return false;
        }
        CellIndex cellIndex = (CellIndex) o;
        return row == cellIndex.row && column == cellIndex.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
