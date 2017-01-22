package de.sfuhrm.sudoku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Solves a partially filled Sudoku. Can find multiple solutions if they are
 * there.
 *
 * @author fury
 */
public class Solver {

    /**
     * Current working copy.
     */
    private Riddle field;

    private List<Riddle> solvedFields;

    public final static int limit = 20;

    Solver(Riddle f) {
        field = (Riddle) f.clone();
        solvedFields = new ArrayList<>();
    }

    /**
     * Solves the Sudoku problem.
     *
     * @return the found solutions. Should be only one.
     */
    public List<Riddle> solve() {

        solvedFields.clear();
        int freeCells = GameMatrix.SIZE * GameMatrix.SIZE - field.getSetCount();

        backtrack(freeCells);

        return Collections.unmodifiableList(solvedFields);
    }

    /**
     * Solves a sudoku.
     *
     * @param freeCells number of free cells, abort criterion
     * @return the number of solutions.
     */
    private int backtrack(int freeCells) {

        // just one result, we have no more to choose
        if (freeCells == 0) {

            if (solvedFields.size() < limit) {
                solvedFields.add((Riddle) field.clone());
            }

            return 1;
        }

        boolean hasMin = false;
        int result = 0;
        int minI = 0;
        int minJ = 0;
        int minV = 0;
        int minBits = 0;
        // find the cell with the smallest number of free bits
        // TODO this look is the hot spot. Could do something with a list
        // sorted by free bit count, but this requires nasty structures.
        for (int i = 0; i < Riddle.SIZE && (hasMin == false || minBits != 1); i++) {
            for (int j = 0; j < Riddle.SIZE && (hasMin == false || minBits != 1); j++) {
                if (field.get(j, i) != Riddle.UNSET) {
                    continue;
                }

                int free = field.getFreeMask(i, j);
                if (free == 0) {
                    // the field is empty, but there are no values we could
                    // insert.
                    // this means we have a paradoxon and must abort this
                    // backtrack branch
                    return 0;
                }

                int bits = Integer.bitCount(free);

                if ((!hasMin) || bits < minBits) {
                    minV = free;
                    minI = i;
                    minJ = j;
                    minBits = bits;
                    hasMin = true;
                }
            }
        }

        if (hasMin) {
            // else we are done

            // now try each number
            for (int bit = 0; bit < minBits; bit++) {
                int idx = Creator.getSetBitOffset(minV, bit);
                if (idx < 0) {
                    throw new IllegalStateException("minV=" + minV + ", i=" + bit + ", idx=" + idx);
                }

                field.set(minJ, minI, (byte) idx);
                int resultCount = backtrack(freeCells - 1);
                result += resultCount;
            }
            field.set(minJ, minI, Riddle.UNSET);
        } else {
            // freeCells != 0 and !hasMin -> dead end
            result = 0;
        }
        return result;
    }

    public static void main(String args[]) throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        Vector<String> lines = new Vector<String>();

        for (int i = 0; i < Riddle.SIZE; i++) {
            String line = br.readLine();
            lines.add(line);
        }

        byte data[][] = new byte[Riddle.SIZE][Riddle.SIZE];

        for (int i = 0; i < Riddle.SIZE; i++) {
            for (int j = 0; j < Riddle.SIZE; j++) {
                String line = lines.get(i);
                char digit = line.length() > j ? line.charAt(j) : '0';
                int val = Integer.valueOf(digit + "");
                data[j][i] = (byte) val;
            }
        }

        Riddle f = new Riddle();
        f.setAll(data);
        System.out.println(f);

        Solver s = new Solver(f);
        Collection<Riddle> results = s.solve();

        int i = 0;
        for (Riddle fd : results) {
            i++;
            System.out.println(i + ".");
            System.out.println(fd);
        }
    }
}
