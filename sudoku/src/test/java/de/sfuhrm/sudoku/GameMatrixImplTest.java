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
public class GameMatrixImplTest {

    private final GameSchema schema = GameSchemas.SCHEMA_9X9;

    @Test
    public void testNew() {
        GameMatrixImpl m = new GameMatrixImpl(schema);
        assertEquals(0, m.getSetCount());
    }

    @Test
    public void testGet() {
        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        GameSchema schema = matrix.getSchema();
        byte value = matrix.get(0, 0);
        assertEquals(schema.getUnsetValue(), value);
        value = matrix.get(8, 8);
        assertEquals(schema.getUnsetValue(), value);
    }

    @Test
    public void testGetSetCount() {
        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        assertEquals(0, matrix.getSetCount());
        matrix.set(0, 0, (byte)1);
        assertEquals(1, matrix.getSetCount());
        matrix.set(0, 0, (byte)2);
        assertEquals(1, matrix.getSetCount());
        matrix.set(0, 1, (byte)2);
        assertEquals(2, matrix.getSetCount());
    }

    @Test
    public void testGetArray() {
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
        GameMatrixImpl first = new GameMatrixImpl(schema);
        first.setAll(data);

        byte[][] a = first.getArray();
        byte[][] b = first.getArray();

        assertNotSame(a, b);
        assertTrue(Arrays.deepEquals(data, a));
        assertTrue(Arrays.deepEquals(data, b));
    }

    @Test
    public void testSet() {
        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        GameSchema schema = matrix.getSchema();
        byte value = matrix.get(0, 0);
        assertEquals(schema.getUnsetValue(), value);
        matrix.set(0,0,(byte)4);
        value = matrix.get(0, 0);
        assertEquals(4, value);
    }

    @Test
    public void testParse() {
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

        assertEquals(9, data[0].length);
        assertEquals(9, data[1].length);
        assertEquals(9, data[2].length);
        assertEquals(9, data[3].length);
        assertEquals(9, data[4].length);
        assertEquals(9, data[5].length);
        assertEquals(9, data[6].length);
        assertEquals(9, data[7].length);
        assertEquals(9, data[8].length);

        assertEquals(0, data[0][0]);
        assertEquals(0, data[0][1]);
        assertEquals(1, data[1][0]);
        assertEquals(1, data[1][1]);
        assertEquals(2, data[2][0]);
        assertEquals(2, data[2][1]);
        assertEquals(3, data[3][0]);
        assertEquals(3, data[3][1]);
        assertEquals(4, data[4][0]);
        assertEquals(4, data[4][1]);
        assertEquals(5, data[5][0]);
        assertEquals(5, data[5][1]);
        assertEquals(6, data[6][0]);
        assertEquals(6, data[6][1]);
        assertEquals(7, data[7][0]);
        assertEquals(7, data[7][1]);
        assertEquals(8, data[8][0]);
        assertEquals(8, data[8][1]);
    }

