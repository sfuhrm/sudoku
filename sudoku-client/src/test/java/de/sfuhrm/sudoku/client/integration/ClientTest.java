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
package de.sfuhrm.sudoku.client.integration;

import de.sfuhrm.sudoku.GameMatrix;
import de.sfuhrm.sudoku.client.Client;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.kohsuke.args4j.CmdLineException;

/**
 * Test for {@link Client}.
 * @author Stephan Fuhrmann
 */
public class ClientTest {

    @Test
    public void testMain() throws CmdLineException, UnsupportedEncodingException, IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(arrayOutputStream));
        Client.main(new String[] {});
        String output = arrayOutputStream.toString("UTF-8");
        output = output.trim();
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(GameMatrix.parse(output.split("\n")));
        assertEquals(true, matrix.isValid());
        assertEquals(9*9, matrix.getSetCount());
    }
    
    @Test
    public void testMainWithRiddle() throws CmdLineException, UnsupportedEncodingException, IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(arrayOutputStream));
        Client.main(new String[] {"-e", "Riddle"});
        String output = arrayOutputStream.toString("UTF-8");
        output = output.trim();
        GameMatrix matrix = new GameMatrix();
        matrix.setAll(GameMatrix.parse(output.split("\n")));
        assertEquals(true, matrix.isValid());
    }
    
    @Test
    public void testMainWithSolve() throws CmdLineException, UnsupportedEncodingException, IOException, IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        Path tmpFile = Files.createTempFile("sudoku", ".txt");
        Files.write(tmpFile, Arrays.asList("__4_3_8_6",
                "__1______",
                "5___2__4_",
                "______92_",
                "_____65_3",
                "____73___",
                "___612__7",
                "9______1_",
                "_6_5_____"
        ));
        System.setOut(new PrintStream(arrayOutputStream));
        Client.main(new String[] {"-e", "Solve", "-i", tmpFile.toAbsolutePath().toString()});
        
        String output = arrayOutputStream.toString("UTF-8");
        output = output.trim();
        GameMatrix actual = new GameMatrix();
        actual.setAll(GameMatrix.parse(output.split("\n")));
        assertEquals(true, actual.isValid());
        
        GameMatrix expected = new GameMatrix();
        expected.setAll(GameMatrix.parse(
                "294731856",
                "781465239",
                "536829741",
                "673158924",
                "819246573",
                "425973168",
                "358612497",
                "942387615",
                "167594382"
        ));
        assertEquals(expected, actual);
    }
}
