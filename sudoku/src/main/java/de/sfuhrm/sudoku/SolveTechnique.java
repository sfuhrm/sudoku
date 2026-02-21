package de.sfuhrm.sudoku;

/**
 * Solving techniques used for difficulty scoring.
 */
enum SolveTechnique {
    /** A single candidate in one cell. */
    NAKED_SINGLE(1),
    /** A candidate that appears only once in a row/column/block. */
    HIDDEN_SINGLE(2),
    /** Non-deductive search is required. */
    BACKTRACKING(100);

    /** Scoring points for one step with this technique. */
    private final int points;

    SolveTechnique(final int scorePoints) {
        this.points = scorePoints;
    }

    /**
     * Points for this solving technique.
     *
     * @return points.
     */
    public int getPoints() {
        return points;
    }
}
