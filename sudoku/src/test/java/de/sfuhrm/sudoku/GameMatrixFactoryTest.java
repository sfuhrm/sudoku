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
 * Test for {@link GameMatrixFactory}.
 * @author Stephan Fuhrmann
 */
public class GameMatrixFactoryTest {

    @Test
    public void testNewGameMatrix() {
        GameMatrixFactory instance = new GameMatrixFactory();
        GameMatrix expResult = new GameMatrixImpl(GameSchemas.SCHEMA_9X9);
        GameMatrix result = instance.newGameMatrix();
        assertEquals(expResult, result);
    }

    @Test
    public void testNewGameMatrixWithSchema() {
        GameMatrixFactory instance = new GameMatrixFactory();
        GameMatrix expResult = new GameMatrixImpl(GameSchemas.SCHEMA_4X4);
        GameMatrix result = instance.newGameMatrix(GameSchemas.SCHEMA_4X4);
        assertEquals(expResult, result);
    }

    @Test
    public void testNewRiddle() {
        GameMatrixFactory instance = new GameMatrixFactory();
        RiddleImpl expResult = new RiddleImpl(GameSchemas.SCHEMA_9X9);
        GameMatrix result = instance.newRiddle();
        assertEquals(expResult, result);
    }

    @Test
    public void testNewRiddleWithSchema() {
        GameMatrixFactory instance = new GameMatrixFactory();
        RiddleImpl expResult = new RiddleImpl(GameSchemas.SCHEMA_4X4);
        GameMatrix result = instance.newRiddle(GameSchemas.SCHEMA_4X4);
        assertEquals(expResult, result);
    }
}