    @Test
    public void testParseWithWrongOuterLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            byte[][] data =
                    QuadraticArrays.parse(
                            "000000000"
                    );
        });
    }

    @Test
    public void testParseWithWrongInnerLength() {
        assertThrows(IllegalArgumentException.class, () -> {
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
                            "00000000"
                    );
        });
    }

    @Test
    public void testEqualsWithSame() {
        GameMatrixImpl instance = new GameMatrixImpl(schema);
        assertEquals(instance, instance);
    }

    @Test
    public void testEqualsWithNull() {
        GameMatrixImpl instance = new GameMatrixImpl(schema);
        assertNotEquals(null, instance);
    }

    @Test
    public void testEqualsWithOtherClass() {
        GameMatrixImpl instance = new GameMatrixImpl(schema);
        assertNotEquals("foobar", instance);
    }

    @Test
    public void testEqualsWithEqualMatrix() {
        byte[][] matrix =
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
        GameMatrixImpl instance1 = new GameMatrixImpl(schema);
        instance1.setAll(matrix);

        GameMatrixImpl instance2 = new GameMatrixImpl(schema);
        instance1.setAll(matrix);

        assertEquals(instance1, instance2);
    }

    @Test
    public void testEqualsWithUnequalMatrix() {
        byte[][] matrix1 =
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
        GameMatrixImpl instance1 = new GameMatrixImpl(schema);
        instance1.setAll(matrix1);

        byte[][] matrix2 =
        QuadraticArrays.parse(
                "123000000",
                "000000000",
                "000000000",
                "000000000",
                "000000000",
                "000000000",
                "000000000",
                "000000000",
                "000000000"
                );
        GameMatrixImpl instance2 = new GameMatrixImpl(schema);
        instance2.setAll(matrix2);

        assertNotEquals(instance1, instance2);
    }

    @Test
    public void testHashCode() {
        GameMatrixImpl instance = new GameMatrixImpl(schema);
        instance.hashCode();
    }

    @Test
    public void testParseWithWrongChars() {
        byte[][] expected =
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

        byte[][] actual =
                QuadraticArrays.parse(
                        "?00000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000"
                );

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testSetAll() {
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        matrix.setAll(data);

        for (int i=0; i < schema.getWidth(); i++) {
           for (int j=0; j < schema.getWidth(); j++) {
                assertEquals(i, matrix.get(i, j));
           }
        }
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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
    public void testFindDuplicateBits() {
        int mask;
        byte[] array;

        array = new byte[] {1,2,3,4,5};
        mask = GameMatrixImpl.findDuplicateBits(schema, array);
        assertEquals(0, mask);

        array = new byte[] {1,1,3,4,5};
        mask = GameMatrixImpl.findDuplicateBits(schema, array);
        assertEquals(2, mask);

        array = new byte[] {1,1,1,4,5};
        mask = GameMatrixImpl.findDuplicateBits(schema, array);
        assertEquals(2, mask);

        array = new byte[] {0,0,0,4,5};
        mask = GameMatrixImpl.findDuplicateBits(schema, array);
        assertEquals(0, mask);
    }

    @Test
    public void testGetNumberMask() {
        int mask;
        byte[] array;

        array = new byte[] {1,2,3,4,5};
        mask = GameMatrixImpl.getNumberMask(schema, array);
        assertEquals(2+4+8+16+32, mask);

        array = new byte[] {1,1,3,4,5};
        mask = GameMatrixImpl.getNumberMask(schema, array);
        assertEquals(2+8+16+32, mask);

        array = new byte[] {1,1,1,4,5};
        mask = GameMatrixImpl.getNumberMask(schema, array);
        assertEquals(2+16+32, mask);

        array = new byte[] {0,0,0,4,5};
        mask = GameMatrixImpl.getNumberMask(schema, array);
        assertEquals(16+32, mask);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        matrix.setAll(data);
        assertTrue(matrix.isValid());
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        matrix.setAll(data);
        assertFalse(matrix.isValid());
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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
        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        matrix.setAll(data);
        int mask = matrix.getBlockFreeMask(0,0);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
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
    public void testRoundToBlock() {
        GameMatrixImpl nineMatrix = new GameMatrixImpl(schema);
        assertEquals(0, nineMatrix.roundToBlock( 0));
        assertEquals(0, nineMatrix.roundToBlock( 1));
        assertEquals(0, nineMatrix.roundToBlock( 2));
        assertEquals(3, nineMatrix.roundToBlock( 3));
        assertEquals(3, nineMatrix.roundToBlock( 4));
        assertEquals(3, nineMatrix.roundToBlock( 5));
        assertEquals(6, nineMatrix.roundToBlock( 6));
        assertEquals(6, nineMatrix.roundToBlock( 7));
        assertEquals(6, nineMatrix.roundToBlock( 8));
    }

    @Test
    public void testFindLeastFreeCellWithAllFull() {
        // full matrix
        byte[][] data =
                QuadraticArrays.parse(
                        "367915482",
                        "149268357",
                        "582473619",
                        "436187925",
                        "975624831",
                        "218359764",
                        "624731598",
                        "753892146",
                        "891546273"
                );

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        matrix.setAll(data);

        CellIndex min = new CellIndex();
        GameMatrixImpl.FreeCellResult result = matrix.findLeastFreeCell(min);
        assertEquals(GameMatrixImpl.FreeCellResult.NONE_FREE, result);
     }

    @Test
    public void testFindLeastFreeCellWithAlmostFull() {
        // 0,0 free matrix
        byte[][] data =
                QuadraticArrays.parse(
                        "067915482",
                        "149268357",
                        "582473619",
                        "436187925",
                        "975624831",
                        "218359764",
                        "624731598",
                        "753892146",
                        "891546273"
                );

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        matrix.setAll(data);

        CellIndex min = new CellIndex();
        GameMatrixImpl.FreeCellResult result = matrix.findLeastFreeCell(min);
        assertEquals(GameMatrixImpl.FreeCellResult.FOUND, result);
        assertEquals(0, min.column);
        assertEquals(0, min.row);
     }

    @Test
    public void testFindLeastFreeCell() {
        // a possible "full house" in the upper left block
        byte[][] data =
                QuadraticArrays.parse(
                        "123000000",
                        "456000000",
                        "780000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000",
                        "000000000"
                );

        GameMatrixImpl matrix = new GameMatrixImpl(schema);
        matrix.setAll(data);

        CellIndex min = new CellIndex();
        GameMatrixImpl.FreeCellResult result = matrix.findLeastFreeCell(min);
        assertEquals(GameMatrixImpl.FreeCellResult.FOUND, result);
        assertEquals(2, min.row);
        assertEquals(2, min.column);
     }
}
