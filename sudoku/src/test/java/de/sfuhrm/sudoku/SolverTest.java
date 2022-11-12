/*
Sudoku - a fast Java Sudoku game creation library.
Copyright (C) 2017-2018  Stephan Fuhrmann

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
Boston, MA  02110-1301, USA.
*/
package de.sfuhrm.sudoku;

import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link Solver}.
 * @author Stephan Fuhrmann
 */
public class SolverTest {

    private final GameSchema schema = GameSchemas.SCHEMA_9X9;

    @Test
    public void testSolveWithNoStep() {
        // create full matrix
        GameMatrix matrix = Creator.createFull(schema);

        RiddleImpl riddle = new RiddleImpl(schema);
        riddle.setAll(matrix.getArray());

        Solver solver = new Solver(riddle);
        List<GameMatrix> solutions = solver.solve();
        assertEquals(1, solutions.size());
        assertEquals(matrix, solutions.get(0));
    }

    @Test
    public void testSolveWithOneStep() {
        // create full matrix
        GameMatrix matrix = Creator.createFull(schema);
        Random random = new Random();

        int row = random.nextInt(9);
        int column = random.nextInt(9);

        RiddleImpl riddle = new RiddleImpl(schema);
        riddle.setAll(matrix.getArray());
        riddle.set(row, column, schema.getUnsetValue());
        riddle.setWritable(row, column, true);

        Solver solver = new Solver(riddle);
        List<GameMatrix> solutions = solver.solve();
        assertEquals(1, solutions.size());
        assertEquals(matrix, solutions.get(0));
    }

    @Test
    public void testSolveWithExampleProblem() {
        byte[][] riddleBytes = QuadraticArrays.parse(
                "......3..",
                "9.3....2.",
                ".....8.1.",
                ".........",
                "6...2..51",
                "..8.5.79.",
                "73.5..96.",
                "5....2...",
                "29.7.158.");
        RiddleImpl riddle = new RiddleImpl(schema);
        riddle.setAll(riddleBytes);

        byte[][] solutionBytes = QuadraticArrays.parse(
                "862419375",
                "913675428",
                "475238619",
                "159847236",
                "647923851",
                "328156794",
                "731584962",
                "586392147",
                "294761583");
        GameMatrixImpl solution = new GameMatrixImpl(schema);
        solution.setAll(solutionBytes);

        Solver solver = new Solver(riddle);
        List<GameMatrix> solutions = solver.solve();
        assertEquals(1, solutions.size());
        assertEquals(solution, solutions.get(0));
    }
}
