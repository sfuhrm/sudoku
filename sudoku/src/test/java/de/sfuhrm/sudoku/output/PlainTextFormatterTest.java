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

import de.sfuhrm.sudoku.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link PlainTextFormatter}.
 * @author Stephan Fuhrmann
 */
public class PlainTextFormatterTest {

    @Test
    public void testNew() {
        PlainTextFormatter formatter = new PlainTextFormatter();
        assertEquals("\n", formatter.getLineSeparator());
        assertEquals(".", formatter.getUnknownCellContentCharacter());
    }    
    @Test
    public void testFormatWithEmpty() {
        GameMatrixImpl matrix = new GameMatrixImpl();
        String actual = new PlainTextFormatter().format(matrix);
        assertEquals(
                ".........\n"+
                ".........\n"+
                ".........\n"+
                ".........\n"+
                ".........\n"+
                ".........\n"+
                ".........\n"+
                ".........\n"+
                ".........\n"
                , actual);
    }
    
    @Test
    public void testFormatWithEmptyAndOtherUnknownCharacter() {
        GameMatrixImpl matrix = new GameMatrixImpl();
        PlainTextFormatter formatter = new PlainTextFormatter();
        formatter.setUnknownCellContentCharacter("?");
        String actual = formatter.format(matrix);
        assertEquals(
                "?????????\n"+
                "?????????\n"+
                "?????????\n"+
                "?????????\n"+
                "?????????\n"+
                "?????????\n"+
                "?????????\n"+
                "?????????\n"+
                "?????????\n"
                , actual);
    }
    
    @Test
    public void testFormatWithFullMatrix() {
        GameMatrixImpl matrix = new GameMatrixImpl();
        matrix.setAll(GameMatrixImpl.parse(
                "294731856",
                "781465239",
                "536829741",
                "673158924",
                "819246573",
                "425973168",
                "358612497",
                "942387615",
                "167594382"));
        PlainTextFormatter formatter = new PlainTextFormatter();
        formatter.setUnknownCellContentCharacter("?");
        String actual = formatter.format(matrix);
        assertEquals(
                "294731856\n"+
                "781465239\n"+
                "536829741\n"+
                "673158924\n"+
                "819246573\n"+
                "425973168\n"+
                "358612497\n"+
                "942387615\n"+
                "167594382\n"
                , actual);
    }    
}
