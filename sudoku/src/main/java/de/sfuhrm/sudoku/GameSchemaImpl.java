package de.sfuhrm.sudoku;

import java.util.Objects;

/** An immutable implementation of the GameSchema.
 * @see GameSchemas for a list of all supported game schemas.
 * @author Stephan Fuhrmann
 * */
class GameSchemaImpl implements GameSchema {

    /** GameMatrix cell value for an unset cell. */
    private final byte unsetValue;

    /** GameMatrix cell value for the minimum number value (1). */
    private final byte minimumValue;

    /** GameMatrix cell value for the maximum number value
     * (9 for a 9x9 matrix). */
    private final byte maximumValue;

    /** The edge dimension of the game. For a 9x9 game, this is 9.
     * */
    private final int width;

    /** The total number of fields of the game.
     * For a 9x9 game, this is 9x9 = 81.
     * */
    private final int totalFields;

    /** The edge size of a block.
     * For a 9x9 game, this is 3.
     * */
    private final int blockWidth;

    /** The number of blocks in the width.
     *  For a 9x9 game, this is 3. */
    private final int blockCount;

    /** All mask bits set for this game.
     * For a 9x9 game, this is bits 0 to 8 set.
     * */
    private final int bitMask;

    GameSchemaImpl(final byte inUnsetValue,
                          final byte inMinimumValue,
                          final byte inMaximumValue,
                          final int inWidth,
                          final int inBlockWidth) {
        this.unsetValue = inUnsetValue;
        if (inMinimumValue <= inUnsetValue && inUnsetValue <= inMaximumValue) {
            throw new IllegalArgumentException(
                    "unsetValue " + inUnsetValue + " must be outside of "
                            + "minimumValue " + inMinimumValue
                            + " and maximumValue "
                            + inMaximumValue);
        }
        if (inMaximumValue - inMinimumValue + 1 != inWidth) {
            throw new IllegalArgumentException(
                    "maximumValue - minimumValue + 1 ("
                            + inMaximumValue
                            + " - "
                            + inMinimumValue
                            + " + 1) must be equal width");
        }

        this.minimumValue = inMinimumValue;
        this.maximumValue = inMaximumValue;
        if (inWidth != inBlockWidth * inBlockWidth) {
            throw new IllegalArgumentException(
                    "Width (" + inWidth
                            + ") must be blockWidth * blockWidth "
                            + "(" + inBlockWidth
                            + " * " + inBlockWidth + ")");
        }
        if (inWidth <= 0) {
            throw new IllegalArgumentException(
                    "Width (" + inWidth + ") must be >= 0");
        }
        this.width = inWidth;
        this.blockWidth = inBlockWidth;

        this.totalFields = inWidth * inWidth;

        if (inWidth % inBlockWidth != 0) {
            throw new IllegalArgumentException(
                    "Width (" + inWidth + ") can not be divided "
                    + "by block width (" + inBlockWidth + ")");
        }
        this.blockCount = inWidth / inBlockWidth;

        int myBitMask = 0;
        for (int i = minimumValue; i <= maximumValue; i++) {
            myBitMask |= 1 << i;
        }
        this.bitMask = myBitMask;
    }

    @Override
    public byte getUnsetValue() {
        return unsetValue;
    }

    @Override
    public byte getMinimumValue() {
        return minimumValue;
    }

    @Override
    public byte getMaximumValue() {
        return maximumValue;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getTotalFields() {
        return totalFields;
    }

    @Override
    public int getBlockWidth() {
        return blockWidth;
    }

    @Override
    public int getBlockCount() {
        return blockCount;
    }

    @Override
    public int getBitMask() {
        return bitMask;
    }

    /** Is the value passed in valid for a field?
     * @param b value to check.
     * @return {@code true} if valid.
     */
    public boolean validValue(final byte b) {
        return b == unsetValue || (b >= minimumValue && b <= maximumValue);
    }

    /** Is the coordinate pair passed valid?
     * @param row the row index.
     * @param column the column index.
     * @return {@code true} if valid.
     */
    public boolean validCoords(final int row, final int column) {
        return row >= 0 && row < width  && column >= 0 && column < width;
    }

    /** Is the value passed in valid for a bit mask?
     * @param mask bit mask to check.
     * @return {@code true} if valid.
     */
    public boolean validBitMask(final int mask) {
        return (mask & (~bitMask)) == 0;
    }

    public String toString() {
        return String.format("%dx%d", width, width);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameSchemaImpl that = (GameSchemaImpl) o;
        return unsetValue == that.unsetValue
                && minimumValue == that.minimumValue
                && maximumValue == that.maximumValue
                && width == that.width
                && totalFields == that.totalFields
                && blockWidth == that.blockWidth
                && blockCount == that.blockCount
                && bitMask == that.bitMask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                unsetValue,
                minimumValue,
                maximumValue,
                width,
                totalFields,
                blockWidth,
                blockCount,
                bitMask);
    }
}
