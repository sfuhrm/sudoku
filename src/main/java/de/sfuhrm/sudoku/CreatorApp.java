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
