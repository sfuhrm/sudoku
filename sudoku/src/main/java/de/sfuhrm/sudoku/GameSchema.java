package de.sfuhrm.sudoku;

/**
 * Defines the dimensions of a {@linkplain GameMatrix}.
 * Usually Sudokus are 9 x 9 fields and have blocks of
 * edge with of 3. The possible numbers are 1 to 9.
 * @author Stephan Fuhrmann
 */
public interface GameSchema {

    /**
     * The value that is assigned to unset fields.
     * @return the unset field representing value.
     */
    byte getUnsetValue();

    /**
     * The valid value that is the minimum (1).
     * @return the minimum digit field representing value.
     */
    byte getMinimumValue();

    /**
     * The valid value that is the maximum (9).
     * @return the maximum digit field representing value.
     */
    byte getMaximumValue();

    /**
     * The size in one dimension.
     * @return the size of the game matrix edge in one dimension.
     */
    int getWidth();

    /**
     * The total number of fields.
     * @return the total number of fields which is the
     * width * width.
     */
    int getTotalFields();

    /** The edge dimension of a NxN block.
     * @return the edge dimension of a block.
     * */
    int getBlockWidth();

    /**
     * The total number of blocks in one dimension.
     * @return the number of blocks in one dimension.
     */
    int getBlockCount();

    /**
     * A mask for all bits set from bit 0 to the bit $width-1.
     * @return the mask of all bits set for all valid number values
     * except the unset value.
     */
    int getBitMask();

    /** Is the value passed in valid for a field?
     * @param b value to check.
     * @return {@code true} if valid.
     */
    boolean validValue(byte b);

    /** Is the coordinate pair passed valid?
     * @param row the row index.
     * @param column the column index.
     * @return {@code true} if valid.
     */
    boolean validCoords(int row, int column);

    /** Is the value passed in valid for a bit mask?
     * @param mask bit mask to check.
     * @return {@code true} if valid.
     */
    boolean validBitMask(int mask);
}
