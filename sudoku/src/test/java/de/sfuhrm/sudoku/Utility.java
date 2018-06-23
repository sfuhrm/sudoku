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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class.
 * @author Stephan Fuhrmann
 */
public class Utility {

    private Utility() {
        // no instance allowed
    }

    public static List<Integer> toIntList(byte[] array) {
        int[] iv = toIntArray(array);
        return IntStream.of(iv).boxed().sorted().collect(Collectors.toList());
    }

    public static int[] toIntArray(byte[] array) {
        int[] iv = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            iv[i] = array[i];
        }
        return iv;
    }
}
