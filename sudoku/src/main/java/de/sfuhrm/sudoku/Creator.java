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
import java.util.Random;
import java.util.function.Function;

/**
 * Creates a fully filled sudoku.
 * @author Stephan Fuhrmann
 */
public final class Creator {

    /** Number of random cleared fields before systematic clearing. */
    private static final int CREATE_RIDDLE_RANDOM_CLEAR = 10;

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

    /** Private constructor. Use the static methods instead.
     */
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
    protected static int getSetBitOffset(final int mask,
            final int bitIndex) {
        int count = 0; // index of the next bit seen being set
        int workingMask = mask; // the left unseen bits are set
        for (int i = 0; workingMask != 0; i++) {
            if ((workingMask & 1) != 0) {
                if (count == bitIndex) {
                    return i;
                }
                count++;
            }
            workingMask >>>= 1;
        }
        return -1;
    }

    /**
     * Creates a valid fully setup sudoku.
     * @return a fully filled sudoku board.
     * No fields are {@link GameMatrix#UNSET unset}.
     */
    public static GameMatrix createFull() {
        Creator c = new Creator();
        while (true) {
            // the number to distribute

            c.riddle.clear();

            // * 0 0
            // 0 * 0
            // 0 0 *
            //
            // The blocks on the diagonal can be filled independently in random
            // because they can not collide.
            // This way we fill 1/3 of the matrix 'for free'.
            for (int i = 0; i < GameMatrix.BLOCK_COUNT; i++) {
                c.fillBlock(
                        i * GameMatrix.BLOCK_SIZE,
                        i * GameMatrix.BLOCK_SIZE);

            }

            int fields = GameMatrix.SIZE * GameMatrix.SIZE;
            boolean ok = c.backtrack(fields - c.riddle.getSetCount(),
                    new int[2]);
            if (ok) {
                break;
            }
        }

        return c.winner;
    }

    /** Creates a variant of a fully-filled game matrix. The
     * variant is calculated very efficiently by applying simple
     * transformations.
     * @param matrix the input matrix to transform.
     * @return a transformed variant of the input game matrix.
     * @throws IllegalArgumentException if there are unset fields in the
     * GameMatrix.
     */
    public static GameMatrix createVariant(final GameMatrix matrix) {
        GameMatrix target = new GameMatrix();
        Random random = new Random();

        byte[] substitution = createNumbersToDistribute(random, 1);
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            for (int column = 0; column < GameMatrix.SIZE; column++) {
                byte original = matrix.get(row, column);
                if (original == GameMatrix.UNSET) {
                    throw new IllegalArgumentException(
                            "There are unset fields in the given GameMatrix");
                }
                byte substitute = substitution[original - 1];
                target.set(row, column, substitute);
            }
        }

        // row swapping within blocks
        for (int i = 0; i < GameMatrix.BLOCK_COUNT; i++) {
            boolean swap = random.nextBoolean();
            if (swap) {
                // swapped row distance: 1 or 2
                int distance = 1 + random.nextInt(2);
                int offset = 0;
                if (distance != 2) {
                    offset = random.nextInt(2);
                }
                swapRow(matrix,
                        i * GameMatrix.BLOCK_SIZE + offset,
                        i * GameMatrix.BLOCK_SIZE + offset + distance);
            }
        }

        // column swapping within blocks
        for (int i = 0; i < GameMatrix.BLOCK_COUNT; i++) {
            boolean swap = random.nextBoolean();
            if (swap) {
                // swapped column distance: 1 or 2
                int distance = 1 + random.nextInt(2);
                int offset = 0;
                if (distance != 2) {
                    offset = random.nextInt(2);
                }
                swapColumn(matrix,
                        i * GameMatrix.BLOCK_SIZE + offset,
                        i * GameMatrix.BLOCK_SIZE + offset + distance);
            }
        }

        return target;
    }

    /** Swaps two rows in the given matrix.
     * @param matrix the game matrix to swap rows in.
     * @param rowA the first row to swap.
     * @param rowB the second row to swap.
     */
    protected static void swapRow(final GameMatrix matrix,
            final int rowA,
            final int rowB) {
        for (int column = 0; column < GameMatrix.SIZE; column++) {
            byte av = matrix.get(rowA, column);
            byte bv = matrix.get(rowB, column);
            matrix.set(rowB, column, av);
            matrix.set(rowA, column, bv);
        }
    }

    /** Swaps two columns in the given matrix.
     * @param matrix the game matrix to swap rows in.
     * @param columnA the first column to swap.
     * @param columnB the second column to swap.
     */
    protected static void swapColumn(final GameMatrix matrix,
            final int columnA,
            final int columnB) {
        for (int row = 0; row < GameMatrix.SIZE; row++) {
            byte av = matrix.get(row, columnA);
            byte bv = matrix.get(row, columnB);
            matrix.set(row, columnB, av);
            matrix.set(row, columnA, bv);
        }
    }

    /** Create a random array with numbers to distribute.
    ** @param r the random number generator to use.
    ** @param multiplicity the number of times to add the numbers 1 to 9.
    * 1 means adding 1 to 9 only once. 2 means adding 1 to 9 twice.
    * @return an array with randomly ordered numbers from 1 to 9
    * with each number occuring {@code multiplicity} times.
    */
    protected static byte[] createNumbersToDistribute(
            final Random r,
            final int multiplicity) {
        int totalNumbers = GameMatrix.MAXIMUM_VALUE
                - GameMatrix.MINIMUM_VALUE + 1;
        List<Integer> numbersToDistribute = new ArrayList<>(totalNumbers
                * multiplicity);
        for (int number = GameMatrix.MINIMUM_VALUE;
                number <= GameMatrix.MAXIMUM_VALUE;
                number++) {
            for (int j = 0; j < multiplicity; j++) {
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
     * @return {@code true} if the field with the coordinates can be
     * cleared without endangering the unique solvability of the Sudoku.
     */
    private static boolean canClear(final Riddle riddle,
            final int column,
            final int row) {
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
     * @param fullMatrix a fully set up (solved) and valid sudoku.
     * Can be created using {@link #createFull()} or
     * {@link #createVariant(de.sfuhrm.sudoku.GameMatrix)} of a full
     * matrix.
     * @return a maximally cleared sudoku. Contains
     * {@link GameMatrix#UNSET unset} value fields for places where
     * the user/player needs to guess values.
     * @see #createFull()
     * @see #createVariant(de.sfuhrm.sudoku.GameMatrix)
     */
    public static Riddle createRiddle(final GameMatrix fullMatrix) {
        Random random = new Random();

        Riddle cur = new Riddle();
        cur.setAll(fullMatrix.getArray());

        int multi = 0;
        // this could be improved:
        // first the randomized loop can run
        // second a loop over all cells can run
        while (multi < CREATE_RIDDLE_RANDOM_CLEAR) {
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

    /** Fills a block with randomly ordered numbers from 1 to 9.
     * @param row the start row of the block.
     * @param column the start column of the block.
     */
    private void fillBlock(final int row, final int column) {
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
     * @return {@code true} if the search shall be aborted by the
     * call hierarchy or {@code false} if search shall continue.
     */
    private boolean backtrack(
            final int numbersToDistribute,
            final int[] minimumCell) {
        if (numbersToDistribute == 0) {
            if (!riddle.isValid()) {
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
