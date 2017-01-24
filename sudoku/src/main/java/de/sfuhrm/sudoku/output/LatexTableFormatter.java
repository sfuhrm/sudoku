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
 * Formats the game matrix to a LaTeX document.
 * @author Stephan Fuhrmann
 */
public class LatexTableFormatter extends AbstractTextFormatter {

    private final String tableSeparator = "&";

    public LatexTableFormatter() {
        setUnknownCellContentCharacter(" ");
    }

    @Override
    public String format(GameMatrix matrix) {
        StringBuilder sb = new StringBuilder();

        sb.append("\\begin{center}");
        sb.append("\\huge");
        sb.append(getLineSeparator());
        sb.append("\\begin{tabular}{");
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0 && i != 0) {
                sb.append("|");
            }
            sb.append("|C{1.1em}");
        }
        sb.append("|");
        sb.append("}");
        sb.append(getLineSeparator());

        for (int row = 0; row < GameMatrix.SIZE; row++) {
            if (row % 3 == 0) {
                sb.append("\\hline");
                sb.append(getLineSeparator());
            }
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                byte val = matrix.get(row, column);
                String str;
                if (val == GameMatrix.UNSET) {
                    str = getUnknownCellContentCharacter();
                } else {
                    str = Integer.toString(val);
                }

                if (column != 0) {
                    sb.append(tableSeparator);
                }
                sb.append(" ");
                sb.append(str);
                sb.append(" ");
            }
            sb.append("\\\\");
            sb.append(getLineSeparator());
            sb.append("\\hline");
            sb.append(getLineSeparator());
        }

        sb.append("\\end{tabular}");
        sb.append(getLineSeparator());
        sb.append("\\end{center}");
        sb.append(getLineSeparator());

        return sb.toString();
    }

    @Override
    public String documentStart() {
        StringBuilder sb = new StringBuilder();
        sb.append("\\documentclass[a4paper,11pt]{article}");
        sb.append(getLineSeparator());
        sb.append("\\usepackage{array}");
        sb.append(getLineSeparator());
        sb.append("\\newcolumntype{L}[1]{>{\\raggedright\\let\\newline\\\\\\arraybackslash\\hspace{0pt}}m{#1}}");
        sb.append(getLineSeparator());
        sb.append("\\newcolumntype{C}[1]{>{\\centering\\let\\newline\\\\\\arraybackslash\\hspace{0pt}}m{#1}}");
        sb.append(getLineSeparator());
        sb.append("\\newcolumntype{R}[1]{>{\\raggedleft\\let\\newline\\\\\\arraybackslash\\hspace{0pt}}m{#1}}");
        sb.append(getLineSeparator());
        sb.append("\\begin{document}");
        sb.append(getLineSeparator());
        return sb.toString();
    }

    @Override
    public String documentEnd() {
        StringBuilder sb = new StringBuilder();
        sb.append("\\end{document}");
        sb.append(getLineSeparator());
        return sb.toString();
    }
}
