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
import java.util.Random;
import java.util.function.Function;

/**
 * Creates a fully filled sudoku.
 * @author Stephan Fuhrmann
 */
public class Creator {

    /**
     * The result consumer. Consumes a valid sudoku
     * and returns whether to abort (true) or continue (false).
     */
    private final Function<GameMatrix, Boolean> resultConsumer;

    /**
     * Current work in progress.
     */
    private final GameMatrix riddle;
    
    /**
     * Possibly found Sudoku.
     */
    private GameMatrix winner;

    /**
     * The random number generator.
     */
    private final Random random;

    private Creator() {
        riddle = new CachedGameMatrix();
        random = new Random();
        
        resultConsumer = t -> {
            winner = t;
            return true;
        };
    }

    /**
     * Get the index of the nth bit set.
     * @param mask the value to get the bit index from.
     * @param bitIndex the number of the set bit wanted.
     * @return the index of the relative bitIndex set bit counted from 0, or -1
     * if there are no more set bits.
     */
    protected final static int getSetBitOffset(final int mask, final int bitIndex) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if ((mask & 1 << i) != 0) {
                if (count == bitIndex) {
                    return i;
                }
                count++;
            }
        }
        return -1;
    }

    /**
     * Creates a valid fully setup sudoku.
     * @return a fully filled sudoku board.
     */
    public static GameMatrix createFull() {
        Creator c = new Creator();
        while (true) {
            // the number to distribute
            
            c.riddle.clear();
            c.fillBlock(0, 0);
            c.fillBlock(3, 3);
            c.fillBlock(6, 6);
                   
            boolean ok = c.backtrack(9*9 - c.riddle.getSetCount(), new int[2]);
            if (ok)
                break;
        }
        
        return c.winner;
    }
    
    /** Creates a variant of a fully-filled game matrix. The
     * variant is calculated very efficiently by applying simple
     * transformations.
     * @param matrix the input matrix to transform.
     * @return a transformed variant of the input game matrix.
     * @throws IllegalArgumentException if there are unset fields in the GameMatrix.
     */
    public static GameMatrix createVariant(GameMatrix matrix) {
        GameMatrix target = new GameMatrix();
        Random random = new Random();
        
        byte substitution[] = createNumbersToDistribute(random, 1);
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                byte original = matrix.get(row, column);
                if (original == GameMatrix.UNSET)
                    throw new IllegalArgumentException("There are unset fields in the given GameMatrix");
                byte substitute = substitution[original-1];
                target.set(row, column, substitute);
            }
        }
        
        // row swapping within blocks
        for (int i=0; i < 3; i++) {
            boolean swap = random.nextBoolean();
            if (swap) {
                // swapped row distance: 1 or 2
                int distance = 1 + random.nextInt(2);
                int offset   = distance == 2 ? 0 : random.nextInt(2);
                swapRow(matrix, i*3 + offset, i*3 + offset + distance);
            }
        }
        
        // column swapping within blocks
        for (int i=0; i < 3; i++) {
            boolean swap = random.nextBoolean();
            if (swap) {
                // swapped column distance: 1 or 2
                int distance = 1 + random.nextInt(2);
                int offset   = distance == 2 ? 0 : random.nextInt(2);
                swapColumn(matrix, i*3 + offset, i*3 + offset + distance);
            }
        }
        
        return target;
    }

    /** Swaps two rows in the given matrix. */
    protected static void swapRow(GameMatrix matrix, int rowA, int rowB) {
        for (int column = 0; column < GameMatrix.SIZE; column++) {
            byte av = matrix.get(rowA, column);
            byte bv = matrix.get(rowB, column);
            matrix.set(rowB, column, av);
            matrix.set(rowA, column, bv);
        }
    }
    
    /** Swaps two columns in the given matrix. */
    protected static void swapColumn(GameMatrix matrix, int columnA, int columnB) {
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            byte av = matrix.get(row, columnA);
            byte bv = matrix.get(row, columnB);
            matrix.set(row, columnB, av);
            matrix.set(row, columnA, bv);
        }
    }

    /* Create a random array with numbers to distribute. */
    protected static byte[] createNumbersToDistribute(Random r, int multiplicity) {
        List<Integer> numbersToDistribute= new ArrayList<>(9*multiplicity);
        for (int number = 1; number <= 9; number++) {
            for (int j=0; j < multiplicity; j++) {
                numbersToDistribute.add(number);
            }
        }
        Collections.shuffle(numbersToDistribute, r);
        byte[] numbersToDistributeArray = new byte[numbersToDistribute.size()];
        int k = 0;
        for (Integer number : numbersToDistribute) {
            numbersToDistributeArray[k++] = number.byteValue();
        }
        return numbersToDistributeArray;
    }

    /** Checks whether on the given riddle the given cell can
     * be cleared. A cell can only be cleared if the result remains
     * uniqely solvable.
     * @param riddle riddle to check clearability in.
     * @param column the column in the riddle.
     * @param row the row in the riddle.
     */
    private static boolean canClear(Riddle riddle, int column, int row) {
        if (riddle.get(row, column) == Riddle.UNSET) {
            return false;
        }

        // if there's only one free val, it's unique
        int freeMask = riddle.getFreeMask(row, column);
        int freeVals = Integer.bitCount(freeMask);

        if (freeVals == 0) {
            return true;
        }

        int old = riddle.get(row, column);
        riddle.set(row, column, Riddle.UNSET);

        Solver s = new Solver(riddle);
        List<Riddle> results = s.solve();
        boolean result = (results.size() == 1);

        // rollback
        riddle.set(row, column, (byte) old);
        return result;
    }

    /**
     * Creates a riddle setup sudoku.
     *
     * @param in a fully set up (solved) and valid sudoku.
     * @return a maximally cleared sudoku.
     */
    public static Riddle createRiddle(GameMatrix in) {
        Random random = new Random();

        Riddle cur = new Riddle();
        cur.setAll(in.getArray());

        int multi = 0;
        // this could be improved:
        // first the randomized loop can run
        // second a loop over all cells can run
        while (multi < 10) {
            int i = random.nextInt(Riddle.SIZE);
            int j = random.nextInt(Riddle.SIZE);

            if (cur.get(j, i) == Riddle.UNSET) {
                continue;
            }

            if (canClear(cur, i, j)) {
                cur.set(j, i, Riddle.UNSET);
            } else {
                multi++;
            }
        }

        for (int i = 0; i < Riddle.SIZE; i++) {
            for (int j = 0; j < Riddle.SIZE; j++) {
                if (cur.get(j, i) == Riddle.UNSET) {
                    continue;
                }

                if (canClear(cur, i, j)) {
                    cur.set(j, i, Riddle.UNSET);
                }
            }
        }

        // set the preset fields non-writable
        for (int i = 0; i < Riddle.SIZE; i++) {
            for (int j = 0; j < Riddle.SIZE; j++) {
                cur.setWritable(j, i, cur.get(j, i) == Riddle.UNSET);
            }
        }

        return cur;
    }
    
    private void fillBlock(int row, int column) {
        byte[] numbers = createNumbersToDistribute(random, 1);
        int k = 0;
        for (int i = 0; i < Riddle.BLOCK_SIZE; i++) {
            for (int j = 0; j < Riddle.BLOCK_SIZE; j++) {
                riddle.set(row + j, column + i, numbers[k++]);
            }
        }
    }
    
    /** 
     * Do the backtracking job.
     * @param numbersToDistribute the count of fields left to fill.
     * @param minimumCell two-int array for use within the algorithm.
     */
    private boolean backtrack(final int numbersToDistribute, final int minimumCell[]) {
        if (numbersToDistribute == 0) {
            if (! riddle.isValid()) {
                throw new IllegalStateException();
            }
            return resultConsumer.apply(riddle);
        }
        
        // determine rows + cols that are possible candidates
        // (reduce random trying)
        boolean hasMinimum = riddle.findLeastFreeCell(minimumCell);
        if (!hasMinimum) {
            return false;
        }
        
        int minimumRow = minimumCell[0];
        int minimumColumn = minimumCell[1];
        int minimumFree = riddle.getFreeMask(minimumRow, minimumColumn);
        int minimumBits = Integer.bitCount(minimumFree);
        
        for (int bit = 0; bit < minimumBits; bit++) {
            int free = riddle.getFreeMask(minimumRow, minimumColumn);
            int number = getSetBitOffset(free, bit);
            if (number <= 0) {
                throw new IllegalStateException();
            }
            riddle.set(minimumRow, minimumColumn, (byte) (number));
            boolean ok = backtrack(numbersToDistribute - 1, minimumCell);
            if (ok) {
                return true;
            }
            riddle.set(minimumRow, minimumColumn, GameMatrix.UNSET);
        }

        return false;
    }
}
