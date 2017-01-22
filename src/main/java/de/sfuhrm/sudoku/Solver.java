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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Solves a partially filled Sudoku. Can find multiple solutions if they are
 * there.
 * @author Stephan Fuhrmann
 */
public class Solver {

    /**
     * Current working copy.
     */
    private final Riddle riddle;
    
    /** The possible solutions for this riddle. */
    private final List<Riddle> possibleSolutions;

    /** The maximum number of solutions to search. */
    public final static int LIMIT = 20;

    public Solver(Riddle f) {
        riddle = (Riddle) f.clone();
        possibleSolutions = new ArrayList<>();
    }

    /**
     * Solves the Sudoku problem.
     * @return the found solutions. Should be only one.
     */
    public List<Riddle> solve() {
        possibleSolutions.clear();
        int freeCells = GameMatrix.SIZE * GameMatrix.SIZE - riddle.getSetCount();

        backtrack(freeCells);

        return Collections.unmodifiableList(possibleSolutions);
    }

    /**
     * Solves a Sudoku using backtracking.
     * @param freeCells number of free cells, abort criterion.
     * @return the total number of solutions.
     */
    private int backtrack(int freeCells) {

        // just one result, we have no more to choose
        if (freeCells == 0) {
            if (possibleSolutions.size() < LIMIT) {
                possibleSolutions.add((Riddle) riddle.clone());
            }

            return 1;
        }

        boolean hasMin = false;
        int result = 0;
        int minColumn = 0;
        int minRow = 0;
        int minFreeMask = 0;
        int minBits = 0;
        // find the cell with the smallest number of free bits
        // TODO this look is the hot spot. Could do something with a list
        // sorted by free bit count, but this requires nasty structures.
        for (int i = 0; i < Riddle.SIZE && (hasMin == false || minBits != 1); i++) {
            for (int j = 0; j < Riddle.SIZE && (hasMin == false || minBits != 1); j++) {
                if (riddle.get(j, i) != Riddle.UNSET) {
                    continue;
                }

                int free = riddle.getFreeMask(j, i);
                if (free == 0) {
                    // the field is empty, but there are no values we could
                    // insert.
                    // this means we have a paradoxon and must abort this
                    // backtrack branch
                    return 0;
                }

                int bits = Integer.bitCount(free);

                if ((!hasMin) || bits < minBits) {
                    minFreeMask = free;
                    minColumn = i;
                    minRow = j;
                    minBits = bits;
                    hasMin = true;
                }
            }
        }

        if (hasMin) {
            // else we are done

            // now try each number
            for (int bit = 0; bit < minBits; bit++) {
                int index = Creator.getSetBitOffset(minFreeMask, bit);
                if (index < 0) {
                    throw new IllegalStateException("minV=" + minFreeMask + ", i=" + bit + ", idx=" + index);
                }

                riddle.set(minRow, minColumn, (byte) index);
                int resultCount = backtrack(freeCells - 1);
                result += resultCount;
            }
            riddle.set(minRow, minColumn, Riddle.UNSET);
        } else {
            // freeCells != 0 and !hasMin -> dead end
            result = 0;
        }
        return result;
    }
}
