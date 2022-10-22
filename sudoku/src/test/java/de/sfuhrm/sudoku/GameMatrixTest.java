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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link GameMatrix}.
 * @author Stephan Fuhrmann
 */
public class GameMatrixTest {

    @Test
    public void testValidBitMaskWithZero() {
        assertTrue(GameMatrix.validBitMask(0));
    }

    @Test
    public void testValidBitMaskWithSingleBitSet() {
        for (int i = GameMatrix.MINIMUM_VALUE; i <= GameMatrix.MAXIMUM_VALUE; i++) {
            assertTrue(GameMatrix.validBitMask(1 << i));
        }
    }

    @Test
    public void testValidBitMaskWithAllBitsSet() {
        int value = 0;
        for (int i = GameMatrix.MINIMUM_VALUE; i <= GameMatrix.MAXIMUM_VALUE; i++) {
            value |= 1 << i;
        }
        assertTrue(GameMatrix.validBitMask(value));
    }

    @Test
    public void testValidBitMaskWithInvalidZeroBit() {
        assertFalse(GameMatrix.validBitMask(1 << 0));
    }

    @Test
    public void testValidBitMaskWithInvalidTenBit() {
        assertFalse(GameMatrix.validBitMask(1 << 10));
    }

    @Test
    public void testValidCoordsWithBothTooBig() {
        assertFalse(GameMatrix.validCoords(10, 10));
    }

    @Test
    public void testValidCoordsWithRowTooBig() {
        assertFalse(GameMatrix.validCoords(10, 0));
    }

    @Test
    public void testValidCoordsWithColumnTooBig() {
        assertFalse(GameMatrix.validCoords(0, 10));
    }

    @Test
    public void testValidCoordsWithColumnTooSmall() {
        assertFalse(GameMatrix.validCoords(0, -1));
    }

    @Test
    public void testValidCoordsWithRowTooSmall() {
        assertFalse(GameMatrix.validCoords(-1, 1));
    }

    @Test
    public void testValidCoordsWithBothZero() {
        assertTrue(GameMatrix.validCoords(0, 0));
    }

    @Test
    public void testValidCoordsWithBothMax() {
        assertTrue(GameMatrix.validCoords(GameMatrix.SIZE - 1, GameMatrix.SIZE - 1));
    }

    @Test
    public void testValidValueWithMinimum() {
        assertTrue(GameMatrix.validValue(GameMatrix.MINIMUM_VALUE));
    }

    @Test
    public void testValidValueWithMaximum() {
        assertTrue(GameMatrix.validValue(GameMatrix.MAXIMUM_VALUE));
    }

    @Test
    public void testValidValueWithTooSmall() {
        assertFalse(GameMatrix.validValue((byte)(GameMatrix.UNSET - 1)));
    }

    @Test
    public void testValidValueWithTooBig() {
        assertFalse(GameMatrix.validValue((byte)(GameMatrix.MAXIMUM_VALUE + 1)));
    }
}
