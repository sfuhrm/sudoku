package de.sfuhrm.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Creates a fully filled sudoku.
 */
public class Creator {

    /**
     * The result found.
     */
    private Riddle winner;

    /**
     * Current work in progress.
     */
    private final Riddle riddle;

    /**
     * The random number generator.
     */
    private final Random random;

    private static int solverCalls;

    Creator() {
        riddle = new Riddle();
        random = new Random();
    }

    /**
     * Get the index of the nth bit set.
     * @param mask the value to get the bit index from.
     * @param bitIndex the number of the set bit wanted.
     * @return the index of the relative bitIndex set bit counted from 0, or -1
     * if there are no more set bits.
     */
    final static int getSetBitOffset(final int mask, final int bitIndex) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if ((mask & 1 << i) != 0) {
                if (count == bitIndex) {
                    return i;
                }
                count++;
            }
        }
        return -1;
    }

    /**
     * Creates a valid fully setup sudoku.
     * @return a fully filled sudoku board.
     */
    public static Riddle createFull() {
        Creator c = new Creator();
        // the number to distribute
        boolean ok = c.backtrack((byte)1, 0, new byte[9]);
        if (!ok)
            throw new IllegalStateException();
        return c.riddle;
    }

    /** Checks whether on the given riddle the given cell can
     * be cleared. A cell can only be cleared if the result remains
     * uniqely solvable.
     * @param riddle riddle to check clearability in.
     * @param column the column in the riddle.
     * @param row the row in the riddle.
     */
    private static boolean canClear(Riddle riddle, int column, int row) {
        if (!riddle.isSet(column, row)) {
            return false;
        }

        // if there's only one free val, it's unique
        int freeMask = riddle.getFreeMask(column, row);
        int freeVals = Integer.bitCount(freeMask);

        if (freeVals == 0) {
            return true;
        }

        int old = riddle.get(column, row);
        riddle.set(column, row, Riddle.UNSET);

        solverCalls++;
        Solver s = new Solver(riddle);
        List<Riddle> results = s.solve();
        boolean result = (results.size() == 1);

        // rollback
        riddle.set(column, row, (byte) old);
        return result;
    }

    /**
     * Creates a riddle setup sudoku.
     *
     * @param in a fully set up (solved) and valid sudoku.
     * @return a maximally cleared sudoku.
     */
    public static Riddle createRiddle(Riddle in) {
        Random random = new Random();

        Riddle cur = (Riddle) in.clone();

        int multi = 0;
        // this could be improved:
        // first the randomized loop can run
        // second a loop over all cells can run
        while (multi < 10) {
            int i = random.nextInt(Riddle.SIZE);
            int j = random.nextInt(Riddle.SIZE);

            if (!cur.isSet(i, j)) {
                continue;
            }

            if (canClear(cur, i, j)) {
                cur.set(i, j, Riddle.UNSET);
            } else {
                multi++;
            }
        }

        for (int i = 0; i < Riddle.SIZE; i++) {
            for (int j = 0; j < Riddle.SIZE; j++) {
                if (!cur.isSet(i, j)) {
                    continue;
                }

                if (canClear(cur, i, j)) {
                    cur.set(i, j, Riddle.UNSET);
                }
            }
        }

        // set the preset fields non-writable
        for (int i = 0; i < Riddle.SIZE; i++) {
            for (int j = 0; j < Riddle.SIZE; j++) {
                cur.setWritable(i, j, !cur.isSet(i, j));
            }
        }

        return cur;
    }
        
    private boolean backtrack(byte number, int i, byte nineArray[]) {
        if (i == 0 && number > 9) {
            return true;
        }

        // determine rows + cols that are possible candidates
        // (reduce random trying)
        for (int row=0; row < GameMatrix.SIZE; row++) {
            riddle.row(row, nineArray);
            int mask = Riddle.getNumberMask(nineArray);
            // number is already set?
            if ((mask & 1<<number) != 0) {
                continue;
            }
            // no number is free?
            if (mask == Riddle.MASK_FOR_NINE_BITS) {
                continue;                
            }
            for (int column=0; column < GameMatrix.SIZE; column++) {
                // cell is not empty?
                if (riddle.get(column, row) != Riddle.UNSET) {
                    continue;
                }
                
                riddle.column(column, nineArray);
                mask = Riddle.getNumberMask(nineArray);
                // number is already set in column?
                if ((mask & 1<<number) != 0) {
                    continue;
                }

                if (riddle.canSet(column, row, number)) {
                    riddle.set(column, row, number);
                    boolean ok;
                    if (i >= 8) {
                        ok = backtrack((byte)(number+1), 0, nineArray);
                    } else {
                        ok = backtrack(number, i+1, nineArray);
                    }
                    if (ok) {
                        return true;
                    }
                    riddle.set(column, row, Riddle.UNSET);
                }
            }
        }

        return false;
    }

    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        int n = 1;
        for (int i = 0; i < n; i++) {

            Riddle f = Creator.createFull();
            String s = f.toString();
            System.out.println(s);
            System.out.println();
            f = Creator.createRiddle(f);
            s = f.toString();
            System.out.println(s);
            System.out.println("Set:" + f.getSetCount());

        }

        System.out.println("Milli time: " + (System.currentTimeMillis() - start) + "ms");
        System.out.println("Solver calls: " + solverCalls + " " + (solverCalls / n) + " per S");
    }
}
