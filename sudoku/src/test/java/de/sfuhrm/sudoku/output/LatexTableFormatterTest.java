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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link LatexTableFormatter}.
 * @author Stephan Fuhrmann
 */
public class LatexTableFormatterTest extends AbstractTextFormatterTest {
    @Override
    AbstractTextFormatter newInstance() {
        return new LatexTableFormatter();
    }

    @Test
    @Override
    public void testNew() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals("\n", formatter.getRowSeparator());
        assertEquals(" ", formatter.getUnknownCellContentCharacter());
        assertNotEquals("", formatter.documentStart());
        assertNotEquals("", formatter.documentEnd());
    }

    @Test
    @Override
    public void testGetUnknownCellContentCharacter() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals(" ", formatter.getUnknownCellContentCharacter());
    }

    @Test
    public void testFormat() {
        AbstractTextFormatter formatter = newInstance();
        GameMatrix gameMatrix = gameMatrixFactory.newGameMatrix();

        // this test sucks, but it's better than nothing
        String actual = formatter.format(gameMatrix);
        assertTrue(actual.contains("\\begin"));
    }
}
