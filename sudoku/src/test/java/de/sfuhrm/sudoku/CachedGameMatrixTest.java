/*
Sudoku - a fast Java Sudoku game creation library.
Copyright (C) 2017  Stephan Fuhrmann

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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link CachedGameMatrix}.
 * @author Stephan Fuhrmann
 */
public class CachedGameMatrixTest {

    @Test
    public void testNew() {
        new CachedGameMatrix();
    }
    
    @Test
    public void testGet() {
        GameMatrix matrix = new CachedGameMatrix();
        byte value = matrix.get(0, 0);
        assertEquals(GameMatrix.UNSET, value);
        value = matrix.get(8, 8);
        assertEquals(GameMatrix.UNSET, value);
    }
    
    @Test
    public void testSet() {
        GameMatrix matrix = new CachedGameMatrix();
        byte value = matrix.get(0, 0);
        assertEquals(GameMatrix.UNSET, value);
        matrix.set(0,0,(byte)4);
        value = matrix.get(0, 0);
        assertEquals(4, value);
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
        
        GameMatrix matrix = new CachedGameMatrix();
        matrix.setAll(data);
        
        for (int i=0; i < GameMatrix.SIZE; i++) {
           for (int j=0; j < GameMatrix.SIZE; j++) {
                assertEquals(i, matrix.get(i, j));
           }
        }
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
        matrix.setAll(data);
        CachedGameMatrix clone = (CachedGameMatrix) matrix.clone();
        
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
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
    
    @Test
    public void testCanSet() {
        byte data[][] =
        GameMatrix.parse(
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
        
        CachedGameMatrix matrix = new CachedGameMatrix();
        matrix.setAll(data);

        // the "x" cell marked above
        assertEquals(true,  matrix.canSet(0, 0, (byte)0)); // always works
        assertEquals(false, matrix.canSet(0, 0, (byte)2)); // in block
        assertEquals(true,  matrix.canSet(0, 0, (byte)3)); // not in block
        
        // the "y" cell marked above
        assertEquals(true,  matrix.canSet(4, 8, (byte)0)); // always works
        assertEquals(true,  matrix.canSet(4, 8, (byte)2)); // in block
        assertEquals(true,  matrix.canSet(4, 8, (byte)3)); // not in block
     }
    
    @Test
    public void testGetSetCount() {
        CachedGameMatrix matrix = new CachedGameMatrix();
        
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                matrix.set(row, column, GameMatrix.UNSET);
                assertEquals(0,  matrix.getSetCount());
            }
        }
        
        int set = 0;
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                matrix.set(row, column, (byte)1);
                set++;
                assertEquals(set,  matrix.getSetCount());
            }
        }
        
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                matrix.set(row, column, GameMatrix.UNSET);
                set--;
                assertEquals(set,  matrix.getSetCount());
            }
        }
     }
}
