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
import org.json.JSONArray;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link LatexTableFormatter}.
 *
 * @author Stephan Fuhrmann
 */
public class JsonArrayFormatterTest extends AbstractTextFormatterTest {

    @Override
    AbstractTextFormatter newInstance() {
        return new JsonArrayFormatter();
    }

    @Test
    @Override
    public void testNew() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals("\n", formatter.getRowSeparator());
        assertEquals("0", formatter.getUnknownCellContentCharacter());
    }

    @Test
    @Override
    public void testGetUnknownCellContentCharacter() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals("0", formatter.getUnknownCellContentCharacter());
    }

    @Test
    public void testFormatWithOneMatrixWithoutIndent() {
        JsonArrayFormatter formatter = (JsonArrayFormatter) newInstance();
        formatter.setIndent(false);
        GameMatrix gameMatrix = gameMatrixFactory.newGameMatrix();

        for (int i = 0; i < gameMatrix.getSchema().getWidth(); i++) {
            gameMatrix.set(0, i, (byte)i);
        }

        String actual = formatter.documentStart()
                + formatter.format(gameMatrix)
                + formatter.documentEnd();

        JSONArray allMatrices = new JSONArray(actual);
        assertEquals(1, allMatrices.length());
        JSONArray firstMatrix = allMatrices.getJSONArray(0);

        assertEquals(gameMatrix.getSchema().getWidth(), firstMatrix.length());

        for (int row = 0; row < gameMatrix.getSchema().getWidth(); row++) {
            JSONArray rowArray = firstMatrix.getJSONArray(row);
            assertEquals(gameMatrix.getSchema().getWidth(), rowArray.length());

            for (int column = 0; column < gameMatrix.getSchema().getWidth(); column++) {
                int element = rowArray.getInt(column);
                assertEquals(gameMatrix.get(row, column), element);
            }
        }
    }

    @Test
    public void testFormatWithOneMatrixWithtIndent() {
        JsonArrayFormatter formatter = (JsonArrayFormatter) newInstance();
        formatter.setIndent(true);
        GameMatrix gameMatrix = gameMatrixFactory.newGameMatrix();

        for (int i = 0; i < gameMatrix.getSchema().getWidth(); i++) {
            gameMatrix.set(0, i, (byte)i);
        }

        String actual = formatter.documentStart()
                + formatter.format(gameMatrix)
                + formatter.documentEnd();

        JSONArray allMatrices = new JSONArray(actual);
        assertEquals(1, allMatrices.length());
        JSONArray firstMatrix = allMatrices.getJSONArray(0);

        assertEquals(gameMatrix.getSchema().getWidth(), firstMatrix.length());

        for (int row = 0; row < gameMatrix.getSchema().getWidth(); row++) {
            JSONArray rowArray = firstMatrix.getJSONArray(row);
            assertEquals(gameMatrix.getSchema().getWidth(), rowArray.length());

            for (int column = 0; column < gameMatrix.getSchema().getWidth(); column++) {
                int element = rowArray.getInt(column);
                assertEquals(gameMatrix.get(row, column), element);
            }
        }
    }

    @Test
    public void testFormatWithTwoMatrices() {
        JsonArrayFormatter formatter = (JsonArrayFormatter) newInstance();
        formatter.setIndent(true);
        GameMatrix gameMatrix = gameMatrixFactory.newGameMatrix();

        String actual = formatter.documentStart()
                + formatter.format(gameMatrix)
                + formatter.format(gameMatrix)
                + formatter.documentEnd();

        JSONArray allMatrices = new JSONArray(actual);
        assertEquals(2, allMatrices.length());
        JSONArray secondMatrix = allMatrices.getJSONArray(1);
        assertEquals(gameMatrix.getSchema().getWidth(), secondMatrix.length());
        JSONArray firstRow = secondMatrix.getJSONArray(0);
        assertEquals(gameMatrix.getSchema().getWidth(), firstRow.length());
        int element = firstRow.getInt(0);
        assertEquals(0, element);
    }
}
