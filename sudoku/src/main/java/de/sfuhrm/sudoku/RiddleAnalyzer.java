package de.sfuhrm.sudoku;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes riddles and creates a technique-based difficulty score.
 */
final class RiddleAnalyzer {
    /** Marker for unresolved unique candidate coordinates. */
    private static final int NONE = -1;
    /** Marker for duplicate candidates. */
    private static final int MULTIPLE = -2;
    /** Marker row for non-cell-specific steps. */
    private static final int NO_ROW = -1;
    /** Marker column for non-cell-specific steps. */
    private static final int NO_COLUMN = -1;
    /** Marker value for non-cell-specific steps. */
    private static final byte NO_VALUE = 0;

    /** Width of a classic Sudoku. */
    private static final int WIDTH_9X9 = 9;
    /** 9x9 boundary for VERY_EASY. */
    private static final int BORDER_9X9_VERY_EASY = 40;
    /** 9x9 boundary for EASY. */
    private static final int BORDER_9X9_EASY = 90;
    /** 9x9 boundary for MEDIUM. */
    private static final int BORDER_9X9_MEDIUM = 160;
    /** 9x9 boundary for HARD. */
    private static final int BORDER_9X9_HARD = 260;

    /** Scaled boundary factor for VERY_EASY. */
    private static final int SCALE_VERY_EASY = 4;
    /** Scaled boundary factor for EASY. */
    private static final int SCALE_EASY = 8;
    /** Scaled boundary factor for MEDIUM. */
    private static final int SCALE_MEDIUM = 12;
    /** Scaled boundary factor for HARD. */
    private static final int SCALE_HARD = 16;

    /** Utility class. */
    private RiddleAnalyzer() {
    }

    /**
     * Analyze a riddle.
     * @param riddle input riddle.
     * @return analysis with path and score.
     */
    static RiddleAnalysis analyze(final GameMatrix riddle) {
        CachedGameMatrixImpl work =
                new CachedGameMatrixImpl(riddle.getSchema());
        work.setAll(riddle.getArray());
        List<SolveStep> path = new ArrayList<>();

        boolean progress;
        do {
            progress = fillNakedSingles(work, path)
                    || fillHiddenSingles(work, path);
        } while (progress);

        if (work.getSetCount() != work.getSchema().getTotalFields()) {
            Solver solver = new Solver(riddle);
            solver.setLimit(2);
            if (!solver.solve().isEmpty()) {
                path.add(new SolveStep(SolveTechnique.BACKTRACKING,
                        NO_ROW,
                        NO_COLUMN,
                        NO_VALUE));
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

    private static boolean fillHiddenSingleInRows(
            final CachedGameMatrixImpl work,
            final List<SolveStep> path) {
        final int width = work.getSchema().getWidth();
        final byte unset = work.getSchema().getUnsetValue();
        for (int row = 0; row < width; row++) {
            for (int candidate = work.getSchema().getMinimumValue();
                    candidate <= work.getSchema().getMaximumValue();
                    candidate++) {
                int onlyColumn = NONE;
                for (int column = 0; column < width; column++) {
                    if (work.get(row, column) == unset
                            && (work.getFreeMask(row, column)
                            & (1 << candidate)) != 0) {
                        if (onlyColumn != NONE) {
                            onlyColumn = MULTIPLE;
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
                int onlyRow = NONE;
                for (int row = 0; row < width; row++) {
                    if (work.get(row, column) == unset
                            && (work.getFreeMask(row, column)
                            & (1 << candidate)) != 0) {
                        if (onlyRow != NONE) {
                            onlyRow = MULTIPLE;
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
                    int onlyRow = NONE;
                    int onlyColumn = NONE;
                    boolean duplicate = false;
                    for (int row = blockRow;
                            row < blockRow + blockWidth;
                            row++) {
                        for (int column = blockColumn;
                                column < blockColumn + blockWidth;
                                column++) {
                            if (work.get(row, column) == unset
                                    && (work.getFreeMask(row, column)
                                    & (1 << candidate)) != 0) {
                                if (onlyRow != NONE) {
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
        if (schema.getWidth() == WIDTH_9X9) {
            if (points <= BORDER_9X9_VERY_EASY) {
                return Difficulty.VERY_EASY;
            }
            if (points <= BORDER_9X9_EASY) {
                return Difficulty.EASY;
            }
            if (points <= BORDER_9X9_MEDIUM) {
                return Difficulty.MEDIUM;
            }
            if (points <= BORDER_9X9_HARD) {
                return Difficulty.HARD;
            }
            return Difficulty.VERY_HARD;
        }

        int veryEasyBorder = schema.getWidth() * SCALE_VERY_EASY;
        int easyBorder = schema.getWidth() * SCALE_EASY;
        int mediumBorder = schema.getWidth() * SCALE_MEDIUM;
        int hardBorder = schema.getWidth() * SCALE_HARD;

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
