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
public class CachedGameMatrixImplTest {

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


    @Test
    public void testNew() {
        CachedGameMatrixImpl m = new CachedGameMatrixImpl(GameSchemas.SCHEMA_9X9);
        assertEquals(0, m.getSetCount());
    }

    @Test
    public void testGet() {
        GameMatrix matrix = new CachedGameMatrixImpl(GameSchemas.SCHEMA_9X9);
        byte value = matrix.get(0, 0);
        assertEquals(matrix.getSchema().getUnsetValue(), value);
        value = matrix.get(8, 8);
        assertEquals(matrix.getSchema().getUnsetValue(), value);
    }

    @Test
    public void testSet() {
        GameMatrix matrix = new CachedGameMatrixImpl(GameSchemas.SCHEMA_9X9);
        byte value = matrix.get(0, 0);
        assertEquals(matrix.getSchema().getUnsetValue(), value);
        matrix.set(0,0,(byte)4);
        value = matrix.get(0, 0);
        assertEquals(4, value);
    }

    @Test
    public void testSetAll() {
        byte[][] data =
                QuadraticArrays.parse(FULL_EXAMPLE);

        GameMatrixImpl matrix = new CachedGameMatrixImpl(GameSchemas.SCHEMA_9X9);
        matrix.setAll(data);

        for (int i = 0; i < matrix.getSchema().getWidth(); i++) {
           for (int j = 0; j < matrix.getSchema().getWidth(); j++) {
                assertEquals(data[i][j], matrix.get(i, j));
           }
        }
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

    @Test
    public void testIsValidWithEmptyValid() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000"
                );

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(GameSchemas.SCHEMA_9X9);
        matrix.setAll(data);
        assertTrue(matrix.isValid());
     }

    @Test
    public void testIsValidWithPartlyFullValid() {
        byte[][] data =
                QuadraticArrays.parse(
                        "100000000",
                        "000100000",
                        "000000100",
                        "010000000",
                        "000010000",
                        "000000010",
                        "001000000",
                        "000001000",
                        "000000001"
                );

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(GameSchemas.SCHEMA_9X9);
        matrix.setAll(data);
        assertTrue(matrix.isValid());
     }

    @Test
    public void testGetRowFreeMask() {
        byte[][] data =
                QuadraticArrays.parse(
                        "100000000",
                        "020100000",
                        "000320100",
                        "010000456",
                        "000010000",
                        "000000010",
                        "001000000",
                        "000001000",
                        "000000001"
                );

        GameSchema schema = GameSchemas.SCHEMA_9X9;
        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(schema);
        matrix.setAll(data);
        int mask = matrix.getRowFreeMask(0);
        assertEquals(schema.getBitMask() & (~(1<<1)), mask);
        mask = matrix.getRowFreeMask(1);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getRowFreeMask(2);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getRowFreeMask(3);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
     }

    @Test
    public void testGetColumnFreeMask() {
        byte[][] data =
                QuadraticArrays.parse(
                        "100000000",
                        "020100000",
                        "000320100",
                        "010000456",
                        "000010000",
                        "000000010",
                        "001000000",
                        "000001000",
                        "000000001"
                );

        GameSchema schema = GameSchemas.SCHEMA_9X9;
        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(schema);
        matrix.setAll(data);
        int mask = matrix.getColumnFreeMask(0);
        assertEquals(schema.getBitMask() & (~(1<<1)), mask);
        mask = matrix.getColumnFreeMask(1);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getColumnFreeMask(2);
        assertEquals(schema.getBitMask() & (~((1<<1))), mask);
        mask = matrix.getColumnFreeMask(3);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<3))), mask);
     }

    @Test
    public void testGetBlockFreeMask() {
        byte[][] data =
                QuadraticArrays.parse(
                        "100000000",
                        "020100000",
                        "000320100",
                        "010000456",
                        "000010000",
                        "000000010",
                        "001000000",
                        "000001000",
                        "000000001"
                );

        GameSchema schema = GameSchemas.SCHEMA_9X9;
        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(schema);
        matrix.setAll(data);
        int mask = matrix.getBlockFreeMask(0,0);
        // all except { 1, 2 }
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getBlockFreeMask(0,3);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getBlockFreeMask(0,6);
        assertEquals(schema.getBitMask() & (~((1<<1))), mask);
        mask = matrix.getBlockFreeMask(3,6);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
     }

    @Test
    public void testGetFreeMask() {
        byte[][] data =
                QuadraticArrays.parse(
                        "100000000",
                        "020100000",
                        "000320100",
                        "010000456",
                        "000010000",
                        "000000010",
                        "001000000",
                        "000001000",
                        "000000001"
                );

        GameSchema schema = GameSchemas.SCHEMA_9X9;
        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(schema);
        matrix.setAll(data);
        int mask = matrix.getFreeMask(0,0);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getFreeMask(0,3);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getFreeMask(0,6);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<4))), mask);
        mask = matrix.getFreeMask(3,6);
        assertEquals(schema.getBitMask() & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
     }

    @Test
    public void testCanSet() {
        byte[][] data =
                QuadraticArrays.parse(
                        //   x
                        "100000000",
                        "020100000",
                        "000320100",
                        "010000456",
                        //           y
                        "000010000",
                        "000000010",
                        "001000000",
                        "000001000",
                        "000000001"
                );

        GameSchema schema = GameSchemas.SCHEMA_9X9;
        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(schema);
        matrix.setAll(data);

        // the "x" cell marked above
        assertTrue(matrix.canSet(0, 0, (byte) 0)); // always works
        assertFalse(matrix.canSet(0, 0, (byte) 2)); // in block
        assertTrue(matrix.canSet(0, 0, (byte) 3)); // not in block

        // the "y" cell marked above
        assertTrue(matrix.canSet(4, 8, (byte) 0)); // always works
        assertTrue(matrix.canSet(4, 8, (byte) 2)); // in block
        assertTrue(matrix.canSet(4, 8, (byte) 3)); // not in block
     }

    @Test
    public void testGetSetCount() {
        GameSchema schema = GameSchemas.SCHEMA_9X9;
        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl(schema);

        for (int row = 0; row < matrix.getSchema().getWidth(); row++) {
            for (int column = 0; column < matrix.getSchema().getWidth(); column++) {
                matrix.set(row, column, matrix.getSchema().getUnsetValue());
                assertEquals(0,  matrix.getSetCount());
            }
        }

        int set = 0;
        matrix.set(0, 0, (byte)1);
        set++;
        assertEquals(set,  matrix.getSetCount());

        matrix.set(1, 1, (byte)2);
        set++;
        assertEquals(set,  matrix.getSetCount());

        matrix.set(1, 2, (byte)3);
        set++;
        assertEquals(set,  matrix.getSetCount());

        matrix.set(1, 2, matrix.getSchema().getUnsetValue());
        set--;
        assertEquals(set,  matrix.getSetCount());
     }
}
