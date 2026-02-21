package de.sfuhrm.sudoku;

import java.util.List;

/**
 * Aggregate score of a solving path.
 */
public final class DifficultyScore {
    /** Sum of all step points. */
    private final int points;
    /** Number of steps. */
    private final int stepCount;
    /** Highest single-step point value in path. */
    private final int maximumStepPoints;

    /**
     * Build score from a path.
     * @param steps solving path.
     */
    public DifficultyScore(final List<SolveStep> steps) {
        int sum = 0;
        int max = 0;
        for (SolveStep step : steps) {
            sum += step.getPoints();
            max = Math.max(max, step.getPoints());
        }
        this.points = sum;
        this.stepCount = steps.size();
        this.maximumStepPoints = max;
    }

    /**
     * Sum of points over the whole path.
     * @return total points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Number of recorded solve steps.
     * @return step count.
     */
    public int getStepCount() {
        return stepCount;
    }

    /**
     * Highest points value of one step.
     * @return maximum single-step points.
     */
    public int getMaximumStepPoints() {
        return maximumStepPoints;
    }
}
