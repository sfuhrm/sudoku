/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sfuhrm.sudoku;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author fury
 */
public class CreatorTest {
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
    }
    
    
    @Test
    public void testCreateFull() {
        Riddle r = Creator.createFull();
        assertEquals(9*9, r.getSetCount());
    }
}
