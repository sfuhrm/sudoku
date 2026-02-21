package de.sfuhrm.sudoku;

import java.util.Objects;

/**
 * A single solving step with technique and position.
 */
final class SolveStep {
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

    /**
     * The solving technique used for this step.
     * @return solve technique.
     */
    public SolveTechnique getTechnique() {
        return technique;
    }

    /**
     * Row index affected by this step.
     * @return row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Column index affected by this step.
     * @return column index.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Value set by this step.
     * @return value.
     */
    public byte getValue() {
        return value;
    }

    /**
     * Points awarded for this step.
     * @return step points.
     */
    public int getPoints() {
        return technique.getPoints();
    }
}
