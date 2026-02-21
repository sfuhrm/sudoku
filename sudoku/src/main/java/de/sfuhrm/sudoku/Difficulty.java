package de.sfuhrm.sudoku;

/**
 * Difficulty levels for puzzle generation.
 */
public enum Difficulty {
    /** Many givens, mostly direct placements. */
    VERY_EASY,
    /** Easy puzzle for beginners. */
    EASY,
    /** Medium puzzle with mixed deduction steps. */
    MEDIUM,
    /** Hard puzzle with fewer givens. */
    HARD,
    /** Very hard puzzle, close to expert level. */
    VERY_HARD;

    /** Supported schema width 4. */
    private static final int WIDTH_4 = 4;
    /** Supported schema width 9. */
    private static final int WIDTH_9 = 9;
    /** Supported schema width 16. */
    private static final int WIDTH_16 = 16;
    /** Supported schema width 25. */
    private static final int WIDTH_25 = 25;

    /** Ratio denominator for one third fallback. */
    private static final int RATIO_THIRD = 3;
    /** Ratio denominator for one half fallback. */
    private static final int RATIO_HALF = 2;
    /** Ratio numerator for three-fifth fallback. */
    private static final int RATIO_THREE = 3;
    /** Ratio denominator for three-fifth fallback. */
    private static final int RATIO_FIVE = 5;
    /** Ratio numerator for two-third fallback. */
    private static final int RATIO_TWO = 2;
    /** Ratio numerator for seven-tenth fallback. */
    private static final int RATIO_SEVEN = 7;
    /** Ratio denominator for seven-tenth fallback. */
    private static final int RATIO_TEN = 10;

    /**
     * Returns a recommended maximum amount of fields to clear for the schema.
     *
     * @param schema schema of the Sudoku to create.
     * @return recommended maximum number of empty fields.
     */
    public int getMaxNumbersToClear(final GameSchema schema) {
        int width = schema.getWidth();
        switch (width) {
            case WIDTH_4:
                return configuredValue(
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_VERY_EASY,
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_EASY,
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_MEDIUM,
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_HARD,
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_VERY_HARD);
            case WIDTH_9:
                return configuredValue(
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_VERY_EASY,
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_EASY,
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_MEDIUM,
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_HARD,
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_VERY_HARD);
            case WIDTH_16:
                return configuredValue(
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_VERY_EASY,
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_EASY,
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_MEDIUM,
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_HARD,
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_VERY_HARD);
            case WIDTH_25:
                return configuredValue(
                        Creator.RIDDLE_25X25_EMPTY_FIELDS_VERY_EASY,
                        Creator.RIDDLE_25X25_EMPTY_FIELDS_EASY,
                        Creator.RIDDLE_25X25_EMPTY_FIELDS_MEDIUM,
                        Creator.RIDDLE_25X25_EMPTY_FIELDS_HARD,
                        Creator.RIDDLE_25X25_EMPTY_FIELDS_VERY_HARD);
            default:
                return fallback(width);
        }
    }

    private int configuredValue(final int veryEasy,
            final int easy,
            final int medium,
            final int hard,
            final int veryHard) {
        switch (this) {
            case VERY_EASY:
                return veryEasy;
            case EASY:
                return easy;
            case MEDIUM:
                return medium;
            case HARD:
                return hard;
            case VERY_HARD:
            default:
                return veryHard;
        }
    }

    private int fallback(final int width) {
        int totalFields = width * width;
        switch (this) {
            case VERY_EASY:
                return totalFields / RATIO_THIRD;
            case EASY:
                return totalFields / RATIO_HALF;
            case MEDIUM:
                return (totalFields * RATIO_THREE) / RATIO_FIVE;
            case HARD:
                return (totalFields * RATIO_TWO) / RATIO_THIRD;
            case VERY_HARD:
            default:
                return (totalFields * RATIO_SEVEN) / RATIO_TEN;
        }
    }
}
