package de.sfuhrm.sudoku;

import java.util.List;
import java.util.Objects;

/**
 * Result for creation with difficulty analysis details.
 */
public final class CreationResult {
    /** Created riddle. */
    private final Riddle riddle;
    /** Difficulty analysis data. */
    private final RiddleAnalysis analysis;

    /**
     * Constructor.
     * @param createdRiddle generated riddle.
     * @param riddleAnalysis analysis details.
     */
    public CreationResult(final Riddle createdRiddle,
            final RiddleAnalysis riddleAnalysis) {
        this.riddle = Objects.requireNonNull(createdRiddle,
                "createdRiddle is null");
        this.analysis = Objects.requireNonNull(riddleAnalysis,
                "riddleAnalysis is null");
    }

    public Riddle getRiddle() {
        return riddle;
    }

    public DifficultyScore getScore() {
        return analysis.getScore();
    }

    public List<SolveStep> getPath() {
        return analysis.getPath();
    }

    public Difficulty getClassifiedDifficulty() {
        return analysis.getClassifiedDifficulty();
    }
}
