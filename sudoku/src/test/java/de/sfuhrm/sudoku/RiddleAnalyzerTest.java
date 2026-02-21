package de.sfuhrm.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RiddleAnalyzer} and result API.
 */
public class RiddleAnalyzerTest {

    @Test
    public void testSingleMissingCellCreatesNakedSingle() {
        GameMatrix full = Creator.createFull(GameSchemas.SCHEMA_9X9);
        RiddleImpl riddle = new RiddleImpl(GameSchemas.SCHEMA_9X9);
        riddle.setAll(full.getArray());
        riddle.set(0, 0, riddle.getSchema().getUnsetValue());

        RiddleAnalysis analysis = RiddleAnalyzer.analyze(riddle);

        assertEquals(1, analysis.getPath().size());
        assertEquals(SolveTechnique.NAKED_SINGLE,
                analysis.getPath().get(0).getTechnique());
        assertEquals(1, analysis.getScore().getPoints());
        assertEquals(Difficulty.VERY_EASY, analysis.getClassifiedDifficulty());
    }

    @Test
    public void testBacktrackingPenaltyIsAddedForUnresolvedGrid() {
        RiddleImpl riddle = new RiddleImpl(GameSchemas.SCHEMA_9X9);

        RiddleAnalysis analysis = RiddleAnalyzer.analyze(riddle);

        assertTrue(analysis.getPath().stream().anyMatch(step
                -> step.getTechnique() == SolveTechnique.BACKTRACKING));
        assertTrue(analysis.getScore().getPoints() >= 100);
    }

    @Test
    public void testCreateRiddleResultContainsAnalysis() {
        GameMatrix full = Creator.createFull(GameSchemas.SCHEMA_9X9);

        CreationResult result = Creator.createRiddleResult(full,
                Difficulty.EASY);

        assertNotNull(result.getRiddle());
        assertNotNull(result.getPath());
        assertFalse(result.getPath().isEmpty());
        assertTrue(result.getScore().getPoints() > 0);
    }
}
