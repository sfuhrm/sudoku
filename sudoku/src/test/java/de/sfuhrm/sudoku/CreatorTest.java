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
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link Creator}.
 * @author Stephan Fuhrmann
 */
public class CreatorTest {

    private final GameSchema schema = GameSchemas.SCHEMA_9X9;

    @Test
    public void testGetSetBitOffsetWithNothingSet1() {
        int v = Creator.getSetBitOffset(0, 0);
        assertEquals(-1, v);
    }

    @Test
    public void testGetSetBitOffsetWithNothingSet2() {
        int v = Creator.getSetBitOffset(0, 1);
        assertEquals(-1, v);
    }

    @Test
    public void testGetSetBitOffsetWithTwoSet1() {
        int v = Creator.getSetBitOffset(3, 0);
        assertEquals(0, v);
    }

    @Test
    public void testGetSetBitOffsetWithTwoSet2() {
        int v = Creator.getSetBitOffset(3, 1);
        assertEquals(1, v);
    }

    @Test
    public void testGetSetBitOffsetWithTreeSet1() {
        int v = Creator.getSetBitOffset(0x111, 0);
        assertEquals(0, v);
    }

    @Test
    public void testGetSetBitOffsetWithTreeSet2() {
        int v = Creator.getSetBitOffset(0x111, 1);
        assertEquals(4, v);
    }

    @Test
    public void testGetSetBitOffsetWithTreeSet3() {
        int v = Creator.getSetBitOffset(0x111, 2);
        assertEquals(8, v);
    }

    @Test
    public void testGetSetBitOffsetWithTreeSet4() {
        int v = Creator.getSetBitOffset(0x111, 3);
        assertEquals(-1, v);
    }

    @Test
    public void testGetSetBitOffsetWithAll() {
        int v;

        v = Creator.getSetBitOffset(0xffffffff, 0);
        assertEquals(0, v);
        v = Creator.getSetBitOffset(0xffffffff, 1);
        assertEquals(1, v);
        v = Creator.getSetBitOffset(0xffffffff, 2);
        assertEquals(2, v);
        v = Creator.getSetBitOffset(0xffffffff, 3);
        assertEquals(3, v);
        v = Creator.getSetBitOffset(0xffffffff, 4);
        assertEquals(4, v);
        v = Creator.getSetBitOffset(0xffffffff, 5);
        assertEquals(5, v);
        v = Creator.getSetBitOffset(0xffffffff, 6);
        assertEquals(6, v);
        v = Creator.getSetBitOffset(0xffffffff, 7);
        assertEquals(7, v);
        v = Creator.getSetBitOffset(0xffffffff, 8);
        assertEquals(8, v);
        v = Creator.getSetBitOffset(0xffffffff, 31);
        assertEquals(31, v);
        v = Creator.getSetBitOffset(0xffffffff, 32);
        assertEquals(-1, v);
    }


    @Test
    public void testCreateFull() {
        GameMatrix r = Creator.createFull(schema);
        assertEquals(9*9, r.getSetCount());
        assertTrue(r.isValid());
    }

    @Test
    public void testCreateFullWithMultipleInvocations() {
//        for (int i=0; i < 100000; i++) {
        for (int i=0; i < 1000; i++) {
            GameMatrix r = Creator.createFull();
            assertEquals(9*9, r.getSetCount());
            assertTrue(r.isValid());
        }
    }

    @Test
    public void testCreateRiddle() {
        GameMatrix matrix = Creator.createFull();
        GameSchema schema = matrix.getSchema();
        Riddle riddle = Creator.createRiddle(matrix);
        for (int i=0; i < schema.getWidth(); i++) {
            for (int j=0; j < schema.getWidth(); j++) {
                if (riddle.get(i, j) != schema.getUnsetValue()) {
                    // all fields that are set are needed to be the same
                    assertEquals(matrix.get(i, j), riddle.get(i, j));
                    assertFalse(riddle.getWritable(i, j));
                } else {
                    assertTrue(riddle.getWritable(i, j));
                }
            }
        }

        // there can be only one
        Solver solver = new Solver(riddle);
        solver.setLimit(3);
        List<GameMatrix> results = solver.solve();
        assertEquals(1, results.size());
    }

    @Test
    public void testCreateRiddleWithOne() {
        GameMatrix matrix = Creator.createFull();
        GameSchema schema = matrix.getSchema();
        Riddle riddle = Creator.createRiddle(matrix, 1);
        int unsetCount = 0;
        for (int i=0; i < schema.getWidth(); i++) {
            for (int j=0; j < schema.getWidth(); j++) {
                if (riddle.get(i, j) == schema.getUnsetValue()) {
                    unsetCount++;
                }
            }
        }
        assertEquals(1, unsetCount);

        Solver solver = new Solver(riddle);
        solver.setLimit(3);
        List<GameMatrix> results = solver.solve();
        assertEquals(1, results.size());
    }

