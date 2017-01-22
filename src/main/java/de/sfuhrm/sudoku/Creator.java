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
     * and returns wheter to abort (true) or continue (false).
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
    final static int getSetBitOffset(final int mask, final int bitIndex) {
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
                   
            boolean ok = c.backtrack(9*9 - c.riddle.getSetCount());
            if (ok)
                break;
        }
        
        return c.winner;
    }

    /* Create a random array with numbers to distribute. */
    static byte[] createNumbersToDistribute(Random r, int multiplicity) {
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
     */
    private boolean backtrack(int numbersToDistribute) {
        if (numbersToDistribute == 0) {
            if (! riddle.isValid()) {
                throw new IllegalStateException();
            }
            return resultConsumer.apply(riddle);
        }
        
        // current number to distribute
        int minimumColumn = -1;
        int minimumRow = -1;
        int minimumBits = -1;
        
        // determine rows + cols that are possible candidates
        // (reduce random trying)
        for (int row=0; row < GameMatrix.SIZE; row++) {
            for (int column=0; column < GameMatrix.SIZE; column++) {
                if (riddle.get(row, column) != GameMatrix.UNSET)
                    continue;
                int free = riddle.getFreeMask(row, column);
                int bits = Integer.bitCount(free);
                
                if (bits != 0 && (minimumBits == -1 || bits < minimumBits)) {
                    minimumColumn = column;
                    minimumRow = row;
                    minimumBits = bits;
                }
            }
        }
        
        if (minimumColumn == -1 || minimumRow == -1) {
            return false;
        }
        
        for (int bit = 0; bit < minimumBits; bit++) {
            int free = riddle.getFreeMask(minimumRow, minimumColumn);
            int number = getSetBitOffset(free, bit);
            if (number <= 0) {
                throw new IllegalStateException();
            }
            riddle.set(minimumRow, minimumColumn, (byte) (number));
            boolean ok = backtrack(numbersToDistribute - 1);
            if (ok) {
                return true;
            }
            riddle.set(minimumRow, minimumColumn, GameMatrix.UNSET);
        }

        return false;
    }
}
