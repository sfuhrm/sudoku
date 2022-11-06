/*
Sudoku - a fast Java Sudoku game creation library.
Copyright (C) 2017  Stephan Fuhrmann

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
package de.sfuhrm.sudoku.client;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;
import de.sfuhrm.sudoku.GameMatrixFactory;
import de.sfuhrm.sudoku.GameSchema;
import de.sfuhrm.sudoku.GameSchemas;
import de.sfuhrm.sudoku.QuadraticArrays;
import de.sfuhrm.sudoku.Riddle;
import de.sfuhrm.sudoku.Solver;
import de.sfuhrm.sudoku.output.GameMatrixFormatter;
import de.sfuhrm.sudoku.output.JsonArrayFormatter;
import de.sfuhrm.sudoku.output.LatexTableFormatter;
import de.sfuhrm.sudoku.output.MarkdownTableFormatter;
import de.sfuhrm.sudoku.output.PlainTextFormatter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * A Sudoku CLI client.
 * @author Stephan Fuhrmann
 */
public class Client {

    /** The number of outputs to create. */
    @Option(name = "-n",
            aliases = {"-count"},
            usage = "The number of outputs to create")
    private int count = 1;

    /** The operator for the command. */
    enum Op {
        /** Create a fully filled Sudoku. */
        Full,
        /** Create a partly filled Sudoku for a riddle. */
        Riddle,
        /** Create a riddle and the solution. */
        Both,
        /** Solve a Sudoku. */
        Solve
    }

    /** The possible formatters that can be used. */
    enum Formatter {
        /** The {@link PlainTextFormatter}. */
        PlainText(PlainTextFormatter.class),
        /** The {@link MarkdownTableFormatter}. */
        MarkDownTable(MarkdownTableFormatter.class),
        /** The {@link LatexTableFormatter}. */
        LatexTable(LatexTableFormatter.class),
        /** The {@link JsonArrayFormatter}. */
        JsonArray(JsonArrayFormatter.class);

        /** The class of the formatter to create an instance of. */
        private final Class<? extends GameMatrixFormatter> clazz;

        /** Constructs a new instance.
         * @param inClazz the class to construct formatters with.
         */
        Formatter(final Class<? extends GameMatrixFormatter> inClazz) {
            this.clazz = inClazz;
        }

