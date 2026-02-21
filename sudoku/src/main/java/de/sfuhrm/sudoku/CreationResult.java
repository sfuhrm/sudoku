package de.sfuhrm.sudoku;

import java.util.List;
import java.util.Objects;

/**
 * Result for creation with difficulty analysis details.
 */
final class CreationResult {
    /** Created riddle. */
    private final Riddle riddle;
    /** Difficulty analysis data. */
    private final RiddleAnalysis analysis;

    /**
     * Constructor.
     * @param createdRiddle generated riddle.
     * @param riddleAnalysis analysis details.
     */
    CreationResult(final Riddle createdRiddle,
            final RiddleAnalysis riddleAnalysis) {
        this.riddle = Objects.requireNonNull(createdRiddle,
                "createdRiddle is null");
        this.analysis = Objects.requireNonNull(riddleAnalysis,
                "riddleAnalysis is null");
    }

    /**
     * Generated riddle.
     * @return generated riddle.
     */
    Riddle getRiddle() {
        return riddle;
    }

    /**
     * Aggregated difficulty score.
     * @return score details.
     */
    DifficultyScore getScore() {
        return analysis.getScore();
    }

    /**
     * Recorded solving steps.
     * @return immutable solving path.
     */
    List<SolveStep> getPath() {
        return analysis.getPath();
    }

    /**
     * Difficulty class measured from the path score.
     * @return classified difficulty.
     */
    Difficulty getClassifiedDifficulty() {
        return analysis.getClassifiedDifficulty();
    }
}
