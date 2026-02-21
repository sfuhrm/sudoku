package de.sfuhrm.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Difficulty} and difficulty-based creation.
 */
public class DifficultyTest {

    @Test
    public void test9x9UsesConfiguredValues() {
        assertEquals(Creator.RIDDLE_9X9_EMPTY_FIELDS_VERY_EASY,
                Difficulty.VERY_EASY.getMaxNumbersToClear(GameSchemas.SCHEMA_9X9));
        assertEquals(Creator.RIDDLE_9X9_EMPTY_FIELDS_EASY,
                Difficulty.EASY.getMaxNumbersToClear(GameSchemas.SCHEMA_9X9));
        assertEquals(Creator.RIDDLE_9X9_EMPTY_FIELDS_MEDIUM,
                Difficulty.MEDIUM.getMaxNumbersToClear(GameSchemas.SCHEMA_9X9));
        assertEquals(Creator.RIDDLE_9X9_EMPTY_FIELDS_HARD,
                Difficulty.HARD.getMaxNumbersToClear(GameSchemas.SCHEMA_9X9));
        assertEquals(Creator.RIDDLE_9X9_EMPTY_FIELDS_VERY_HARD,
                Difficulty.VERY_HARD.getMaxNumbersToClear(GameSchemas.SCHEMA_9X9));
    }

    @Test
    public void test4x4UsesConfiguredValues() {
        assertEquals(Creator.RIDDLE_4X4_EMPTY_FIELDS_VERY_EASY,
                Difficulty.VERY_EASY.getMaxNumbersToClear(GameSchemas.SCHEMA_4X4));
        assertEquals(Creator.RIDDLE_4X4_EMPTY_FIELDS_VERY_HARD,
                Difficulty.VERY_HARD.getMaxNumbersToClear(GameSchemas.SCHEMA_4X4));
    }

    @Test
    public void testCreateRiddleRejectsNullDifficulty() {
        GameMatrix matrix = Creator.createFull();
        assertThrows(IllegalArgumentException.class,
                () -> Creator.createRiddle(matrix, (Difficulty) null));
    }
}
