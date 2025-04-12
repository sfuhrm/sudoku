Sudoku Java Library
===================
[![Java CI with Maven](https://github.com/sfuhrm/sudoku/actions/workflows/maven-ref.yml/badge.svg)](https://github.com/sfuhrm/sudoku/actions/workflows/maven-ref.yml)
[![Coverage](https://raw.githubusercontent.com/sfuhrm/sudoku/refs/heads/gh-pages/jacoco.svg)](https://github.com/sfuhrm/sudoku/actions)
[![javadoc](https://javadoc.io/badge2/de.sfuhrm/sudoku/javadoc.svg)](https://javadoc.io/doc/de.sfuhrm/sudoku)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sfuhrm/sudoku/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sfuhrm/sudoku)
[![ReleaseDate](https://img.shields.io/github/release-date/sfuhrm/sudoku)](https://github.com/sfuhrm/sudoku/releases)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)

A Java implementation of a very fast algorithm for creating [Sudoku](https://en.wikipedia.org/wiki/Sudoku) riddles.
Has also the functionality to solve Sudoku riddles.

The following animation shows how quick the provided command line client can create Sudokus:

![Creating a riddle](http://sfuhrm.de/wp-content/uploads/2017/11/Sudoku-Create-Riddle-SF-1.gif
 "Creating a riddle")
 
 The riddles can be of the schema dimensions:
* 4x4
* 9x9 (standard size)
* 16x16 (too slow at the moment)
* 25x25 (too slow at the moment)

## Building it

For building the application you need Apache Maven.
Use the following command line:

    $ mvn clean package

## Features

In the following list I want to give an overview of the features:

* **Performance**: Very fast algorithm that is using backtracking, but terminates in some fractions of a second. For fully filled boards this is usually less than 1ms on current hardware. For partly-filled riddles this is usually less than 20ms.
* **Pureness**: Pure Java implementation without any runtime dependencies to other libraries. Runs on Java 8+.
* **Quality**: High test coverage of >94%. Many (optional) runtime assertions to assure correct operation.
* **Output**: Plain text, Markdown, LaTeX and JSON output formats. For an example, see [2000-sudokus.pdf](https://github.com/sfuhrm/sudoku/releases/download/v0.1.3/2000-sudokus.pdf), a collection of 2000 Sudokus.

## Usage

You can review the [javadoc](https://javadoc.io/doc/de.sfuhrm/sudoku) for
detailed information.

The usage for fully set Sudoku boards (no empty fields) is as following:

---------------------------------------

```java
GameMatrix matrix = Creator.createFull();
```

---------------------------------------

You can create a solvable riddle (with empty fields) using

---------------------------------------

```java
GameMatrix matrix = Creator.createFull(GameSchemas.SCHEMA_9X9);
Riddle riddle = Creator.createRiddle(matrix);
```

---------------------------------------

A solvable riddle looks like this:

---------------------------------------
|  |  |  |  |  |  |  |  |  |
|---|---|---|---|---|---|---|---|---|
| . | . | 4 | . | . | . | . | 2 | 9 |
| . | 2 | . | . | . | 4 | . | . | . |
| 6 | . | . | . | . | . | . | . | 3 |
| 2 | 4 | . | . | . | 3 | . | . | . |
| 5 | . | . | . | . | . | 9 | . | . |
| . | 7 | . | 5 | . | . | 8 | . | . |
| . | . | . | . | . | 7 | 1 | 6 | . |
| 1 | . | . | . | . | 6 | . | 9 | . |
| . | . | . | 4 | . | 2 | . | . | 8 |
|  |  |  |  |  |  |  |  |  |
---------------------------------------

And last but not least you can solve a riddle using

---------------------------------------

```java
    Riddle riddle = new GameMatrixFactory().newRiddle(GameSchemas.SCHEMA_9X9);
    riddle.setAll(QuadraticArrays.parse("000000000", ...));

    Solver solver = new Solver(riddle);
    List<GameMatrix> solutions = solver.solve();
```

---------------------------------------

For valid riddles you'll find in magazines there is only one solution in the list.

There is also a [CLI client](sudoku-client) that demonstrates the usage of the library.

## Including it in your projects

Please note that the current version is experimental. It creates and solves riddles. The API will change.
The library could run into a runtime exception.

There are unit tests for many things, but the code is still young.

The recommended way of including the library into your project is using maven:

---------------------------------------

```xml
<dependency>
    <groupId>de.sfuhrm</groupId>
    <artifactId>sudoku</artifactId>
    <version>5.0.1</version>
</dependency>
```

---------------------------------------

## Algorithm

The design idea is to use the narrowest bottleneck of the Sudoku board to prune the backtracking
tree to the maximum and get the fastest results.

### Initialization

The algorithm first fills three blocks with numbers in random order to reduce the amount of backtracking.
After that, backtracking for the remaining fields starts.

### Backtracking

The field with the least number of possible number candidates on the board is searched. 
All candidates are tried until the first candidate leads to a valid backtracking tree path. Backtracking occurs in this loop.

#### Note on algorithm optimization

It's enough to restrict each backtracking recursion to one field. 
This means there are no *field*-dead-ends, only *value*-dead-ends
the algorithm runs in.

This can be proved because as long as the algorithms invariant to only
add valid values is true, no fields become dead-ends. The requirement
for field-dead-ends is that the surrounding fields have an illegal
setup which leads to a rule-violation for each and every value of the
field in question.

## Versions

The version numbers are chosen according to the
[semantic versioning](https://semver.org/) schema.
Especially major version changes come with breaking API
changes.

## Author

Written 2017-2022 by Stephan Fuhrmann. You can reach me via email to s (at) sfuhrm.de

## License

The project is licensed under [LGPL 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html).
