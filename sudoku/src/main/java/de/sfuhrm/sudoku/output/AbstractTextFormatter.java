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

/**
 * Basic functionality for most text formatters.
 * @author Stephan Fuhrmann
 */
abstract class AbstractTextFormatter implements GameMatrixFormatter {
    private String unknownCellContentCharacter = ".";
    private String lineSeparator = "\n";

    /** Gets the String to use for unknown/unset cells. */
    public String getUnknownCellContentCharacter() {
        return unknownCellContentCharacter;
    }

    /** Sets the String to use for unknown/unset cells. */
    public void setUnknownCellContentCharacter(String unknownCellContentCharacter) {
        this.unknownCellContentCharacter = unknownCellContentCharacter;
    }

    /** Gets the line separator String to use. */
    public String getLineSeparator() {
        return lineSeparator;
    }

    /** Sets the line separator String to use. */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    @Override
    public String documentStart() {
        return "";
    }
    
    @Override
    public String documentEnd() {
        return "";
    }    
}
