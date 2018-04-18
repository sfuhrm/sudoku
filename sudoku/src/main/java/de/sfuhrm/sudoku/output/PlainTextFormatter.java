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
package de.sfuhrm.sudoku.output;

import de.sfuhrm.sudoku.GameMatrix;

/**
 * Formats the game matrix to a plain text.
 * @author Stephan Fuhrmann
 */
public final class PlainTextFormatter extends AbstractTextFormatter {

    @Override
    public String format(final GameMatrix matrix) {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < GameMatrix.SIZE; row++) {
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                byte val = matrix.get(row, column);
                String str;
                if (val == GameMatrix.UNSET) {
                    str = getUnknownCellContentCharacter();
                } else {
                    str = Integer.toString(val);
                }
                sb.append(str);
            }
            sb.append(getLineSeparator());
        }
        return sb.toString();
    }
}
