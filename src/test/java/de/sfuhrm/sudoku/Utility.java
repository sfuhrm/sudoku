/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        return IntStream.of(iv).mapToObj((int b) -> b).sorted().collect(Collectors.toList());
    }

    public static int[] toIntArray(byte[] array) {
        int[] iv = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            iv[i] = array[i];
        }
        return iv;
    }
}
