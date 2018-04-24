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
package de.sfuhrm.sudoku.output;

import de.sfuhrm.sudoku.GameMatrix;

/**
 * Formats the game matrices to a 3-dimensional JSON array.
 * The outer dimension is the matrix itself.
 * The next dimension is the row.
 * The last dimension is the column containing numbers from
 * 0 (for UNSET) over 1 to 9.
 * @author Stephan Fuhrmann
 */
public final class JsonArrayFormatter extends AbstractTextFormatter {

    /** Number of matrices printed. */
    private int count;

    /** Constructs a new instance.
     */
    public JsonArrayFormatter() {
        setUnknownCellContentCharacter("0");
        count = 0;
    }

    @Override
    public String format(final GameMatrix matrix) {
        StringBuilder sb = new StringBuilder();

        if (count != 0) {
            sb.append(",");
        }
        sb.append("[");
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            sb.append("[");
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                byte val = matrix.get(row, column);
                String str;
                if (val == GameMatrix.UNSET) {
                    str = getUnknownCellContentCharacter();
                } else {
                    str = Integer.toString(val);
                }

                if (column != 0) {
                    sb.append(", ");
                }
                sb.append(str);
            }
            sb.append("]");
            if (row != GameMatrix.SIZE - 1) {
                sb.append(",");
            }
            sb.append(getLineSeparator());
        }

        sb.append("]");
        sb.append(getLineSeparator());

        count++;

        return sb.toString();
    }

    @Override
    public String documentStart() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(getLineSeparator());
        count = 0;
        return sb.toString();
    }

    @Override
    public String documentEnd() {
        StringBuilder sb = new StringBuilder();
        sb.append("]");
        sb.append(getLineSeparator());
        count = 0;
        return sb.toString();
    }
}
