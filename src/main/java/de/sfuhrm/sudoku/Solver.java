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
    private final Riddle field;
    
    /** The possible solutions for this riddle. */
    private final List<Riddle> possibleSolutions;

    /** The maximum number of solutions to search. */
    public final static int LIMIT = 20;

    public Solver(Riddle f) {
        field = (Riddle) f.clone();
        possibleSolutions = new ArrayList<>();
    }

    /**
     * Solves the Sudoku problem.
     * @return the found solutions. Should be only one.
     */
    public List<Riddle> solve() {
        possibleSolutions.clear();
        int freeCells = GameMatrix.SIZE * GameMatrix.SIZE - field.getSetCount();

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
                possibleSolutions.add((Riddle) field.clone());
            }

            return 1;
        }

        boolean hasMin = false;
        int result = 0;
        int minI = 0;
        int minJ = 0;
        int minV = 0;
        int minBits = 0;
        // find the cell with the smallest number of free bits
        // TODO this look is the hot spot. Could do something with a list
        // sorted by free bit count, but this requires nasty structures.
        for (int i = 0; i < Riddle.SIZE && (hasMin == false || minBits != 1); i++) {
            for (int j = 0; j < Riddle.SIZE && (hasMin == false || minBits != 1); j++) {
                if (field.get(j, i) != Riddle.UNSET) {
                    continue;
                }

                int free = field.getFreeMask(j, i);
                if (free == 0) {
                    // the field is empty, but there are no values we could
                    // insert.
                    // this means we have a paradoxon and must abort this
                    // backtrack branch
                    return 0;
                }

                int bits = Integer.bitCount(free);

                if ((!hasMin) || bits < minBits) {
                    minV = free;
                    minI = i;
                    minJ = j;
                    minBits = bits;
                    hasMin = true;
                }
            }
        }

        if (hasMin) {
            // else we are done

            // now try each number
            for (int bit = 0; bit < minBits; bit++) {
                int idx = Creator.getSetBitOffset(minV, bit);
                if (idx < 0) {
                    throw new IllegalStateException("minV=" + minV + ", i=" + bit + ", idx=" + idx);
                }

                field.set(minJ, minI, (byte) idx);
                int resultCount = backtrack(freeCells - 1);
                result += resultCount;
            }
            field.set(minJ, minI, Riddle.UNSET);
        } else {
            // freeCells != 0 and !hasMin -> dead end
            result = 0;
        }
        return result;
    }
}
