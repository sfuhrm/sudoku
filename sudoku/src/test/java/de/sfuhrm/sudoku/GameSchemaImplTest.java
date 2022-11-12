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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link GameSchemaImpl}.
 * @author Stephan Fuhrmann
 */
public class GameSchemaImplTest {

    private static Stream<Arguments> allGameSchemas() {
        return GameSchemas.getSupportedGameSchemas().stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidBitMaskWithZero(GameSchema schema) {
        assertTrue(schema.validBitMask(0));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidBitMaskWithSingleBitSet(GameSchema schema) {
        for (int i = schema.getMinimumValue(); i <= schema.getMaximumValue(); i++) {
            assertTrue(schema.validBitMask(1 << i));
        }
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidBitMaskWithAllBitsSet(GameSchema schema) {
        int value = 0;
        for (int i = schema.getMinimumValue(); i <= schema.getMaximumValue(); i++) {
            value |= 1 << i;
        }
        assertTrue(schema.validBitMask(value));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidBitMaskWithInvalidZeroBit(GameSchema schema) {
        assertFalse(schema.validBitMask(1 << 0));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidBitMaskWithInvalidWidthPBit(GameSchema schema) {
        assertFalse(schema.validBitMask(1 << schema.getMaximumValue() + 1));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidCoordsWithBothTooBig(GameSchema schema) {
        assertFalse(schema.validCoords(schema.getWidth(), schema.getWidth()));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidCoordsWithRowTooBig(GameSchema schema) {
        assertFalse(schema.validCoords(schema.getWidth(), 0));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidCoordsWithColumnTooBig(GameSchema schema) {
        assertFalse(schema.validCoords(0, schema.getWidth()));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidCoordsWithColumnTooSmall(GameSchema schema) {
        assertFalse(schema.validCoords(0, -1));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidCoordsWithRowTooSmall(GameSchema schema) {
        assertFalse(schema.validCoords(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidCoordsWithBothZero(GameSchema schema) {
        assertTrue(schema.validCoords(0, 0));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidCoordsWithBothMax(GameSchema schema) {
        assertTrue(schema.validCoords(schema.getWidth() - 1, schema.getWidth() - 1));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidValueWithMinimum(GameSchema schema) {
        assertTrue(schema.validValue(schema.getMinimumValue()));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidValueWithMaximum(GameSchema schema) {
        assertTrue(schema.validValue(schema.getMaximumValue()));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidValueWithTooSmall(GameSchema schema) {
        assertFalse(schema.validValue((byte)(schema.getUnsetValue() - 1)));
    }

    @ParameterizedTest
    @MethodSource("allGameSchemas")
    public void testValidValueWithTooBig(GameSchema schema) {
        assertFalse(schema.validValue((byte)(schema.getMaximumValue() + 1)));
    }
}
