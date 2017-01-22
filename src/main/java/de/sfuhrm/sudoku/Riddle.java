package de.sfuhrm.sudoku;

/**
 * The Sudoku game field.
 */
public class Riddle extends GameMatrix implements Cloneable {

    /**
     * Whether the cell is writable. Pre-defined cells are only readable, use
     * settable cells are writable.
     */
    private boolean writeable[][];

    /**
     * A mask that has bits 1 to 9 set (decimal 1022).
     */
    final static int MASK_FOR_NINE_BITS
            = 1 << 1
            | 1 << 2
            | 1 << 3
            | 1 << 4
            | 1 << 5
            | 1 << 6
            | 1 << 7
            | 1 << 8
            | 1 << 9;

    /**
     * Creates an empty full-writable riddle.
     */
    public Riddle() {
        writeable = new boolean[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                setWritable(j, i, true);
            }
        }
    }

    /**
     * Creates a pre-filled full-writable riddle.
     *
     * @param initializationData a 9x9 array containing the values to init the
     * riddle with.
     */
    public Riddle(byte initializationData[][]) {
        super(initializationData);
        writeable = new boolean[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                setWritable(j, i, true);
            }
        }
    }
    
    /** Finds the duplicate bits. 
     * @param data the cell data from 0-9.
     * @return a mask with bits 1-9 set if the numbers 1-9 occur multiple times.
     */
    private static int findDuplicateBits(final byte data[]) {
        int currentMask = 0;
        int duplicates = 0;
        for (int i = 0; i < data.length; i++) {
            final int shifted = 1 << data[i];
            duplicates |= currentMask & shifted;
            currentMask |= shifted;
        }
        return duplicates & (~1);
    }
    
    /** Finds the used numbers. 
     * @param data the cell data from 0-9.
     * @return a mask with bits 1-9 set if the numbers 1-9 occur.
     */
    static int getNumberMask(final byte data[]) {
        int currentMask = 0;
        for (int i = 0; i < data.length; i++) {
            currentMask |= 1 << data[i];
        }
        return currentMask & (~1);
    }
    
    /** Finds the used numbers. 
     * @param data the cell data from 0-9.
     * @return a mask with bits 1-9 set if the numbers 1-9 occur.
     */
    private static int getSetCount(final byte data[]) {
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            count += data[i] == 0 ? 1 : 0;
        }
        return count;
    }
    
    /**
     * Checks if the effect of one element is valid. This can be used to check
     * validity after setting one field. This is much quicker than
     * {@link #isValid()}.
     *
     */
    public final boolean isValid(int column, int row) {
        boolean result = true;
        
        byte data[] = new byte[9];

        if (result) {
            row(row, data);
            result &= findDuplicateBits(data) == 0;
        }

        if (result) {
            column(column, data);
            result &= findDuplicateBits(data) == 0;
        }

        if (result) {
            block(roundToBlock(row), roundToBlock(column), data);
            result &= findDuplicateBits(data) == 0;
        }
        return result;
    }

    /**
     * Checks if the whole play field is valid.
     */
    public final boolean isValid() {
        boolean result = true;

        byte data[] = new byte[9];
        
        for (int i = 0; i < SIZE && result; i++) {
            row(i, data);
            result &= findDuplicateBits(data) == 0;
        }

        for (int i = 0; i < SIZE && result; i++) {
            column(i, data);
            result &= findDuplicateBits(data) == 0;
        }

        for (int i = 0; i < BLOCK_SIZE && result; i++) {
            for (int j = 0; j < BLOCK_SIZE && result; j++) {
                block(roundToBlock(i), roundToBlock(j), data);
                result &= findDuplicateBits(data) == 0;
            }
        }

        return result;
    }

    /**
     * Set a certain field writable.
     */
    public final void setWritable(final int col, final int row, final boolean set) {
        writeable[row][col] = set;
    }
    
    public int getFreeMask(int column, int row) {
        byte data[] = new byte[9];
        int used = 0;
        row(row, data);
        used |= getNumberMask(data);
        column(column, data);
        used |= getNumberMask(data);
        block(roundToBlock(row), roundToBlock(column), data);
        used |= getNumberMask(data);
        return (~used) & MASK_FOR_NINE_BITS;
    }

    /**
     * Checks if the effect of one set operation is valid. This is much quicker
     * than {@link #isValid()}.
     *
     */
    public final boolean canSet(int col, int row, byte value) {
        int free = getFreeMask(col, row);
        return (free & (1<<value)) != 0;
    }
    
    private static int roundToBlock(int in) {
        return in - in % BLOCK_SIZE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isSet(i, j)) {
                    sb.append(Integer.toString(get(i, j)));
                } else {
                    sb.append('_');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public Object clone() {
        Riddle clone;
        clone = (Riddle) super.clone();
        clone.writeable = cloneArray(writeable);
        return clone;
    }
}
