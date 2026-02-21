package de.sfuhrm.sudoku;

import java.util.Objects;

/**
 * A single solving step with technique and position.
 */
public final class SolveStep {
    /** Technique used for the step. */
    private final SolveTechnique technique;
    /** Row of the affected cell or -1 when not cell-specific. */
    private final int row;
    /** Column of the affected cell or -1 when not cell-specific. */
    private final int column;
    /** Set value or 0 when not cell-specific. */
    private final byte value;

    /**
     * Constructor.
     * @param useTechnique used solving technique.
     * @param inRow row index.
     * @param inColumn column index.
     * @param setValue value set.
     */
    public SolveStep(final SolveTechnique useTechnique,
            final int inRow,
            final int inColumn,
            final byte setValue) {
        this.technique = Objects.requireNonNull(useTechnique,
                "useTechnique is null");
        this.row = inRow;
        this.column = inColumn;
        this.value = setValue;
    }

    public SolveTechnique getTechnique() {
        return technique;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public byte getValue() {
        return value;
    }

    public int getPoints() {
        return technique.getPoints();
    }
}
