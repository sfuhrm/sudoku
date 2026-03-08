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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link GameMatrixImpl}.
 * @author Stephan Fuhrmann
 */
public class GameMatrixImplTest extends CommonGameMatrixImplTest {

    private final GameSchema schema = GameSchemas.SCHEMA_9X9;

    protected GameMatrixImpl newInstance(GameSchema gameSchema) {
        GameMatrixImpl matrix = new GameMatrixImpl(gameSchema);
        return matrix;
    }

    @Test
    public void testRow() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "111111111",
                        "222222222",
                        "333333333",
                        "444444444",
                        "555555555",
                        "666666666",
                        "777777777",
                        "888888888"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);

        byte[] target = new byte[9];

        for (int i=0; i < schema.getWidth(); i++) {
           for (int j=0; j < schema.getWidth(); j++) {
                matrix.row(i, target);
                List<Integer> vals = Utility.toIntList(target);
                assertEquals(Arrays.asList(i,i,i,i,i,i,i,i,i), vals);
           }
        }
     }

    @Test
    public void testColumn() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "111111111",
                        "222222222",
                        "333333333",
                        "444444444",
                        "555555555",
                        "666666666",
                        "777777777",
                        "888888888"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);

        byte[] target = new byte[9];

        for (int i=0; i < schema.getWidth(); i++) {
           for (int j=0; j < schema.getWidth(); j++) {
                matrix.column(i, target);
                List<Integer> vals = Utility.toIntList(target);
                assertEquals(Arrays.asList(0,1,2,3,4,5,6,7,8), vals);
           }
        }
     }

    @Test
    public void testBlock() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "111111111",
                        "222222222",
                        "333333333",
                        "444444444",
                        "555555555",
                        "666666666",
                        "777777777",
                        "888888888"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);

        byte[] target = new byte[9];

        matrix.block(0,0, target);
        List<Integer> vals = Utility.toIntList(target);
        assertEquals(Arrays.asList(0,0,0,1,1,1,2,2,2), vals);

        matrix.block(0,6, target);
        vals = Utility.toIntList(target);
        assertEquals(Arrays.asList(0,0,0,1,1,1,2,2,2), vals);

        matrix.block(6,6, target);
        vals = Utility.toIntList(target);
        assertEquals(Arrays.asList(6,6,6,7,7,7,8,8,8), vals);
     }

    @Test
    public void testToString() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "111111111",
                        "222222222",
                        "333333333",
                        "444444444",
                        "555555555",
                        "666666666",
                        "777777777",
                        "888888888"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);

        String out = matrix.toString();
        assertEquals("_ _ _ _ _ _ _ _ _\n"+
                "1 1 1 1 1 1 1 1 1\n"+
                "2 2 2 2 2 2 2 2 2\n"+
                "3 3 3 3 3 3 3 3 3\n"+
                "4 4 4 4 4 4 4 4 4\n"+
                "5 5 5 5 5 5 5 5 5\n"+
                "6 6 6 6 6 6 6 6 6\n"+
                "7 7 7 7 7 7 7 7 7\n"+
                "8 8 8 8 8 8 8 8 8\n"
                , out);
     }

    @Test
    public void testClear() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "111111111",
                        "222222222",
                        "333333333",
                        "444444444",
                        "555555555",
                        "666666666",
                        "777777777",
                        "888888888"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        matrix.clear();
        String out = matrix.toString();
        String empty = "";
        for (int i = 0; i < 9; i++) {
            empty += "_ _ _ _ _ _ _ _ _\n";
        }
        assertEquals(empty
                , out);
     }

    @Test
    public final void testSetAll() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "111111111",
                        "222222222",
                        "333333333",
                        "444444444",
                        "555555555",
                        "666666666",
                        "777777777",
                        "888888888"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);

        for (int i=0; i < schema.getWidth(); i++) {
            for (int j=0; j < schema.getWidth(); j++) {
                assertEquals(i, matrix.get(i, j));
            }
        }
    }

    @Test
    public final void testGetSetCount() {
        GameMatrixImpl matrix = newInstance(schema);
        assertEquals(0, matrix.getSetCount());
        matrix.set(0, 0, (byte)1);
        assertEquals(1, matrix.getSetCount());
        matrix.set(0, 0, (byte)2);
        assertEquals(1, matrix.getSetCount());
        matrix.set(0, 1, (byte)2);
        assertEquals(2, matrix.getSetCount());
    }


    @Test
    public void testClone() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "111111111",
                        "222222222",
                        "333333333",
                        "444444444",
                        "555555555",
                        "666666666",
                        "777777777",
                        "888888888"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        GameMatrixImpl clone = matrix.clone();

        String out = clone.toString();
        assertEquals("_ _ _ _ _ _ _ _ _\n"+
                "1 1 1 1 1 1 1 1 1\n"+
                "2 2 2 2 2 2 2 2 2\n"+
                "3 3 3 3 3 3 3 3 3\n"+
                "4 4 4 4 4 4 4 4 4\n"+
                "5 5 5 5 5 5 5 5 5\n"+
                "6 6 6 6 6 6 6 6 6\n"+
                "7 7 7 7 7 7 7 7 7\n"+
                "8 8 8 8 8 8 8 8 8\n"
                , out);
     }

    @Test
    public void testIsValidWithInvalid() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "111111111",
                        "222222222",
                        "333333333",
                        "444444444",
                        "555555555",
                        "666666666",
                        "777777777",
                        "888888888"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
     }

    @Test
    public void testIsValidWithBlockTopLeftCollision() {
        byte[][] data =
                QuadraticArrays.parse(
                        "100000000",
                        "000000000",
                        "001000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
    }

    @Test
    public void testIsValidWithBlockTopRightCollision() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000040",
                        "000000000",
                        "000000004",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
    }

    @Test
    public void testIsValidWithBlockBottomRightCollision() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000500",
                        "000000005"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
    }

    @Test
    public void testIsValidWithBlockBottomLeftCollision() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "080000000",
                        "010000000",
                        "800000000"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
    }

    @Test
    public void testIsValidWithBlockCenterLeftCollision() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "009000000",
                        "900000000",
                        "000000000",
                        "000000000",
                        "000000000"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
    }

    @Test
    public void testIsValidWithBlockCenterRightCollision() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "009000003",
                        "000000030",
                        "000000000",
                        "000000000",
                        "000000000"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
    }

    @Test
    public void testIsValidWithBlockCenterCenterCollision() {
        byte[][] data =
                QuadraticArrays.parse(
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000100000",
                        "000001000",
                        "000000000",
                        "000000000",
                        "000000000"
                );

        GameMatrixImpl matrix = newInstance(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
    }
}
