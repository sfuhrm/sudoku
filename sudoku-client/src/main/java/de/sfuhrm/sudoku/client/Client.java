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
import de.sfuhrm.sudoku.Riddle;
import java.io.File;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * A Sudoku CLI client.
 * @author Stephan Fuhrmann
 */
public class Client {

    @Option(name = "-n", aliases = {"-count"}, usage = "The number of outputs to create")
    private int count = 1;
    enum Op {
        Full,
        Riddle,
        Both
    }
    
    @Option(name = "-e", aliases = {"-exec"}, usage = "The operation to perform")
    private Op op = Op.Full;
    
    @Option(name = "-t", aliases = {"-time"}, usage = "Show timing information")
    private boolean timing;
    
    @Option(name = "-q", aliases = {"-quiet"}, usage = "No output")
    private boolean quiet;
    
    @Option(name = "-h", aliases = {"-help"}, usage = "Show this command line help")
    private boolean help;
    
    private void run() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            switch (op) {
                case Full: {
                    GameMatrix matrix = Creator.createFull();
                    if (!quiet) System.out.println(matrix);
                    break;
                }
                case Riddle: {
                    GameMatrix matrix = Creator.createFull();
                    Riddle riddle = Creator.createRiddle(matrix);
                    if (!quiet) System.out.println(riddle);
                    break;                    
                }
                case Both: {
                    GameMatrix matrix = Creator.createFull();
                    Riddle riddle = Creator.createRiddle(matrix);
                    if (!quiet) System.out.println(riddle);
                    if (!quiet) System.out.println(matrix);
                    break;
                }
            }
        }
        long end = System.currentTimeMillis();
        
        if (timing) {
            System.err.println("Took total of "+(end-start)+"ms");
            System.err.println("Each iteration took "+(end-start)/count+"ms");
        }
    }
    
    public static void main(String[] args) throws CmdLineException {
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