    @Test
    public void testCreateNumbersToDistributeWithOnce() {
        GameSchema schema = GameSchemas.SCHEMA_9X9;
        byte[] v = Creator.createNumbersToDistribute(schema, new Random(), 1);
        List<Integer> intList = Utility.toIntList(v);

        assertEquals(9, intList.size());
        assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9), intList);
    }

    @Test
    public void testCreateNumbersToDistributeWithTwice() {
        GameSchema schema = GameSchemas.SCHEMA_9X9;
        byte[] v = Creator.createNumbersToDistribute(schema, new Random(), 2);
        List<Integer> intList = Utility.toIntList(v);

        assertEquals(2*9, intList.size());
        assertEquals(Arrays.asList(1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9), intList);
    }

    @Test
    public void testCreateVariantWithEmpty() {
        GameMatrixImpl original = new GameMatrixImpl(schema);
        assertThrows(IllegalArgumentException.class, () -> {
            Creator.createVariant(original);
        });
    }

    @Test
    public void testCreateVariant() {
        GameMatrix original = Creator.createFull(schema);
        GameMatrix variant1 = Creator.createVariant(original);
        GameMatrix variant2 = Creator.createVariant(original);
        GameMatrix variant3 = Creator.createVariant(original);

        assertTrue(original.isValid());
        assertEquals(9*9, original.getSetCount());
        assertTrue(variant1.isValid());
        assertEquals(9*9, variant1.getSetCount());
        assertTrue(variant2.isValid());
        assertEquals(9*9, variant2.getSetCount());
        assertTrue(variant3.isValid());
        assertEquals(9*9, variant3.getSetCount());
    }

    @Test
    public void testSwapColumnWithFirstAndSecond() {
        GameMatrixImpl actual = new GameMatrixImpl(schema);
        actual.setAll(QuadraticArrays.parse(
                "100000000",
                "020100000",
                "000320100",
                "010000456",
                "000010000",
                "000000010",
                "001000000",
                "000001000",
                "000000001"
                ));

        GameMatrixImpl expected = new GameMatrixImpl(schema);
        expected.setAll(QuadraticArrays.parse(
                "010000000",
                "200100000",
                "000320100",
                "100000456",
                "000010000",
                "000000010",
                "001000000",
                "000001000",
                "000000001"
                ));

        Creator.swapColumn(actual, 0, 1);

        assertEquals(expected, actual);
     }

    @Test
    public void testSwapColumnWithFirstAndLast() {
        GameMatrixImpl actual = new GameMatrixImpl(schema);
        actual.setAll(QuadraticArrays.parse(
                "100000000",
                "020100000",
                "000320100",
                "010000456",
                "000010000",
                "000000010",
                "001000000",
                "000001000",
                "000000001"
                ));

        GameMatrixImpl expected = new GameMatrixImpl(schema);
        expected.setAll(QuadraticArrays.parse(
                "000000001",
                "020100000",
                "000320100",
                "610000450",
                "000010000",
                "000000010",
                "001000000",
                "000001000",
                "100000000"
                ));

        Creator.swapColumn(actual, 0, 8);

        assertEquals(expected, actual);
     }

    @Test
    public void testSwapRowWithFirstAndSecond() {
        GameMatrixImpl actual = new GameMatrixImpl(schema);
        actual.setAll(QuadraticArrays.parse(
                "100000000",
                "020100000",
                "000320100",
                "010000456",
                "000010000",
                "000000010",
                "001000000",
                "000001000",
                "000000001"
                ));

        GameMatrixImpl expected = new GameMatrixImpl(schema);
        expected.setAll(QuadraticArrays.parse(
                "020100000",
                "100000000",
                "000320100",
                "010000456",
                "000010000",
                "000000010",
                "001000000",
                "000001000",
                "000000001"
                ));

        Creator.swapRow(actual, 0, 1);

        assertEquals(expected, actual);
     }

    @Test
    public void testSwapRowWithFirstAndLast() {
        GameMatrixImpl actual = new GameMatrixImpl(schema);
        actual.setAll(QuadraticArrays.parse(
                "100000000",
                "020100000",
                "000320100",
                "010000456",
                "000010000",
                "000000010",
                "001000000",
                "000001000",
                "000000001"
                ));

        GameMatrixImpl expected = new GameMatrixImpl(schema);
        expected.setAll(QuadraticArrays.parse(
                "000000001",
                "020100000",
                "000320100",
                "010000456",
                "000010000",
                "000000010",
                "001000000",
                "000001000",
                "100000000"
                ));

        Creator.swapRow(actual, 0, 8);

        assertEquals(expected, actual);
     }

     @Test
     public void createFullInLoop() {
        GameSchema four = GameSchemas.SCHEMA_4X4;
        for (int i = 0; i < 100; i++) {
            Creator.createFull(four);
        }
     }
}
