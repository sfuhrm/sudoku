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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Solves a partially filled Sudoku. Can find multiple solutions if they are
 * there.
 * @author Stephan Fuhrmann
 */
public final class Solver {

    /**
     * Current working copy.
     */
    private final Riddle riddle;

    /** The possible solutions for this riddle. */
    private final List<Riddle> possibleSolutions;

    /** The maximum number of solutions to search. */
    public static final int LIMIT = 20;

    /** Creates a solver for the given riddle.
     * @param solveMe the riddle to solve.
     */
    public Solver(final Riddle solveMe) {
        riddle = (Riddle) solveMe.clone();
        possibleSolutions = new ArrayList<>();
    }

    /**
     * Solves the Sudoku problem.
     * @return the found solutions. Should be only one.
     */
    public List<Riddle> solve() {
        possibleSolutions.clear();
        int freeCells = GameMatrix.SIZE * GameMatrix.SIZE
                - riddle.getSetCount();

        backtrack(freeCells, new int[2]);

        return Collections.unmodifiableList(possibleSolutions);
    }

    /**
     * Solves a Sudoku using backtracking.
     * @param freeCells number of free cells, abort criterion.
     * @param minimumCell two-int array for use within the algorithm.
     * @return the total number of solutions.
     */
    private int backtrack(final int freeCells, final int[] minimumCell) {

        // just one result, we have no more to choose
        if (freeCells == 0) {
            if (possibleSolutions.size() < LIMIT) {
                possibleSolutions.add((Riddle) riddle.clone());
            }

            return 1;
        }

        boolean hasMin = riddle.findLeastFreeCell(minimumCell);
        if (!hasMin) {
            // no solution
            return 0;
        }

        int result = 0;
        int minimumRow = minimumCell[0];
        int minimumColumn = minimumCell[1];
        int minimumFree = riddle.getFreeMask(minimumRow, minimumColumn);
        int minimumBits = Integer.bitCount(minimumFree);

        // else we are done

        // now try each number
        for (int bit = 0; bit < minimumBits; bit++) {
            int index = Creator.getSetBitOffset(minimumFree, bit);
            if (index <= 0) {
                throw new IllegalStateException(
                        "minV=" + minimumFree
                      + ", i=" + bit
                      + ", idx=" + index);
            }

            riddle.set(minimumRow, minimumColumn, (byte) index);
            int resultCount = backtrack(freeCells - 1, minimumCell);
            result += resultCount;
        }
        riddle.set(minimumRow, minimumColumn, Riddle.UNSET);

        return result;
    }
}
