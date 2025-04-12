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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link RiddleImpl}.
 * @author Stephan Fuhrmann
 */
public class RiddleImplTest {

    private final GameSchema schema = GameSchemas.SCHEMA_9X9;

    public final static String[] FULL_EXAMPLE
            = new String[]{
                "359162487",
                "412837659",
                "687594123",
                "876459312",
                "941623875",
                "523718946",
                "234985761",
                "765341298",
                "198276534"};


    @Test
    public void testNew() {
        RiddleImpl m = new RiddleImpl(schema);
        assertEquals(0, m.getSetCount());
    }

    @Test
    public void testClone() {
        RiddleImpl m = new RiddleImpl(schema);
        m.setAll(QuadraticArrays.parse(FULL_EXAMPLE));

        RiddleImpl clone = m.clone();

        assertNotSame(clone, m);
        assertEquals(clone, m);
    }
}
