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
        CachedGameMatrixImpl m = new CachedGameMatrixImpl();
        assertEquals(0, m.getSetCount());
    }

    @Test
    public void testGet() {
        GameMatrix matrix = new CachedGameMatrixImpl();
        byte value = matrix.get(0, 0);
        assertEquals(GameMatrix.UNSET, value);
        value = matrix.get(8, 8);
        assertEquals(GameMatrix.UNSET, value);
    }

    @Test
    public void testSet() {
        GameMatrix matrix = new CachedGameMatrixImpl();
        byte value = matrix.get(0, 0);
        assertEquals(GameMatrix.UNSET, value);
        matrix.set(0,0,(byte)4);
        value = matrix.get(0, 0);
        assertEquals(4, value);
    }

    @Test
    public void testSetAll() {
        byte[][] data =
                QuadraticArrays.parse(FULL_EXAMPLE);

        GameMatrixImpl matrix = new CachedGameMatrixImpl();
        matrix.setAll(data);

        for (int i=0; i < GameMatrix.SIZE; i++) {
           for (int j=0; j < GameMatrix.SIZE; j++) {
                assertEquals(data[i][j], matrix.get(i, j));
           }
        }
    }

    @Test
    public void testClone() {
        byte[][] data =
                QuadraticArrays.parse(FULL_EXAMPLE);

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();
        matrix.setAll(data);
        CachedGameMatrixImpl clone = (CachedGameMatrixImpl) matrix.clone();

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

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();
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

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();
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

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();
        matrix.setAll(data);
        int mask = matrix.getRowFreeMask(0);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~(1<<1)), mask);
        mask = matrix.getRowFreeMask(1);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getRowFreeMask(2);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getRowFreeMask(3);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
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

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();
        matrix.setAll(data);
        int mask = matrix.getColumnFreeMask(0);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~(1<<1)), mask);
        mask = matrix.getColumnFreeMask(1);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getColumnFreeMask(2);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1))), mask);
        mask = matrix.getColumnFreeMask(3);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<3))), mask);
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

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();
        matrix.setAll(data);
        int mask = matrix.getBlockFreeMask(0,0);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getBlockFreeMask(0,3);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getBlockFreeMask(0,6);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1))), mask);
        mask = matrix.getBlockFreeMask(3,6);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
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

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();
        matrix.setAll(data);
        int mask = matrix.getFreeMask(0,0);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getFreeMask(0,3);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getFreeMask(0,6);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<4))), mask);
        mask = matrix.getFreeMask(3,6);
        assertEquals(BitFreeMatrixInterface.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
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

        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();
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
        CachedGameMatrixImpl matrix = new CachedGameMatrixImpl();

        for (int row = 0; row < GameMatrix.SIZE; row++) {
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                matrix.set(row, column, GameMatrix.UNSET);
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

        matrix.set(1, 2, GameMatrix.UNSET);
        set--;
        assertEquals(set,  matrix.getSetCount());
     }
}
