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

/**
 * Helper methods for working on two-dimensional arrays.
 * @author Stephan Fuhrmann
 */
public final class QuadraticArrays {

    /** No instance allowed. */
    private QuadraticArrays() {
    }


    /** Clones the given two-dimensional int array.
     * @param array the array to clone.
     * @return a clone of the input array with same dimensions and content.
     */
    static int[][] cloneArray(final int[][] array) {
        int[][] result = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = new int[array[i].length];
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    /** Clones the given two-dimensional byte array.
     * @param array the array to clone.
     * @return a clone of the input array with same dimensions and content.
     */
    static byte[][] cloneArray(final byte[][] array) {
        byte[][] result = new byte[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = new byte[array[i].length];
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    /** Clones the given two-dimensional boolean array.
     * @param array the array to clone.
     * @return a clone of the input array with same dimensions and content.
     */
    static boolean[][] cloneArray(final boolean[][] array) {
        boolean[][] result = new boolean[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = new boolean[array[i].length];
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    /**
     * Parses a string based field descriptor.
     * <br>Example usage:
     * <br>
     * <code>
     *  byte data[][] =<br>
     QuadraticArrays.parse(<br>
     "100000000",<br>
     "020100000",<br>
     "000320100",<br>
     "010000456",<br>
     "000010000",<br>
     "000000010",<br>
     "001000000",<br>
     "000001000",<br>
     "000000001"<br>
     );<br>
     GameMatrix matrix = ...<br>
     matrix.setAll(data);<br>
     * </code>
     *
     * @param gameSchema the game schema that defines the input data
     *                   dimensions.
     * @param rows array of strings with each string describing a row. Digits
     * get converted to the element values, everything else gets converted to
     * UNSET. Example for one row: "126453780".
     * @return the parsed array.
     * @throws IllegalArgumentException if one of the rows has a wrong size.
     */
    public static byte[][] parse(final GameSchema gameSchema,
                                 final String... rows) {
        if (rows.length != gameSchema.getWidth()) {
            throw new IllegalArgumentException("Array must have "
                    + gameSchema.getWidth() + " elements");
        }

        byte[][] result = new byte
                [gameSchema.getWidth()]
                [gameSchema.getWidth()];

        for (int r = 0; r < rows.length; r++) {
            String row = rows[r];

            if (row.contains(" ")) {
                String[] parts = row.split(" ");
                if (parts.length != gameSchema.getWidth()) {
                    throw new IllegalArgumentException(
                            "Row " + r
                                    + " must have "
                                    + gameSchema.getWidth() + " elements, "
                                    + "but has " + parts.length);
                }

                for (int c = 0; c < gameSchema.getWidth(); c++) {
                    String v = parts[c];
                    result[r][c] = stringToValue(gameSchema, v);
                }
            } else {
                if (rows[r].length() != gameSchema.getWidth()) {
                    throw new IllegalArgumentException(
                            "Row " + r
                                    + " must have "
                                    + gameSchema.getWidth() + " elements: "
                                    + rows[r]);
                }

                for (int c = 0; c < gameSchema.getWidth(); c++) {
                    char v = rows[r].charAt(c);
                    result[r][c] = charToValue(gameSchema, v);
                }
            }
        }
        return result;
    }

    /** Decimal system multiplier for
     * {@linkplain #stringToValue(GameSchema, String)}.
     * */
    private static final int DECIMAL = 10;
    private static byte stringToValue(final GameSchema gameSchema,
                                      final String value) {
        byte result = 0;
        for (char c : value.toCharArray()) {
            result *= DECIMAL;
            if (c >= '1' && c <= '9') {
                result = (byte) (c
                        - '1'
                        + gameSchema.getMinimumValue());
            }
        }

        if (result == 0) {
            result = gameSchema.getUnsetValue();
        }
        return result;
    }

    private static byte charToValue(final GameSchema gameSchema,
                                    final char value) {
        byte result;
        if (value >= '1' && value <= '9') {
            result = (byte) (value
                    - '1'
                    + gameSchema.getMinimumValue());
        } else {
            result = gameSchema.getUnsetValue();
        }
        return result;
    }

    /**
     * Parses a string based field descriptor.
     * <br>Example usage:
     * <br>
     * <code>
     *  byte data[][] =<br>
        QuadraticArrays.parse(<br>
                "100000000",<br>
                "020100000",<br>
                "000320100",<br>
                "010000456",<br>
                "000010000",<br>
                "000000010",<br>
                "001000000",<br>
                "000001000",<br>
                "000000001"<br>
                );<br>
        GameMatrix matrix = ...<br>
        matrix.setAll(data);<br>
     * </code>
     *
     *
     * @param rows array of strings with each string describing a row. Digits
     * get converted to the element values, everything else gets converted to
     * UNSET. Example for one row: "126453780".
     * @return the parsed array.
     * @throws IllegalArgumentException if one of the rows has a wrong size.
     */
    public static byte[][] parse(final String... rows) {

        for (GameSchema schema : GameSchemas.getSupportedGameSchemas()) {
            if (rows.length == schema.getWidth()) {
                return parse(schema, rows);
            }
        }

        throw new IllegalArgumentException(
                "Input arrays has unknown dimension");
    }

    /** Format a game matrix to a String.
     * @param gameMatrix the input game matrix whose values to use
     * to format the String with.
     * @return a String with $n lines and 2*$n chars per line.
     * '1'-'9' denotes the digit(s) filled in the field. '_'
     * denotes a free field. Fields are separated by a
     * space.
     */
    static String toString(final GameMatrix gameMatrix) {
        StringBuilder sb = new StringBuilder();

        int maxWidth = Integer.toString(
                gameMatrix.getSchema()
                        .getMaximumValue())
                .length();
        char fillChar = '_';
        char space = ' ';
        StringBuilder fill = new StringBuilder();
        for (int i = 0; i < maxWidth; i++) {
            fill.append(fillChar);
        }

        for (int row = 0; row < gameMatrix.getSchema().getWidth(); row++) {
            for (int column = 0;
                 column < gameMatrix.getSchema().getWidth(); column++) {
                if (column != 0) {
                    sb.append(space);
                }

                byte v = gameMatrix.get(row, column);
                if (v != gameMatrix.getSchema().getUnsetValue()) {
                    String value = Integer.toString(
                            v - gameMatrix.getSchema().getMinimumValue() + 1);
                    for (int f = value.length(); f < maxWidth; f++) {
                        sb.append(fillChar);
                    }
                    sb.append(value);
                } else {
                    sb.append(fill);
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
