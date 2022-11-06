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
 * Formats the game matrix to a text representation.
 * The text representation has a
 * <ul>
 * <li> document #{@link #documentStart() start}
 * <li> document #{@link #format(de.sfuhrm.sudoku.GameMatrix)
 * main part} and a
 * <li> document #{@link #documentEnd() end}.
 * </ul>
 * @author Stephan Fuhrmann
 */
public interface GameMatrixFormatter {

    /**
     * Formats the input game matrix to a text representation.
     * @param matrix the matrix to format to String format.
     * @return a String representation of the game matrix.
     */
    String format(GameMatrix matrix);

    /** Formats the document start part.
     * @return a String representation of the document start.
     */
    String documentStart();

    /** Formats the document end part.
     * @return a String representation of the document end.
     */
    String documentEnd();
}
