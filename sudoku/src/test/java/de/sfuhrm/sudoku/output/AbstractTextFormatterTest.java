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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link AbstractTextFormatter}.
 * @author Stephan Fuhrmann
 */
public class AbstractTextFormatterTest {

    protected GameMatrixFactory gameMatrixFactory;

    @Before
    public void before() {
        gameMatrixFactory = new GameMatrixFactory();
    }

    AbstractTextFormatter newInstance() {
        return new AbstractTextFormatter() {
            @Override
            public String format(GameMatrix matrix) {
                return "";
            }
        };
    }

    @Test
    public void testNew() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals("\n", formatter.getLineSeparator());
        assertEquals(".", formatter.getUnknownCellContentCharacter());
        assertEquals("", formatter.documentStart());
        assertEquals("", formatter.documentEnd());
    }

    @Test
    public void testGetLineSeparator() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals("\n", formatter.getLineSeparator());
    }

    @Test
    public void testSetLineSeparator() {
        AbstractTextFormatter formatter = newInstance();
        formatter.setLineSeparator("\r\n");
        assertEquals("\r\n", formatter.getLineSeparator());
    }

    @Test
    public void testGetUnknownCellContentCharacter() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals(".", formatter.getUnknownCellContentCharacter());
    }

    @Test
    public void testSetUnknownCellContentCharacter() {
        AbstractTextFormatter formatter = newInstance();
        formatter.setUnknownCellContentCharacter("?");
        assertEquals("?", formatter.getUnknownCellContentCharacter());
    }
}
