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
import de.sfuhrm.sudoku.GameSchema;

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

    /** Prettify the JSON output? */
    private boolean indent;

    /** Number of space characters to indent one level with. */
    private static final int INDENT_SPACES = 2;

    /** Constructs a new instance.
     */
    public JsonArrayFormatter() {
        setUnknownCellContentCharacter("0");
        count = 0;
    }

    /** Append a number of spaces to the StringBuilder.
     * @param level the number of indentions to append.
     * @param to the StringBuilder to append to.
     */
    private static void appendIndent(final int level, final StringBuilder to) {
        assert level >= 0;
        assert to != null;
        int total = level * INDENT_SPACES;
        for (int i = 0; i < total; i++) {
            to.append(' ');
        }
    }

    @Override
    public String format(final GameMatrix matrix) {
        StringBuilder sb = new StringBuilder();
        GameSchema schema = matrix.getSchema();

        if (count != 0) {
            sb.append(",");
            if (indent) {
                sb.append(getRowSeparator());
            }
        }

        if (indent) {
            appendIndent(1, sb);
        }
        sb.append("[");
        if (indent) {
            sb.append(getRowSeparator());
        }
        for (int row = 0; row < schema.getWidth(); row++) {
            if (indent) {
                appendIndent(2, sb);
            }
            sb.append("[");
            for (int column = 0; column < schema.getWidth(); column++) {
                byte val = matrix.get(row, column);
                String str;
                if (val == schema.getUnsetValue()) {
                    str = getUnknownCellContentCharacter();
                } else {
                    str = Integer.toString(val + 1 - schema.getMinimumValue());
                }

                if (column != 0) {
                    if (indent) {
                        sb.append(", ");
                    } else {
                      sb.append(",");
                    }
                }
                sb.append(str);
            }
            sb.append("]");
            if (row != schema.getWidth() - 1) {
                sb.append(",");
            }
            if (indent) {
                sb.append(getRowSeparator());
            }
        }

        if (indent) {
            appendIndent(1, sb);
        }
        sb.append("]");
        if (indent) {
            sb.append(getRowSeparator());
        }
        count++;

        return sb.toString();
    }

    /** Whether to indent the output or not.
     * @param set {@code true} if indention is desired.
     */
    public void setIndent(final boolean set) {
        this.indent = set;
    }

    @Override
    public String documentStart() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (indent) {
            sb.append(getRowSeparator());
        }
        count = 0;
        return sb.toString();
    }

    @Override
    public String documentEnd() {
        StringBuilder sb = new StringBuilder();
        sb.append("]");
        if (indent) {
            sb.append(getRowSeparator());
        }
        count = 0;
        return sb.toString();
    }
}
