# Sudoku Java Library ![Travis CI Status](https://travis-ci.org/sfuhrm/sudoku.svg?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sfuhrm/sudoku/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sfuhrm/sudoku)

A Java implementation of a very fast algorithm for creating [Sudoku](https://en.wikipedia.org/wiki/Sudoku) riddles.
Has also the functionality to solve Sudoku riddles.

## Building it

For building the application you need Apache Maven.
Use the following command line:

    $ mvn clean package
    
## Features

In the following list I want to give an overview of the features:

* Very fast algorithm that is using backtracking, but terminates in some fractions of a second. For fully filled boards this is usually less than 1ms on current hardware. For partly-filled riddles this is usually less than 20ms.
* Pure Java implementation without any runtime dependencies.
* Using Java 8.

## Usage

The usage for fully set Sudoku boards (no empty fields) is as following:

---------------------------------------
        GameMatrix matrix = Creator.createFull();        
---------------------------------------

You can create a solvable riddle (with empty fields) using

---------------------------------------
        GameMatrix matrix = Creator.createFull();        
        Riddle riddle = Creator.createRiddle(matrix);
---------------------------------------

And last but not least you can solve a riddle using

---------------------------------------
        Riddle riddle = new Riddle();
        riddle.setAll(GameMatrix.parse("000000000", ...));
        
        Solver solver = new Solver(riddle);
        List<Riddle> solutions = solver.solve();
---------------------------------------

For valid riddles you'll find in magazines there is only one solution in the list.

There is also a [CLI client](sudoku-client) that demonstrates the usage of the library.

## Including it in your projects

Please note that the current version is experimental. It creates and solves riddles. The API will change.
The library could run into a runtime exception.

There are unit tests for many things, but the code is still young.

The recommended way of including the library into your project is using maven:

---------------------------------------
    <dependency>
        <groupId>de.sfuhrm</groupId>
        <artifactId>sudoku</artifactId>
        <version>0.1.2</version>
    </dependency>
---------------------------------------

## Algorithm

The design idea is to use the narrowest bottleneck of the Sudoku board to prune the backtracking
tree to the maximum and get the fastest results.

### Initialization

The algorithm first fills three blocks with numbers in random order to reduce the amount of backtracking.
After that, backtracking for the remaining fields starts.

### Backtracking

The field with the lest number of possible number candidates on the board is searched. 
All candidates are tried until the first candidate leads to a valid backtracking tree path. Backtracking occurs in this loop.

### Warning

At the time of writing it seems that it's enough to reduce each backtracking recursion to one field.
I didn't prove this. Many test iterations show that it works.

## Author

Written 2017 by Stephan Fuhrmann. You can reach me via email to s (at) sfuhrm.de

## License

The project is licensed under [LGPL 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html).