        /** Constructs a new instance of the formatter.
         * @return the freshly created formatter.
         */
        public GameMatrixFormatter newInstance() {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException
                    | IllegalAccessException | InstantiationException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    /** The output format to use. */
    @Option(name = "-f",
            aliases = {"-format"},
            usage = "The output format to use")
    private Formatter format = Formatter.PlainText;

    /** The operation to perform. */
    @Option(name = "-e",
            aliases = {"-exec"},
            usage = "The operation to perform")
    private Op op = Op.Full;

    /** Show timing information. */
    @Option(name = "-t",
            aliases = {"-time"},
            usage = "Show timing information")
    private boolean timing;

    /** No output. */
    @Option(name = "-q",
            aliases = {"-quiet"},
            usage = "No output")
    private boolean quiet;

    /** Input file to read for solving. */
    @Option(name = "-i",
            aliases = {"-input"},
            usage = "Input file to read for solving")
    private Path input;

    /**
     * Maximum amount of Numbers to clear.
     */
    @Option(name = "-c",
            aliases = {"-numberstoclear"},
            usage = "Amount of Numbers to clear.")
    private int maxNumbersToClear = -1;

    /** Game schema.
     * @see de.sfuhrm.sudoku.GameSchemas
     * */
    @Option(name = "-s",
            aliases = {"-schema"},
            usage = "Game matrix size for the generated game."
                    + "A 9x9 sudoku has 9. There are"
                    + "4x4, 9x9, 16x16 and 25x25 sudokus supported.")
    private SchemaEnum schema = SchemaEnum.S9X9;

    /** Show this command line help. */
    @Option(name = "-h",
            aliases = {"-help"},
            usage = "Show this command line help")
    private boolean help;

    /** The GameSchema to use. */
    private enum SchemaEnum {
        /** The 4x4 schema. */
        S4X4(GameSchemas.SCHEMA_4X4),
        /** The 9x9 schema. */
        S9X9(GameSchemas.SCHEMA_9X9),
        /** The 16x16 schema. */
        S16X16(GameSchemas.SCHEMA_16X16),
        /** The 25x25 schema. */
        S25X25(GameSchemas.SCHEMA_25X25);

        /** Reference of the game schema object of the game. */
        private final GameSchema schema;

        /** Constructor.
         * @param inSchema the game schema used in the game.
         * */
        SchemaEnum(final GameSchema inSchema) {
            this.schema = inSchema;
        }
    }

    /** Get the game schema requested in the command line.
     * @return a game schema to use for the game.
     * */
    private GameSchema getSchema() {
        return schema.schema;
    }

    /** Solves a Sudoku.
     * @param formatter the formatter to print the solved Sudoku with.
     * @throws FileNotFoundException if the referenced file does not exist.
     * @throws IOException for other errors related to file IO.
     */
    private void solve(final GameMatrixFormatter formatter)
            throws FileNotFoundException, IOException {
        if (op == Op.Solve && input == null) {
            throw new IllegalArgumentException(
                    "Expecting input file for Solve");
        }

        List<String> lines = Files.readAllLines(input);
        // remove empty lines, replace funny chars with 0
        lines = lines.stream()
                .filter(l -> !l.isEmpty())
                .map(l -> l.replaceAll("[_?.]", "0"))
                .collect(Collectors.toList());

        byte[][] data = QuadraticArrays.parse(lines.toArray(new String[0]));

        GameMatrix gameMatrix = new GameMatrixFactory()
                .newGameMatrix(getSchema());
        gameMatrix.setAll(data);
        Solver solver = new Solver(gameMatrix);
        List<GameMatrix> solutions = solver.solve();
        if (!quiet) {
            for (GameMatrix r : solutions) {
                System.out.println(formatter.format(r));
            }
        }
    }

    /**
     * Runs the client with the parsed command line options.
     * Performs the actions requested by the user.
     * @throws IOException if some IO goes wrong.
     */
    private void run() throws IOException {
        GameMatrixFormatter formatter = format.newInstance();
        long start = System.currentTimeMillis();

        if (!quiet) {
            System.out.print(formatter.documentStart());
        }

        if (op == Op.Solve) {
            solve(formatter);
        } else {
            for (int i = 0; i < count; i++) {
                GameMatrix matrix;
                Riddle riddle;
                switch (op) {
                    case Full:
                        matrix = Creator.createFull(getSchema());
                        if (!quiet) {
                            System.out.println(formatter.format(matrix));
                        }
                        break;
                    case Riddle:
                        matrix = Creator.createFull(getSchema());
                        if (maxNumbersToClear > 0) {
                            riddle = Creator
                                    .createRiddle(matrix, maxNumbersToClear);
                        } else {
                            riddle = Creator.createRiddle(matrix);
                        }
                        if (!quiet) {
                            System.out.println(formatter.format(riddle));
                        }
                        break;
                    case Both:
                        matrix = Creator.createFull(getSchema());
                        if (maxNumbersToClear > 0) {
                            riddle = Creator
                                    .createRiddle(matrix, maxNumbersToClear);
                        } else {
                            riddle = Creator.createRiddle(matrix);
                        }
                        if (!quiet) {
                            System.out.println(formatter.format(matrix));
                            System.out.println(formatter.format(riddle));
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unhandled case "
                                + op);
                }
            }
        }
        long end = System.currentTimeMillis();
        if (!quiet) {
            System.out.print(formatter.documentEnd());
        }

        if (timing) {
            System.err.printf("Took total of %dms%n", (end - start));
            System.err.printf("Each iteration took %fms%n",
                    ((double) end - start) / count);
        }
    }

    /** The program entry for the client.
     * @param args the command line arguments.
     * @throws CmdLineException if command line parsing went wrong.
     * @throws IOException if file IO went wrong.
     */
    public static void main(final String[] args)
            throws CmdLineException, IOException {
        Client client = new Client();
        CmdLineParser parser = new CmdLineParser(client);
        parser.parseArgument(args);
        if (client.help) {
            parser.printUsage(System.out);
            return;
        }

        client.run();
    }
}
