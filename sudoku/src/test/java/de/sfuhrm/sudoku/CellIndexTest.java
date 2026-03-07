package de.sfuhrm.sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CellIndexTest {

    @Test
    void testToString() {
        CellIndex cellIndex = new CellIndex();
        cellIndex.row = 5;
        cellIndex.column = 10;

        assertEquals("CellIndex{row=5, column=10}", cellIndex.toString());
    }
}
