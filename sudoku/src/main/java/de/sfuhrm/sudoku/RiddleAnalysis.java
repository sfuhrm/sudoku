package de.sfuhrm.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Result of analyzing a riddle's difficulty.
 */
final class RiddleAnalysis {
    /** Recorded solving path. */
    private final List<SolveStep> path;
    /** Aggregated score. */
    private final DifficultyScore score;
    /** Classified difficulty. */
    private final Difficulty classifiedDifficulty;

    /**
     * Constructor.
     * @param solvePath solving path.
     * @param difficulty classified difficulty.
     */
    RiddleAnalysis(final List<SolveStep> solvePath,
            final Difficulty difficulty) {
        Objects.requireNonNull(solvePath, "solvePath is null");
        this.path = Collections.unmodifiableList(new ArrayList<>(solvePath));
        this.score = new DifficultyScore(path);
        this.classifiedDifficulty = Objects.requireNonNull(difficulty,
                "difficulty is null");
    }

    /**
     * Path of logged solving steps.
     * @return immutable solve path.
     */
    List<SolveStep> getPath() {
        return path;
    }

    /**
     * Aggregated score over the solve path.
     * @return score details.
     */
    DifficultyScore getScore() {
        return score;
    }

    /**
     * Difficulty class derived from the score.
     * @return classified difficulty.
     */
    Difficulty getClassifiedDifficulty() {
        return classifiedDifficulty;
    }
}
