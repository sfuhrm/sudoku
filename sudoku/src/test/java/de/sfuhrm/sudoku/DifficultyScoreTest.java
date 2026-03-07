package de.sfuhrm.sudoku;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DifficultyScoreTest {

    @Test
    void testScoreCalculation() {
        List<SolveStep> steps = new ArrayList<>();
        steps.add(new SolveStep(SolveTechnique.NAKED_SINGLE, 0, 0, (byte) 1));
        steps.add(new SolveStep(SolveTechnique.HIDDEN_SINGLE, 0, 1, (byte) 2));
        steps.add(new SolveStep(SolveTechnique.BACKTRACKING, 0, 2, (byte) 3));

        DifficultyScore score = new DifficultyScore(steps);

        assertEquals(103, score.getPoints());
        assertEquals(3, score.getStepCount());
        assertEquals(100, score.getMaximumStepPoints());
    }

    @Test
    void testEmptyScore() {
        List<SolveStep> steps = Collections.emptyList();

        DifficultyScore score = new DifficultyScore(steps);

        assertEquals(0, score.getPoints());
        assertEquals(0, score.getStepCount());
        assertEquals(0, score.getMaximumStepPoints());
    }
}
