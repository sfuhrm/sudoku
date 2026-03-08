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

import java.util.Arrays;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link CachedGameMatrixImpl}.
 * @author Stephan Fuhrmann
 */
public class CachedGameMatrixImplTest extends CommonGameMatrixImplTest {

    public final static String[] FULL_EXAMPLE
            = new String[]{
                "3 5 9 1 6 2 4 8 7",
                "4 1 2 8 3 7 6 5 9",
                "6 8 7 5 9 4 1 2 3",
                "8 7 6 4 5 9 3 1 2",
                "9 4 1 6 2 3 8 7 5",
                "5 2 3 7 1 8 9 4 6",
                "2 3 4 9 8 5 7 6 1",
                "7 6 5 3 4 1 2 9 8",
                "1 9 8 2 7 6 5 3 4"};

    @Override
    protected CachedGameMatrixImpl newInstance(GameSchema gameSchema) {
        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(gameSchema);
        return matrix;
    }

    @Test
    public void testClone() {
        byte[][] data =
                QuadraticArrays.parse(FULL_EXAMPLE);

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(GameSchemas.SCHEMA_9X9);
        matrix.setAll(data);
        CachedGameMatrixImpl clone = matrix.clone();

        String out = clone.toString();
        String expected = Arrays.stream(FULL_EXAMPLE)
                .collect(Collectors.joining("\n"))+"\n";
        assertEquals(expected, out);
     }
}
