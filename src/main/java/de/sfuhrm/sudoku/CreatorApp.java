/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sfuhrm.sudoku;

/**
 *
 * @author Stephan Fuhrmann
 */
public class CreatorApp {
    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        int n = 1;
        for (int i = 0; i < n; i++) {

            GameMatrix f = Creator.createFull();
            String s = f.toString();
            System.out.println(s);
            System.out.println();
            
            f = Creator.createRiddle(f);
            s = f.toString();
            
            System.out.println(s);
            System.out.println("Set:" + f.getSetCount());

        }

        System.out.println("Milli time: " + (System.currentTimeMillis() - start) + "ms");
    }    
}
