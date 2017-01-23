# Sudoku Example Client

This is the description of a command line client that shows the functions of the Sudoku library.

## Building it

For building the application you need Apache Maven.
Use the following command line:

    $ mvn clean compile assembly:single
    
## Usage

The command line options for the client are as follows:

     -e (-exec) [Full | Riddle | Both |     : The operation to perform (default:
     Solve]                                   Full)
     -f (-format) [PlainText |              : The output format to use (default:
     MarkDownTable]                           MarkDownTable)
     -h (-help)                             : Show this command line help (default:
                                              true)
     -i (-input) PATH                       : Input file to read for solving
     -n (-count) N                          : The number of outputs to create
                                              (default: 1)
     -q (-quiet)                            : No output (default: false)
     -t (-time)                             : Show timing information (default:
                                              false)


One example session is given here which creates 3 full boards:

    java -jar target/sudoku-client-0.1.3-SNAPSHOT-jar-with-dependencies.jar -e Riddle  -f MarkDownTable -n 3
    |  |  |  |  |  |  |  |  |  |
    |---|---|---|---|---|---|---|---|---|
    | . | 7 | . | . | . | . | . | . | 1 |
    | . | 2 | . | 1 | 7 | . | . | 5 | . |
    | . | . | . | 5 | 9 | . | . | . | . |
    | . | 9 | . | . | 6 | . | 8 | . | 3 |
    | 3 | 4 | . | . | . | 2 | . | . | . |
    | . | 1 | . | . | . | . | 7 | 4 | . |
    | . | . | . | . | . | . | . | . | . |
    | . | . | . | . | 5 | 3 | 6 | . | . |
    | . | . | 6 | . | . | . | 5 | . | . |
    |  |  |  |  |  |  |  |  |  |
    
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
    
    |  |  |  |  |  |  |  |  |  |
    |---|---|---|---|---|---|---|---|---|
    | . | . | . | . | . | 2 | 8 | . | . |
    | 7 | 1 | . | . | 5 | . | . | . | 6 |
    | . | 8 | . | . | 9 | . | . | . | 5 |
    | . | 5 | . | 2 | . | . | 7 | . | . |
    | . | . | . | 5 | . | . | 6 | . | . |
    | . | . | . | 6 | 8 | 3 | . | . | . |
    | 8 | . | . | . | 7 | . | . | 3 | . |
    | 5 | . | 3 | 4 | . | . | . | . | 1 |
    | 4 | . | . | . | . | . | 2 | . | . |
    |  |  |  |  |  |  |  |  |  |


## Author

Written 2017 by Stephan Fuhrmann. You can reach me via email to s (at) sfuhrm.de

## License

The project is licensed under [LGPL 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html).
