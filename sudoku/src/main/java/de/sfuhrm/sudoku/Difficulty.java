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

    /**
     * Returns a recommended maximum amount of fields to clear for the schema.
     *
     * @param schema schema of the Sudoku to create.
     * @return recommended maximum number of empty fields.
     */
    public int getMaxNumbersToClear(final GameSchema schema) {
        int width = schema.getWidth();
        switch (width) {
            case 4:
                return configuredValue(
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_VERY_EASY,
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_EASY,
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_MEDIUM,
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_HARD,
                        Creator.RIDDLE_4X4_EMPTY_FIELDS_VERY_HARD);
            case 9:
                return configuredValue(
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_VERY_EASY,
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_EASY,
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_MEDIUM,
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_HARD,
                        Creator.RIDDLE_9X9_EMPTY_FIELDS_VERY_HARD);
            case 16:
                return configuredValue(
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_VERY_EASY,
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_EASY,
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_MEDIUM,
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_HARD,
                        Creator.RIDDLE_16X16_EMPTY_FIELDS_VERY_HARD);
            case 25:
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
                return totalFields / 3;
            case EASY:
                return totalFields / 2;
            case MEDIUM:
                return (totalFields * 3) / 5;
            case HARD:
                return (totalFields * 2) / 3;
            case VERY_HARD:
            default:
                return (totalFields * 7) / 10;
        }
    }
}
