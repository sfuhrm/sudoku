/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sfuhrm.sudoku;

import static de.sfuhrm.sudoku.GameMatrix.SIZE;
import static de.sfuhrm.sudoku.GameMatrix.UNSET;
import static de.sfuhrm.sudoku.GameMatrix.validValue;

/**
 * Helper methods for cloning two-dimensional arrays of
 * a 9x9 dimension.
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
        int[][] result = new int[SIZE][SIZE];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    /** Clones the given two-dimensional byte array.
     * @param array the array to clone.
     * @return a clone of the input array with same dimensions and content.
     */
    static byte[][] cloneArray(final byte[][] array) {
        byte[][] result = new byte[SIZE][SIZE];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    /** Clones the given two-dimensional boolean array.
     * @param array the array to clone.
     * @return a clone of the input array with same dimensions and content.
     */
    static boolean[][] cloneArray(final boolean[][] array) {
        boolean[][] result = new boolean[SIZE][SIZE];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(array[i], 0, result[i], 0, array[i].length);
        }
        return result;
    }

    /**
     * Parses a string based field descriptor.
     *
     * @param rows array of strings with each string describing a row. Digits
     * get converted to the element values, everything else gets converted to
     * UNSET.
     * @return the parsed array.
     * @throws IllegalArgumentException if one of the rows has a wrong size.
     */
    public static byte[][] parse(final String... rows) {
        if (rows.length != SIZE) {
            throw new IllegalArgumentException("Array must have "
                    + SIZE + " elements");
        }

        byte[][] result = new byte[SIZE][SIZE];

        for (int r = 0; r < rows.length; r++) {
            if (rows[r].length() != SIZE) {
                throw new IllegalArgumentException(
                        "Row " + r
                                + " must have "
                                + SIZE + " elements: "
                                + rows[r]);
            }

            for (int c = 0; c < SIZE; c++) {
                char v = rows[r].charAt(c);

                if (v >= '0' && v <= '9') {
                    result[r][c] = (byte) (v - '0');
                } else {
                    result[r][c] = UNSET;
                }
            }
        }
        return result;
    }

    /** Format a game matrix to a String.
     * @param gameMatrix the input game matrix whose values to use
     * to format the String with.
     * @return a String with 9 lines and 9 chars per line.
     * '1'-'9' denotes the digit filled in the field. '_'
     * denotes a free field.
     */
    static String toString(final GameMatrix gameMatrix) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                byte v = gameMatrix.get(i, j);
                assert validValue(v);
                if (v != UNSET) {
                    sb.append(Integer.toString(v));
                } else {
                    sb.append('_');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
