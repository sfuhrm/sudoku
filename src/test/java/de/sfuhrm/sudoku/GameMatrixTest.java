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
import org.junit.Ignore;

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
}
