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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link MarkdownTableFormatter}.
 * @author Stephan Fuhrmann
 */
public class MarkdownTableFormatterTest extends AbstractTextFormatterTest {

    @Override
    AbstractTextFormatter newInstance() {
        return new MarkdownTableFormatter();
    }

    @Test
    @Override
    public void testNew() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals("\n", formatter.getRowSeparator());
        assertEquals(".", formatter.getUnknownCellContentCharacter());
    }

    @Test
    public void testFormatWithEmpty() {
        GameMatrix matrix = gameMatrixFactory.newGameMatrix();
        String actual = newInstance().format(matrix);
        assertEquals(
"|  |  |  |  |  |  |  |  |  |\n"+
"|---|---|---|---|---|---|---|---|---|\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"| . | . | . | . | . | . | . | . | . |\n"+
"|  |  |  |  |  |  |  |  |  |\n"
                , actual);
    }

    @Test
    public void testFormatWithEmptyAndOtherUnknownCharacter() {
        GameMatrix matrix = gameMatrixFactory.newGameMatrix();
        AbstractTextFormatter formatter = newInstance();
        formatter.setUnknownCellContentCharacter("?");
        String actual = formatter.format(matrix);
        assertEquals(
"|  |  |  |  |  |  |  |  |  |\n"+
"|---|---|---|---|---|---|---|---|---|\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"| ? | ? | ? | ? | ? | ? | ? | ? | ? |\n"+
"|  |  |  |  |  |  |  |  |  |\n"
                , actual);
    }

    @Test
    public void testFormatWithFullMatrix() {
        GameMatrix matrix = gameMatrixFactory.newGameMatrix();
        matrix.setAll(QuadraticArrays.parse(
                "294731856",
                "781465239",
                "536829741",
                "673158924",
                "819246573",
                "425973168",
                "358612497",
                "942387615",
                "167594382"));
        AbstractTextFormatter formatter = newInstance();
        formatter.setUnknownCellContentCharacter("?");
        String actual = formatter.format(matrix);
        assertEquals(
"|  |  |  |  |  |  |  |  |  |\n"+
"|---|---|---|---|---|---|---|---|---|\n"+
"| 2 | 9 | 4 | 7 | 3 | 1 | 8 | 5 | 6 |\n"+
"| 7 | 8 | 1 | 4 | 6 | 5 | 2 | 3 | 9 |\n"+
"| 5 | 3 | 6 | 8 | 2 | 9 | 7 | 4 | 1 |\n"+
"| 6 | 7 | 3 | 1 | 5 | 8 | 9 | 2 | 4 |\n"+
"| 8 | 1 | 9 | 2 | 4 | 6 | 5 | 7 | 3 |\n"+
"| 4 | 2 | 5 | 9 | 7 | 3 | 1 | 6 | 8 |\n"+
"| 3 | 5 | 8 | 6 | 1 | 2 | 4 | 9 | 7 |\n"+
"| 9 | 4 | 2 | 3 | 8 | 7 | 6 | 1 | 5 |\n"+
"| 1 | 6 | 7 | 5 | 9 | 4 | 3 | 8 | 2 |\n"+
"|  |  |  |  |  |  |  |  |  |\n"
                , actual);
    }
}
