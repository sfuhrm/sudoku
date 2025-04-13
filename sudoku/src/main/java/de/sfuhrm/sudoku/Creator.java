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
     * Sample value for 4x4 and difficulty very easy.
     */
    public static final int RIDDLE_4X4_EMPTY_FIELDS_VERY_EASY = 6;
    /**
     * Sample value for 4x4 and difficulty easy.
     */
    public static final int RIDDLE_4X4_EMPTY_FIELDS_EASY = 7;
    /**
     * Sample value for 4x4 and difficulty medium.
     */
    public static final int RIDDLE_4X4_EMPTY_FIELDS_MEDIUM = 9;
    /**
     * Sample value for 4x4 and difficulty hard.
     */
    public static final int RIDDLE_4X4_EMPTY_FIELDS_HARD = 10;
    /**
     * Sample value for 4x4 and difficulty very hard.
     */
    public static final int RIDDLE_4X4_EMPTY_FIELDS_VERY_HARD = 12;
    /**
     * Sample value for 9x9 and difficulty very easy.
     */
    public static final int RIDDLE_9X9_EMPTY_FIELDS_VERY_EASY = 31;
    /**
     * Sample value for 9x9 and difficulty easy.
     */
    public static final int RIDDLE_9X9_EMPTY_FIELDS_EASY = 45;
    /**
     * Sample value for 9x9 and difficulty medium.
     */
    public static final int RIDDLE_9X9_EMPTY_FIELDS_MEDIUM = 49;
    /**
     * Sample value for 9x9 and difficulty hard.
     */
    public static final int RIDDLE_9X9_EMPTY_FIELDS_HARD = 53;
    /**
     * Sample value for 9x9 and difficulty very hard.
     */
    public static final int RIDDLE_9X9_EMPTY_FIELDS_VERY_HARD = 58;
    /*  (Source: http://zhangroup.aporc.org/images/files/Paper_3485.pdf */

    /**
     * Sample value for 16x16 and difficulty very easy. (needs to be adjusted)
     */
    public static final int RIDDLE_16X16_EMPTY_FIELDS_VERY_EASY = 90;
    /**
     * Sample value for 16x16 and difficulty easy. (needs to be adjusted)
     */
    public static final int RIDDLE_16X16_EMPTY_FIELDS_EASY = 100;
    /**
     * Sample value for 16x16 and difficulty medium. (needs to be adjusted)
     */
    public static final int RIDDLE_16X16_EMPTY_FIELDS_MEDIUM = 120;
    /**
     * Sample value for 16x16 and difficulty hard. (needs to be adjusted)
     */
    public static final int RIDDLE_16X16_EMPTY_FIELDS_HARD = 130;
    /**
     * Sample value for 16x16 and difficulty very hard. (needs to be adjusted)
     */
    public static final int RIDDLE_16X16_EMPTY_FIELDS_VERY_HARD = 140;
    /**
     * Sample value for 25x25 and difficulty very easy. (needs to be adjusted)
     */
    public static final int RIDDLE_25X25_EMPTY_FIELDS_VERY_EASY = 200;
    /**
     * Sample value for 25x25 and difficulty easy. (needs to be adjusted)
     */
    public static final int RIDDLE_25X25_EMPTY_FIELDS_EASY = 220;
    /**
     * Sample value for 25x25 and difficulty medium. (needs to be adjusted)
     */
    public static final int RIDDLE_25X25_EMPTY_FIELDS_MEDIUM = 240;
    /**
     * Sample value for 25x25 and difficulty hard. (needs to be adjusted)
     */
    public static final int RIDDLE_25X25_EMPTY_FIELDS_HARD = 260;
    /**
     * Sample value for 25x25 and difficulty very hard. (needs to be adjusted)
     */
    public static final int RIDDLE_25X25_EMPTY_FIELDS_VERY_HARD = 280;

    /**
     * The result consumer. Consumes a valid sudoku
     * and returns whether to abort (true) or continue (false).
     */
    private final Function<GameMatrix, Boolean> resultConsumer;

    /**
     * Current work in progress.
     */
    private final GameMatrixImpl riddle;

    /** The game schema the riddle is for. */
    private final GameSchema schema;

    /**
     * Possibly found Sudoku.
     */
    private GameMatrix winner;

    /**
     * The random number generator.
     */
    private final Random random;

    /** Private constructor. Use the static methods instead.
     * @param gameSchema the dimensions of the game.
     */
    private Creator(final GameSchema gameSchema) {
        this.schema = gameSchema;
        this.riddle = new CachedGameMatrixImpl(gameSchema);
        this.random = new Random();

        resultConsumer = t -> {
            winner = t;
            return Boolean.TRUE;
        };
    }

    /**
     * Get the index of the nth bit set.
     * @param mask the value to get the bit index from.
     * @param bitIndex the number of the set bit wanted.
     * @return the index of the relative bitIndex set bit counted from 0, or -1
     * if there are no more set bits.
     */
    static int getSetBitOffset(final int mask,
            final int bitIndex) {
        int count = 0; // index of the next bit seen being set
        int workingMask = mask; // the left unseen bits are set
        int low = Integer.numberOfTrailingZeros(workingMask);
        workingMask >>>= low;
        assert (workingMask & 1) == 1 || workingMask == 0;
        for (int i = low; workingMask != 0; i++) {
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
     * No fields are {@link GameSchema#getUnsetValue() unset}.
     */
    public static GameMatrix createFull() {
        return createFull(GameSchemas.SCHEMA_9X9);
    }

    /**
     * Creates a valid fully setup sudoku.
     * @param schema the dimensions of the game to create.
     * @return a fully filled sudoku board.
     * No fields are {@link GameSchema#getUnsetValue() unset}.
     */
    public static GameMatrix createFull(final GameSchema schema) {
        Creator c = new Creator(schema);

        BacktrackingResult backtrackingResult;
        do {
            c.riddle.clear();

            // * 0 0
            // 0 * 0
            // 0 0 *
            //
            // The blocks on the diagonal can be filled independently in random
            // because they can not collide.
            // There can be a contradiction later on anyway.
            for (int i = 0; i < c.riddle.getSchema().getBlockCount(); i++) {
                c.fillBlock(
                        i * schema.getBlockWidth(),
                        i * schema.getBlockWidth());
            }
            // this will always work because the code above
            // creates a valid basis for everything
            backtrackingResult = c.backtrack(schema.getTotalFields()
                            - c.riddle.getSetCount(),
                    new CellIndex());
        } while (backtrackingResult != BacktrackingResult.FOUND);

        return c.winner;
    }

    /** Creates a variant of a fully-filled game matrix. The
     * variant is calculated very efficiently by applying simple
     * transformations.
     * @param fullyFilled the input matrix to transform. All fields need
     * to be filled.
     * @return a transformed variant of the input game matrix.
     * @throws IllegalArgumentException if there are unset fields in the
     * GameMatrix.
     */
    public static GameMatrix createVariant(
            final GameMatrix fullyFilled) {
        GameSchema schema = fullyFilled.getSchema();
        GameMatrix target = new GameMatrixImpl(schema);
        Random random = new Random();

        byte[] substitution = createNumbersToDistribute(schema, random, 1);
        for (int row = 0; row < schema.getWidth(); row++) {
            for (int column = 0; column < schema.getWidth(); column++) {
                byte original = fullyFilled.get(row, column);
                if (original == schema.getUnsetValue()) {
                    throw new IllegalArgumentException(
                            "There are unset fields in the given GameMatrix, "
                            + "sample row " + row + ", column " + column);
                }
                byte substitute = substitution[original - 1];
                target.set(row, column, substitute);
            }
        }

        // row swapping within blocks
        for (int i = 0; i < schema.getBlockCount(); i++) {
            boolean swap = random.nextBoolean();
            if (swap) {
                // swapped row distance: 1 or 2
                int distance = 1 + random.nextInt(2);
                int offset = 0;
                if (distance != 2) {
                    offset = random.nextInt(2);
                }
                swapRow(target,
                        i * schema.getBlockWidth() + offset,
                        i * schema.getBlockWidth() + offset + distance);
            }
        }

        // column swapping within blocks
        for (int i = 0; i < schema.getBlockCount(); i++) {
            boolean swap = random.nextBoolean();
            if (swap) {
                // swapped column distance: 1 or 2
                int distance = 1 + random.nextInt(2);
                int offset = 0;
                if (distance != 2) {
                    offset = random.nextInt(2);
                }
                swapColumn(target,
                        i * schema.getBlockWidth() + offset,
                        i * schema.getBlockWidth() + offset + distance);
            }
        }

        return target;
    }

    /** Swaps two rows in the given matrix.
     * @param matrix the game matrix to swap rows in.
     * @param rowA the first row to swap.
     * @param rowB the second row to swap.
     */
    static void swapRow(final GameMatrix matrix,
            final int rowA,
            final int rowB) {
        int width = matrix.getSchema().getWidth();
        for (int column = 0; column < width; column++) {
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
    static void swapColumn(final GameMatrix matrix,
            final int columnA,
            final int columnB) {
        for (int row = 0; row < matrix.getSchema().getWidth(); row++) {
            byte av = matrix.get(row, columnA);
            byte bv = matrix.get(row, columnB);
            matrix.set(row, columnB, av);
            matrix.set(row, columnA, bv);
        }
    }

    /** Create a random array with numbers to distribute.
     * @param schema the dimensions of the game.
    * @param r the random number generator to use.
    * @param multiplicity the number of times to add the numbers 1 to 9.
    * 1 means adding 1 to 9 only once. 2 means adding 1 to 9 twice.
    * @return an array with randomly ordered numbers from 1 to 9
    * with each number occuring {@code multiplicity} times.
    */
    static byte[] createNumbersToDistribute(
            final GameSchema schema,
            final Random r,
            final int multiplicity) {
        int totalNumbers = schema.getMaximumValue()
                - schema.getMinimumValue() + 1;
        List<Integer> numbersToDistribute = new ArrayList<>(totalNumbers
                * multiplicity);
        for (int number = schema.getMinimumValue();
                number <= schema.getMaximumValue();
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
    private static boolean canClear(final RiddleImpl riddle,
            final int row,
            final int column) {
        GameSchema schema = riddle.getSchema();
        assert riddle.get(row, column) != schema.getUnsetValue();

        // if there's only one free val, it's unique
        int freeMask = riddle.getFreeMask(row, column);
        int freeVals = Integer.bitCount(freeMask);
        if (freeVals == 0) {
            return true;
        }

        int old = riddle.get(row, column);
        riddle.set(row, column, schema.getUnsetValue());

        Solver s = new Solver(riddle);
        s.setLimit(2);
        List<GameMatrix> results = s.solve();
        boolean result = results.size() == 1;

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
     * {@link GameSchema#getUnsetValue() unset} value fields for places where
     * the user/player needs to guess values.
     * @see #createRiddle(GameMatrix, int)
     * @see #createFull()
     * @see #createVariant(de.sfuhrm.sudoku.GameMatrix)
     */
    public static Riddle createRiddle(final GameMatrix fullMatrix) {
        Random random = new Random();

        final GameSchema schema = fullMatrix.getSchema();
        final int width = schema.getWidth();
        final byte unset = schema.getUnsetValue();

        RiddleImpl cur = new RiddleImpl(schema);
        cur.setAll(fullMatrix.getArray());

        int randomClearCount = 0;

        // first the randomized loop runs
        // second a deterministic loop over all cells runs

        // random loop
        while (randomClearCount < CREATE_RIDDLE_RANDOM_CLEAR) {
            int column = random.nextInt(schema.getWidth());
            int row = random.nextInt(schema.getWidth());

            if (cur.get(row, column) != schema.getUnsetValue()) {
                if (canClear(cur, row, column)) {
                    cur.set(row, column, schema.getUnsetValue());
                } else {
                    randomClearCount++;
                }
            }
        }

        // deterministic loop
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < width; row++) {
                if (unset != cur.get(row, column)
                    && canClear(cur, row, column)) {
                    cur.set(row, column, unset);
                }
            }
        }

        // set the preset fields non-writable
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < width; row++) {
                cur.setWritable(row, column, cur.get(row, column)
                        == unset);
            }
        }

        return cur;
    }

    /**
     * Creates a riddle setup sudoku.
     *
     * @param fullMatrix        a fully set up (solved) and valid sudoku.
     *                          Can be created using {@link #createFull()} or
     *                          {@link
     *                          #createVariant(de.sfuhrm.sudoku.GameMatrix)}
     *                          of a full matrix.
     * @param maxNumbersToClear maximum amount of numbers to clear.
     *                          9x9 Sudoku: <p>
     *                          Total number of valid 9x9 Sudoku grids is
     *                          6,670,903,752,021,072,936,960. <p>
     *                          Minimal amount of givens in an initial
     *                          Sudoku puzzle that can yield a unique
     *                          solution is 17 (64 empty cells). <p>
     *                          Sample difficulty levels:
     *                           <ul>
     *                           <li>VERY_EASY: more than 50 given numbers,
     *                           remove less than 31 numbers</li>
     *                           <li>EASY: 36-49 given numbers,
     *                           remove 32-45 numbers</li>
     *                           <li>MEDIUM: 32-35 given numbers,
     *                           remove 46-49 numbers</li>
     *                           <li>HARD: 28-31 given numbers,
     *                           remove 50-53 numbers</li>
     *                           <li>EXPERT: 22-27 given numbers,
     *                           remove 54-59 numbers</li>
     *                           </ul>
     *                          The average maximum amount of numbers to clear
     *                          with the current algorithm and
     *                           9x9 Sudoku is 56. <br> <br>
     *                           16x16 Sudoku: <p>
     *                           The maximum amount of numbers to remove with
     *                           the current algorithm
     *                           in a reasonably good time is ~140. <br> <br>
     *                           25x25 Sudoku: <p>
     *                           The maximum amount of numbers to remove with
     *                           the current algorithm
     *                           in a reasonably good time is ~280. <p>
     * @return a sudoku with the given amount of cleared fields (or less if
     * clearing more cells would endanger the unique solvability of the sudoku)
     * Contains {@link GameSchema#getUnsetValue() unset} value fields for
     * places where the user/player needs to guess values.
     * @see #createRiddle(GameMatrix)
     * @see #createFull()
     * @see #createVariant(de.sfuhrm.sudoku.GameMatrix)
     */
    public static Riddle createRiddle(
            final GameMatrix fullMatrix,
            final int maxNumbersToClear
    ) {
        Random random = new Random();

        final GameSchema schema = fullMatrix.getSchema();
        final int width = schema.getWidth();
        final byte unset = schema.getUnsetValue();

        RiddleImpl cur = new RiddleImpl(schema);
        cur.setAll(fullMatrix.getArray());

        int numbersToClear = maxNumbersToClear;
        int randomClearCount = 0;

        // first the randomized loop runs
        // second a deterministic loop over all cells runs

        // random loop
        while (numbersToClear > 0
                && randomClearCount < CREATE_RIDDLE_RANDOM_CLEAR) {
            int i = random.nextInt(width);
            int j = random.nextInt(width);
            if (cur.get(j, i) != schema.getUnsetValue()) {
                if (canClear(cur, j, i)) {
                    cur.set(j, i, schema.getUnsetValue());
                    numbersToClear--;
                } else {
                    randomClearCount++;
                }
            }
        }

        //deterministic loop
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (numbersToClear > 0
                    && unset != cur.get(j, i)
                    && canClear(cur, j, i)) {
                    cur.set(j, i, unset);
                    numbersToClear--;
                }
            }
        }

        // set the preset fields non-writable
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                cur.setWritable(j, i, cur.get(j, i) == unset);
            }
        }

        return cur;
    }

    /** Fills a block with randomly ordered numbers from 1 to 9.
     * @param row the start row of the block.
     * @param column the start column of the block.
     */
    private void fillBlock(final int row, final int column) {
        final int blockSize = schema.getBlockWidth();

        assert schema.validCoords(row, column);
        assert row % blockSize == 0;
        assert column % blockSize == 0;

        byte[] numbers = createNumbersToDistribute(schema, random, 1);
        int k = 0;
        for (int colOfs = 0; colOfs < blockSize; colOfs++) {
            for (int rowOfs = 0; rowOfs < blockSize; rowOfs++) {
                riddle.set(row + rowOfs, column + colOfs, numbers[k++]);
            }
        }
    }

    /** Result for {@linkplain #backtrack(int, CellIndex)}. */
    enum BacktrackingResult {
        /** A result was found, abort. */
        FOUND,
        /** No result found, continue search. */
        CONTINUE,
        /** There's a contradiction in the matrix that can't be solved.
         * */
        CONTRADICTION
    }

    /**
     * Do the backtracking job.
     * @param numbersToDistribute the count of fields left to fill.
     * @param minimumCell coordinates of the so-far minimum cell.
     * @return {@code true} if the search shall be aborted by the
     * call hierarchy or {@code false} if search shall continue.
     */
    private BacktrackingResult backtrack(
            final int numbersToDistribute,
            final CellIndex minimumCell) {
        if (numbersToDistribute == 0) {
            assert riddle.isValid()
                    : "Riddle went non-valid while backtracking";
            if (resultConsumer.apply(riddle)) {
                return BacktrackingResult.FOUND;
            } else {
                return BacktrackingResult.CONTINUE;
            }
        }

        // determine rows + cols that are possible candidates
        // (reduce random trying)
        GameMatrixImpl.FreeCellResult result =
                riddle.findLeastFreeCell(minimumCell);
        switch (result) {
            case CONTRADICTION:
            case NONE_FREE:
                // we have non-free cells, but not numbersToDistribute == 0
                return BacktrackingResult.CONTRADICTION;
            default:
                break;
        }

        int minimumRow = minimumCell.row;
        int minimumColumn = minimumCell.column;
        int minimumFree = riddle.getFreeMask(minimumRow, minimumColumn);
        int minimumBits = Integer.bitCount(minimumFree);

        for (int bit = 0; bit < minimumBits; bit++) {
            int number = getSetBitOffset(minimumFree, bit);
            assert number >= schema.getMinimumValue()
                    && number <= schema.getMaximumValue();
            assert (riddle.getFreeMask(minimumRow, minimumColumn)
                    & (1 << number)) == 1 << number;
            riddle.set(minimumRow, minimumColumn, (byte) (number));
            assert (riddle.getFreeMask(minimumRow, minimumColumn)
                    & (1 << number)) == 0;
            BacktrackingResult subResult = backtrack(
                    numbersToDistribute - 1,
                    minimumCell);
            if (subResult == BacktrackingResult.FOUND) {
                return subResult;
            }
        }
        riddle.set(minimumRow, minimumColumn, schema.getUnsetValue());

        return BacktrackingResult.CONTINUE;
    }
}
