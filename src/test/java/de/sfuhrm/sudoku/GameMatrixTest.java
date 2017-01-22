/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sfuhrm.sudoku;

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
}
