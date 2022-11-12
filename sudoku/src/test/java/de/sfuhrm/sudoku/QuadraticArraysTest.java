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
 * Test for {@link QuadraticArrays}.
 * @author Stephan Fuhrmann
 */
public class QuadraticArraysTest {

    /** Array size for tests. */
    private static final int TEST_SIZE = 9;

    @Test
    public void testCloneArrayWithBooleanAndSizeFour() {
        boolean[][] actual = new boolean[4][4];
        boolean[][] clone = QuadraticArrays.cloneArray(actual);
        assertNotSame(clone, actual);
        assertEquals(clone.length, actual.length);
        assertEquals(clone[0].length, actual[0].length);
    }

    @Test
    public void testCloneArrayWithBooleanAndSizeNine() {
        boolean[][] actual = new boolean[TEST_SIZE][TEST_SIZE];
        boolean[][] clone = QuadraticArrays.cloneArray(actual);
        assertNotSame(clone, actual);
        assertEquals(clone.length, actual.length);
        assertEquals(clone[0].length, actual[0].length);
    }

    @Test
    public void testCloneArrayWithByte() {
        byte[][] actual = new byte[TEST_SIZE][TEST_SIZE];
        byte[][] clone = QuadraticArrays.cloneArray(actual);
        assertNotSame(clone, actual);
        assertEquals(clone.length, actual.length);
        assertEquals(clone[0].length, actual[0].length);
    }

    @Test
    public void testCloneArrayWithInt() {
        int[][] actual = new int[TEST_SIZE][TEST_SIZE];
        int[][] clone = QuadraticArrays.cloneArray(actual);
        assertNotSame(clone, actual);
        assertEquals(clone.length, actual.length);
        assertEquals(clone[0].length, actual[0].length);
    }
}
