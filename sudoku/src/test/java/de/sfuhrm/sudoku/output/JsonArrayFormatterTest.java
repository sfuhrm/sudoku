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
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test for {@link LatexTableFormatter}.
 *
 * @author Stephan Fuhrmann
 */
public class JsonArrayFormatterTest extends AbstractTextFormatterTest {

    @Override
    protected AbstractTextFormatter newInstance() {
        return new JsonArrayFormatter();
    }

    @Test
    @Override
    public void testNew() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals("\n", formatter.getLineSeparator());
        assertEquals("0", formatter.getUnknownCellContentCharacter());
    }

    @Test
    @Override
    public void testGetUnknownCellContentCharacter() {
        AbstractTextFormatter formatter = newInstance();
        assertEquals("0", formatter.getUnknownCellContentCharacter());
    }

    @Test
    public void testFormatWithOneMatrix() {
        AbstractTextFormatter formatter = newInstance();
        GameMatrix gameMatrix = new GameMatrix();

        for (int i = 0; i < GameMatrix.SIZE; i++) {
            gameMatrix.set(0, i, (byte)i);
        }

        String actual = formatter.documentStart()
                + formatter.format(gameMatrix)
                + formatter.documentEnd();
        
        JSONArray allMatrices = new JSONArray(actual);
        assertEquals(1, allMatrices.length());
        JSONArray firstMatrix = allMatrices.getJSONArray(0);
        
        assertEquals(GameMatrix.SIZE, firstMatrix.length());
        
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            JSONArray rowArray = firstMatrix.getJSONArray(row);
            assertEquals(GameMatrix.SIZE, rowArray.length());
            
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                int element = rowArray.getInt(column);
                assertEquals(gameMatrix.get(row, column), element);
            }
        }
    }
    
    @Test
    public void testFormatWithTwoMatrices() {
        AbstractTextFormatter formatter = newInstance();
        GameMatrix gameMatrix = new GameMatrix();

        String actual = formatter.documentStart()
                + formatter.format(gameMatrix)
                + formatter.format(gameMatrix)
                + formatter.documentEnd();
        
        JSONArray allMatrices = new JSONArray(actual);
        assertEquals(2, allMatrices.length());
        JSONArray secondMatrix = allMatrices.getJSONArray(1);
        assertEquals(GameMatrix.SIZE, secondMatrix.length());
        JSONArray firstRow = secondMatrix.getJSONArray(0);
        assertEquals(GameMatrix.SIZE, firstRow.length());
        int element = firstRow.getInt(0);
        assertEquals(0, element);
    }
}
