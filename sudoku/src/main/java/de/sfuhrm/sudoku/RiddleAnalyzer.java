package de.sfuhrm.sudoku;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes riddles and creates a technique-based difficulty score.
 */
public final class RiddleAnalyzer {
    /** Utility class. */
    private RiddleAnalyzer() {
    }

    /**
     * Analyze a riddle.
     * @param riddle input riddle.
     * @return analysis with path and score.
     */
    public static RiddleAnalysis analyze(final GameMatrix riddle) {
        CachedGameMatrixImpl work = new CachedGameMatrixImpl(riddle.getSchema());
        work.setAll(riddle.getArray());
        List<SolveStep> path = new ArrayList<>();

        while (fillNakedSingles(work, path) || fillHiddenSingles(work, path)) {
            // iterate while progress is made
        }

        if (work.getSetCount() != work.getSchema().getTotalFields()) {
            Solver solver = new Solver(riddle);
            solver.setLimit(2);
            if (!solver.solve().isEmpty()) {
                path.add(new SolveStep(SolveTechnique.BACKTRACKING,
                        -1,
                        -1,
                        (byte) 0));
            }
        }

        Difficulty difficulty = classify(riddle.getSchema(),
                new DifficultyScore(path).getPoints());
        return new RiddleAnalysis(path, difficulty);
    }

    private static boolean fillNakedSingles(final CachedGameMatrixImpl work,
            final List<SolveStep> path) {
        final int width = work.getSchema().getWidth();
        final byte unset = work.getSchema().getUnsetValue();
        for (int row = 0; row < width; row++) {
            for (int column = 0; column < width; column++) {
                if (work.get(row, column) == unset) {
                    int freeMask = work.getFreeMask(row, column);
                    if (Integer.bitCount(freeMask) == 1) {
                        int number = Integer.numberOfTrailingZeros(freeMask);
                        work.set(row, column, (byte) number);
                        path.add(new SolveStep(SolveTechnique.NAKED_SINGLE,
                                row,
                                column,
                                (byte) number));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean fillHiddenSingles(final CachedGameMatrixImpl work,
            final List<SolveStep> path) {
        return fillHiddenSingleInRows(work, path)
                || fillHiddenSingleInColumns(work, path)
                || fillHiddenSingleInBlocks(work, path);
    }

    private static boolean fillHiddenSingleInRows(final CachedGameMatrixImpl work,
            final List<SolveStep> path) {
        final int width = work.getSchema().getWidth();
        final byte unset = work.getSchema().getUnsetValue();
        for (int row = 0; row < width; row++) {
            for (int candidate = work.getSchema().getMinimumValue();
                    candidate <= work.getSchema().getMaximumValue();
                    candidate++) {
                int onlyColumn = -1;
                for (int column = 0; column < width; column++) {
                    if (work.get(row, column) == unset
                            && (work.getFreeMask(row, column)
                            & (1 << candidate)) != 0) {
                        if (onlyColumn != -1) {
                            onlyColumn = -2;
                            break;
                        }
                        onlyColumn = column;
                    }
                }
                if (onlyColumn >= 0) {
                    work.set(row, onlyColumn, (byte) candidate);
                    path.add(new SolveStep(SolveTechnique.HIDDEN_SINGLE,
                            row,
                            onlyColumn,
                            (byte) candidate));
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean fillHiddenSingleInColumns(
            final CachedGameMatrixImpl work,
            final List<SolveStep> path) {
        final int width = work.getSchema().getWidth();
        final byte unset = work.getSchema().getUnsetValue();
        for (int column = 0; column < width; column++) {
            for (int candidate = work.getSchema().getMinimumValue();
                    candidate <= work.getSchema().getMaximumValue();
                    candidate++) {
                int onlyRow = -1;
                for (int row = 0; row < width; row++) {
                    if (work.get(row, column) == unset
                            && (work.getFreeMask(row, column)
                            & (1 << candidate)) != 0) {
                        if (onlyRow != -1) {
                            onlyRow = -2;
                            break;
                        }
                        onlyRow = row;
                    }
                }
                if (onlyRow >= 0) {
                    work.set(onlyRow, column, (byte) candidate);
                    path.add(new SolveStep(SolveTechnique.HIDDEN_SINGLE,
                            onlyRow,
                            column,
                            (byte) candidate));
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean fillHiddenSingleInBlocks(
            final CachedGameMatrixImpl work,
            final List<SolveStep> path) {
        final int blockWidth = work.getSchema().getBlockWidth();
        final int width = work.getSchema().getWidth();
        final byte unset = work.getSchema().getUnsetValue();
        for (int blockRow = 0; blockRow < width; blockRow += blockWidth) {
            for (int blockColumn = 0;
                    blockColumn < width;
                    blockColumn += blockWidth) {
                for (int candidate = work.getSchema().getMinimumValue();
                        candidate <= work.getSchema().getMaximumValue();
                        candidate++) {
                    int onlyRow = -1;
                    int onlyColumn = -1;
                    boolean duplicate = false;
                    for (int row = blockRow; row < blockRow + blockWidth; row++) {
                        for (int column = blockColumn;
                                column < blockColumn + blockWidth;
                                column++) {
                            if (work.get(row, column) == unset
                                    && (work.getFreeMask(row, column)
                                    & (1 << candidate)) != 0) {
                                if (onlyRow != -1) {
                                    duplicate = true;
                                    break;
                                }
                                onlyRow = row;
                                onlyColumn = column;
                            }
                        }
                        if (duplicate) {
                            break;
                        }
                    }
                    if (!duplicate && onlyRow >= 0) {
                        work.set(onlyRow, onlyColumn, (byte) candidate);
                        path.add(new SolveStep(SolveTechnique.HIDDEN_SINGLE,
                                onlyRow,
                                onlyColumn,
                                (byte) candidate));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static Difficulty classify(final GameSchema schema,
            final int points) {
        if (schema.getWidth() == 9) {
            if (points <= 40) {
                return Difficulty.VERY_EASY;
            }
            if (points <= 90) {
                return Difficulty.EASY;
            }
            if (points <= 160) {
                return Difficulty.MEDIUM;
            }
            if (points <= 260) {
                return Difficulty.HARD;
            }
            return Difficulty.VERY_HARD;
        }

        int veryEasyBorder = schema.getWidth() * 4;
        int easyBorder = schema.getWidth() * 8;
        int mediumBorder = schema.getWidth() * 12;
        int hardBorder = schema.getWidth() * 16;

        if (points <= veryEasyBorder) {
            return Difficulty.VERY_EASY;
        }
        if (points <= easyBorder) {
            return Difficulty.EASY;
        }
        if (points <= mediumBorder) {
            return Difficulty.MEDIUM;
        }
        if (points <= hardBorder) {
            return Difficulty.HARD;
        }
        return Difficulty.VERY_HARD;
    }
}
