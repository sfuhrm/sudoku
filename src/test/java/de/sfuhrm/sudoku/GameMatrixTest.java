/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sfuhrm.sudoku;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Stephan Fuhrmann
 */
public class GameMatrixTest {

    @Test
    public void testNew() {
        new GameMatrix();
    }
    
    @Test
    public void testGet() {
        GameMatrix matrix = new GameMatrix();
        byte value = matrix.get(0, 0);
        assertEquals(GameMatrix.UNSET, value);
        value = matrix.get(8, 8);
        assertEquals(GameMatrix.UNSET, value);
    }
    
    @Test
    public void testSet() {
        GameMatrix matrix = new GameMatrix();
        byte value = matrix.get(0, 0);
        assertEquals(GameMatrix.UNSET, value);
        matrix.set(0,0,(byte)4);
        value = matrix.get(0, 0);
        assertEquals(4, value);
    }
    
    @Test
    public void testParse() {
        byte data[][] =
        GameMatrix.parse(
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
    public void testSetAll() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        
        for (int i=0; i < GameMatrix.SIZE; i++) {
           for (int j=0; j < GameMatrix.SIZE; j++) {
                assertEquals(i, matrix.get(i, j));
           }
        }
    }
    
    @Test
    public void testRow() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        
        byte[] target = new byte[9];
        
        for (int i=0; i < GameMatrix.SIZE; i++) {
           for (int j=0; j < GameMatrix.SIZE; j++) {
                matrix.row(i, target);
                List<Integer> vals = Utility.toIntList(target);
                assertEquals(Arrays.asList(i,i,i,i,i,i,i,i,i), vals);
           }
        }
     }
    
    @Test
    public void testColumn() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        
        byte[] target = new byte[9];
        
        for (int i=0; i < GameMatrix.SIZE; i++) {
           for (int j=0; j < GameMatrix.SIZE; j++) {
                matrix.column(i, target);
                List<Integer> vals = Utility.toIntList(target);
                assertEquals(Arrays.asList(0,1,2,3,4,5,6,7,8), vals);
           }
        }
     }
    
    @Test
    public void testBlock() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
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
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        
        String out = matrix.toString();
        assertEquals("_________\n"+
                "111111111\n"+
                "222222222\n"+
                "333333333\n"+
                "444444444\n"+
                "555555555\n"+
                "666666666\n"+
                "777777777\n"+
                "888888888\n"
                , out);
     }

    @Test
    public void testClear() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        matrix.clear();
        String out = matrix.toString();
        assertEquals("_________\n"+
                "_________\n"+
                "_________\n"+
                "_________\n"+
                "_________\n"+
                "_________\n"+
                "_________\n"+
                "_________\n"+
                "_________\n"
                , out);
     }
    
    @Test
    public void testClone() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        GameMatrix clone = (GameMatrix) matrix.clone();
        
        String out = clone.toString();
        assertEquals("_________\n"+
                "111111111\n"+
                "222222222\n"+
                "333333333\n"+
                "444444444\n"+
                "555555555\n"+
                "666666666\n"+
                "777777777\n"+
                "888888888\n"
                , out);
     }

    
    @Test
    public void testFindDuplicateBits() {
        int mask;
        byte array[];
        
        array = new byte[] {1,2,3,4,5};
        mask = GameMatrix.findDuplicateBits(array);
        assertEquals(0, mask);
        
        array = new byte[] {1,1,3,4,5};
        mask = GameMatrix.findDuplicateBits(array);
        assertEquals(2, mask);
        
        array = new byte[] {1,1,1,4,5};
        mask = GameMatrix.findDuplicateBits(array);
        assertEquals(2, mask);
        
        array = new byte[] {0,0,0,4,5};
        mask = GameMatrix.findDuplicateBits(array);
        assertEquals(0, mask);
    }
    
    @Test
    public void testGetNumberMask() {
        int mask;
        byte array[];
        
        array = new byte[] {1,2,3,4,5};
        mask = GameMatrix.getNumberMask(array);
        assertEquals(2+4+8+16+32, mask);
        
        array = new byte[] {1,1,3,4,5};
        mask = GameMatrix.getNumberMask(array);
        assertEquals(2+8+16+32, mask);
        
        array = new byte[] {1,1,1,4,5};
        mask = GameMatrix.getNumberMask(array);
        assertEquals(2+16+32, mask);
        
        array = new byte[] {0,0,0,4,5};
        mask = GameMatrix.getNumberMask(array);
        assertEquals(16+32, mask);
    }
    
    @Test
    public void testIsValidWithInvalid() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        assertEquals(false, matrix.isValid());
     }
    
    @Test
    public void testIsValidWithEmptyValid() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        assertEquals(true, matrix.isValid());
     }
    
    @Test
    public void testIsValidWithPartlyFullValid() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        assertEquals(true, matrix.isValid());
     }
    
    @Test
    public void testGetRowFreeMask() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        int mask = matrix.getRowFreeMask(0);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~(1<<1)), mask);
        mask = matrix.getRowFreeMask(1);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getRowFreeMask(2);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getRowFreeMask(3);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
     }

    @Test
    public void testGetColumnFreeMask() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        int mask = matrix.getColumnFreeMask(0);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~(1<<1)), mask);
        mask = matrix.getColumnFreeMask(1);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getColumnFreeMask(2);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1))), mask);
        mask = matrix.getColumnFreeMask(3);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<3))), mask);
     }
    
    @Test
    public void testGetBlockFreeMask() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        int mask = matrix.getBlockFreeMask(0,0);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getBlockFreeMask(0,3);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getBlockFreeMask(0,6);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1))), mask);
        mask = matrix.getBlockFreeMask(3,6);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
     }
    
    @Test
    public void testGetFreeMask() {
        byte data[][] =
        GameMatrix.parse(
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
        
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(data);
        int mask = matrix.getFreeMask(0,0);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2))), mask);
        mask = matrix.getFreeMask(0,3);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<2) | (1<<3))), mask);
        mask = matrix.getFreeMask(0,6);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<4))), mask);
        mask = matrix.getFreeMask(3,6);
        assertEquals(GameMatrix.MASK_FOR_NINE_BITS & (~((1<<1) | (1<<4) | (1<<5) | (1<<6))), mask);
     }
}
