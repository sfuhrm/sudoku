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
    /** Step size for clear count search around target difficulty. */
    private static final int CLEAR_COUNT_SEARCH_STEP = 2;
    /** Maximum recursion depth for difficulty backtracking. */
    private static final int DIFFICULTY_SEARCH_MAX_DEPTH = 8;
    /** Number of candidates to evaluate per search node. */
    private static final int DIFFICULTY_SEARCH_RETRIES = 3;

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
        assert bitIndex >= 0;
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
        assert columnA != columnB;
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
     * Creates a maximally cleared riddle setup sudoku.
     *
     * @param fullMatrix a fully set up (solved) and valid sudoku.
     * @return a maximally cleared sudoku.
     */
    public static Riddle createRiddle(final GameMatrix fullMatrix) {
        return createRiddle(fullMatrix,
                fullMatrix.getSchema().getTotalFields());
    }

    /**
     * Creates a riddle setup sudoku with selectable difficulty.
     *
     * @param fullMatrix a fully set up (solved) and valid sudoku.
     * @param difficulty requested difficulty level.
     * @return a sudoku where the number of empty fields is derived from the
     * difficulty and schema.
     */
    public static Riddle createRiddle(
            final GameMatrix fullMatrix,
            final Difficulty difficulty
    ) {
        return createRiddleResult(fullMatrix, difficulty).getRiddle();
    }

    /**
     * Creates a riddle setup sudoku with selectable difficulty and
     * analysis details.
     *
     * @param fullMatrix a fully set up (solved) and valid sudoku.
     * @param difficulty requested difficulty level.
     * @return creation result containing riddle, solve path and score.
     */
    static CreationResult createRiddleResult(
            final GameMatrix fullMatrix,
            final Difficulty difficulty
    ) {
        if (difficulty == null) {
            throw new IllegalArgumentException("difficulty must not be null");
        }

        final int target = difficulty.getMaxNumbersToClear(
                fullMatrix.getSchema());
        final int[] corridor = calculateClearCountCorridor(fullMatrix,
                difficulty);

        return searchDifficultyBacktracking(fullMatrix,
                difficulty,
                target,
                corridor[0],
                corridor[1],
                DIFFICULTY_SEARCH_MAX_DEPTH);
    }

    /**
     * Creates a riddle setup sudoku with a maximum amount of cleared fields.
     *
     * @param fullMatrix a fully set up (solved) and valid sudoku.
     * @param maxNumbersToClear maximum amount of numbers to clear.
     * @return a sudoku with up to the given amount of cleared fields.
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

    /**
     * Searches a riddle in a clear-count corridor by backtracking.
     *
     * @param fullMatrix solved matrix to derive riddles from.
     * @param targetDifficulty requested target difficulty.
     * @param targetClearCount configured clear-count target.
     * @param minimumClearCount lower clear-count boundary.
     * @param maximumClearCount upper clear-count boundary.
     * @param depth recursion depth limit.
     * @return best found creation result.
     */
    private static CreationResult searchDifficultyBacktracking(
            final GameMatrix fullMatrix,
            final Difficulty targetDifficulty,
            final int targetClearCount,
            final int minimumClearCount,
            final int maximumClearCount,
            final int depth) {
        int pivot = (minimumClearCount + maximumClearCount) / 2;
        CreationResult best = evaluateClearCount(fullMatrix,
                targetDifficulty,
                targetClearCount,
                pivot,
                null);

        if (best.getClassifiedDifficulty() == targetDifficulty || depth <= 0
                || minimumClearCount >= maximumClearCount) {
            return best;
        }

        boolean tooEasy = best.getClassifiedDifficulty().ordinal()
                < targetDifficulty.ordinal();
        int preferredMin = tooEasy
                ? Math.min(maximumClearCount,
                pivot + CLEAR_COUNT_SEARCH_STEP)
                : minimumClearCount;
        int preferredMax = tooEasy
                ? maximumClearCount
                : Math.max(minimumClearCount,
                pivot - CLEAR_COUNT_SEARCH_STEP);

        if (preferredMin <= preferredMax) {
            CreationResult preferred = searchDifficultyBacktracking(fullMatrix,
                    targetDifficulty,
                    targetClearCount,
                    preferredMin,
                    preferredMax,
                    depth - 1);
            best = pickBetterResult(targetDifficulty,
                    targetClearCount,
                    best,
                    preferred);
            if (best.getClassifiedDifficulty() == targetDifficulty) {
                return best;
            }
        }

        int secondaryMin = tooEasy
                ? minimumClearCount
                : Math.min(maximumClearCount,
                pivot + CLEAR_COUNT_SEARCH_STEP);
        int secondaryMax = tooEasy
                ? Math.max(minimumClearCount,
                pivot - CLEAR_COUNT_SEARCH_STEP)
                : maximumClearCount;
        if (secondaryMin <= secondaryMax) {
            CreationResult secondary = searchDifficultyBacktracking(fullMatrix,
                    targetDifficulty,
                    targetClearCount,
                    secondaryMin,
                    secondaryMax,
                    depth - 1);
            best = pickBetterResult(targetDifficulty,
                    targetClearCount,
                    best,
                    secondary);
        }

        return best;
    }

    /**
     * Evaluates one clear-count point multiple times and keeps the best result.
     *
     * @param fullMatrix solved matrix.
     * @param targetDifficulty requested difficulty.
     * @param targetClearCount configured clear-count target.
     * @param clearCount count to clear.
     * @param best seed best result.
     * @return best result for this clear count.
     */
    private static CreationResult evaluateClearCount(
            final GameMatrix fullMatrix,
            final Difficulty targetDifficulty,
            final int targetClearCount,
            final int clearCount,
            final CreationResult best) {
        CreationResult localBest = best;
        for (int retry = 0; retry < DIFFICULTY_SEARCH_RETRIES; retry++) {
            Riddle riddle = createRiddle(fullMatrix, clearCount);
            RiddleAnalysis analysis = RiddleAnalyzer.analyze(riddle);
            CreationResult candidate = new CreationResult(riddle, analysis);
            localBest = pickBetterResult(targetDifficulty,
                    targetClearCount,
                    localBest,
                    candidate);
            if (candidate.getClassifiedDifficulty() == targetDifficulty) {
                return candidate;
            }
        }
        return localBest;
    }

    /**
     * Calculates a difficulty-specific clear-count corridor.
     *
     * @param fullMatrix solved matrix.
     * @param difficulty requested difficulty.
     * @return array with minimum and maximum clear count.
     */
    private static int[] calculateClearCountCorridor(
            final GameMatrix fullMatrix,
            final Difficulty difficulty) {
        int totalFields = fullMatrix.getSchema().getTotalFields();
        int target = difficulty.getMaxNumbersToClear(fullMatrix.getSchema());

        int min;
        int max;
        if (difficulty == Difficulty.VERY_EASY) {
            min = 0;
            max = midpoint(target,
                    Difficulty.EASY.getMaxNumbersToClear(
                            fullMatrix.getSchema()));
        } else if (difficulty == Difficulty.VERY_HARD) {
            min = midpoint(Difficulty.HARD.getMaxNumbersToClear(
                    fullMatrix.getSchema()), target);
            max = totalFields - 1;
        } else {
            Difficulty previous = Difficulty.values()[difficulty.ordinal() - 1];
            Difficulty next = Difficulty.values()[difficulty.ordinal() + 1];
            min = midpoint(previous.getMaxNumbersToClear(
                    fullMatrix.getSchema()), target);
            max = midpoint(target,
                    next.getMaxNumbersToClear(fullMatrix.getSchema()));
        }

        return new int[] {
                Math.max(0, min),
                Math.min(totalFields - 1, max)
        };
    }

    /**
     * Returns the midpoint of two numbers.
     *
     * @param left first value.
     * @param right second value.
     * @return midpoint.
     */
    private static int midpoint(final int left, final int right) {
        return (left + right) / 2;
    }

    /**
     * Compares two creation results and returns the better one.
     *
     * @param targetDifficulty requested target difficulty.
     * @param targetClearCount target clear count.
     * @param best current best result.
     * @param candidate candidate result.
     * @return better result.
     */
    private static CreationResult pickBetterResult(
            final Difficulty targetDifficulty,
            final int targetClearCount,
            final CreationResult best,
            final CreationResult candidate) {

        assert best != null || candidate != null;
        assert targetClearCount > 0;
        if (best == null) {
            return candidate;
        }

        int candidateDistance = Math.abs(targetDifficulty.ordinal()
                - candidate.getClassifiedDifficulty().ordinal());
        int bestDistance = Math.abs(targetDifficulty.ordinal()
                - best.getClassifiedDifficulty().ordinal());

        if (candidateDistance < bestDistance) {
            return candidate;
        }
        if (candidateDistance > bestDistance) {
            return best;
        }

        int candidateCleared = candidate.getRiddle().getSchema()
                .getTotalFields()
                - candidate.getRiddle().getSetCount();
        int bestCleared = best.getRiddle().getSchema().getTotalFields()
                - best.getRiddle().getSetCount();
        int candidateClearDistance = Math.abs(candidateCleared
                - targetClearCount);
        int bestClearDistance = Math.abs(bestCleared - targetClearCount);
        if (candidateClearDistance < bestClearDistance) {
            return candidate;
        }
        return best;
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
        assert minimumRow >= 0 && minimumRow < schema.getWidth();
        assert minimumColumn >= 0 && minimumColumn < schema.getWidth();
        int remainingChoices = minimumFree;
        while (remainingChoices != 0) {
            int selectedBit = remainingChoices & -remainingChoices;
            assert Integer.bitCount(selectedBit) == 1;
            int number = Integer.numberOfTrailingZeros(selectedBit);
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

            remainingChoices ^= selectedBit;
        }
        riddle.set(minimumRow, minimumColumn, schema.getUnsetValue());

        return BacktrackingResult.CONTINUE;
    }
}
