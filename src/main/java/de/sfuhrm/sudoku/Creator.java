package de.sfuhrm.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Creates a fully filled sudoku.
 */
public class Creator {

    /**
     * The result consumer. Consumes a valid sudoku
     * and returns wheter to abort (true) or continue (false).
     */
    private Function<Riddle, Boolean> resultConsumer;

    /**
     * Current work in progress.
     */
    private final Riddle riddle;
    
    /**
     * Possibly found Sudoku.
     */
    private Riddle winner;

    /**
     * The random number generator.
     */
    private final Random random;

    private static int solverCalls;

    Creator() {
        riddle = new CachedRiddle();
        random = new Random();
        
        resultConsumer = t -> {
            winner = t;
            return true;
        };
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
        int iterations = 0;
        while (true) {
            // the number to distribute
            
            c.riddle.clear();
            c.fillBlock(0, 0);
            c.fillBlock(3, 3);
            c.fillBlock(6, 6);
            
            byte[] numbersToDistributeArray = createNumbersToDistribute(c.random, 9-3);        
       
            long limit = System.currentTimeMillis() + 1000;
            boolean ok = c.backtrack(numbersToDistributeArray, 0, new byte[9], Long.MAX_VALUE);
            if (ok)
                break;
            iterations++;
            System.out.println(c.riddle);
        }
        
        return c.riddle;
    }

    /* Create a random array with numbers to distribute. */
    static byte[] createNumbersToDistribute(Random r, int multiplicity) {
        List<Integer> numbersToDistribute= new ArrayList<>(9*multiplicity);
        for (int number = 1; number <= 9; number++) {
            for (int j=0; j < multiplicity; j++) {
                numbersToDistribute.add(number);
            }
        }
        Collections.shuffle(numbersToDistribute, r);
        byte[] numbersToDistributeArray = new byte[numbersToDistribute.size()];
        int k = 0;
        for (Integer number : numbersToDistribute) {
            numbersToDistributeArray[k++] = number.byteValue();
        }
        return numbersToDistributeArray;
    }

    /** Checks whether on the given riddle the given cell can
     * be cleared. A cell can only be cleared if the result remains
     * uniqely solvable.
     * @param riddle riddle to check clearability in.
     * @param column the column in the riddle.
     * @param row the row in the riddle.
     */
    private static boolean canClear(Riddle riddle, int column, int row) {
        if (riddle.get(row, column) == Riddle.UNSET) {
            return false;
        }

        // if there's only one free val, it's unique
        int freeMask = riddle.getFreeMask(row, column);
        int freeVals = Integer.bitCount(freeMask);

        if (freeVals == 0) {
            return true;
        }

        int old = riddle.get(row, column);
        riddle.set(row, column, Riddle.UNSET);

        solverCalls++;
        Solver s = new Solver(riddle);
        List<Riddle> results = s.solve();
        boolean result = (results.size() == 1);

        // rollback
        riddle.set(row, column, (byte) old);
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

            if (cur.get(j, i) == Riddle.UNSET) {
                continue;
            }

            if (canClear(cur, i, j)) {
                cur.set(j, i, Riddle.UNSET);
            } else {
                multi++;
            }
        }

        for (int i = 0; i < Riddle.SIZE; i++) {
            for (int j = 0; j < Riddle.SIZE; j++) {
                if (cur.get(j, i) == Riddle.UNSET) {
                    continue;
                }

                if (canClear(cur, i, j)) {
                    cur.set(j, i, Riddle.UNSET);
                }
            }
        }

        // set the preset fields non-writable
        for (int i = 0; i < Riddle.SIZE; i++) {
            for (int j = 0; j < Riddle.SIZE; j++) {
                cur.setWritable(j, i, cur.get(j, i) == Riddle.UNSET);
            }
        }

        return cur;
    }
    
    private void fillBlock(int column, int row) {
        byte[] numbers = createNumbersToDistribute(random, 1);
        int k = 0;
        for (int i = 0; i < Riddle.BLOCK_SIZE; i++) {
            for (int j = 0; j < Riddle.BLOCK_SIZE; j++) {
                riddle.set(row + j, column + i, numbers[k++]);
            }
        }
    }
    
    private boolean backtrack(byte[] numbersToDistributeArray, int i, byte nineArray[], long timeLimit) {
        if (i == 0) {
            riddle.clear();
        }
        if (i == numbersToDistributeArray.length) {
            return resultConsumer.apply(riddle);
        }
        
        // current number to distribute
        byte number = numbersToDistributeArray[i];

        // determine rows + cols that are possible candidates
        // (reduce random trying)
        for (int row=0; row < GameMatrix.SIZE; row++) {
            
            if (System.currentTimeMillis() > timeLimit) {
                return false;
            }
            
            // no number is free?
            if ((riddle.getRowFreeMask(row) & (1<<number)) == 0) {
                continue;
            }
            for (int column=0; column < GameMatrix.SIZE; column++) {
                // cell is not empty?
                if (riddle.get(row, column) != Riddle.UNSET) {
                    continue;
                }

                if (riddle.canSet(row, column, number)) {
                    riddle.set(row, column, number);
                    boolean ok;
                    ok = backtrack(numbersToDistributeArray, i+1, nineArray, timeLimit);
                    if (ok) {
                        return true;
                    }
                    riddle.set(row, column, Riddle.UNSET);
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
