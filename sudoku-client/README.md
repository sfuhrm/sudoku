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

    java -jar target/sudoku-client-0.1.3-SNAPSHOT-jar-with-dependencies.jar -e Full  -f MarkDownTable -n 3
    |3|9|6|1|7|2|5|8|4|
    |1|2|5|4|6|8|3|9|7|
    |8|7|4|3|5|9|1|6|2|
    |4|8|7|6|9|3|2|5|1|
    |6|3|2|5|4|1|8|7|9|
    |5|1|9|8|2|7|6|4|3|
    |2|4|1|7|8|6|9|3|5|
    |9|5|8|2|3|4|7|1|6|
    |7|6|3|9|1|5|4|2|8|

    |3|8|4|2|7|5|6|9|1|
    |9|5|1|4|3|6|2|7|8|
    |2|7|6|8|9|1|5|3|4|
    |4|2|9|1|6|3|7|8|5|
    |5|6|8|7|2|9|1|4|3|
    |1|3|7|5|8|4|9|6|2|
    |7|4|2|6|5|8|3|1|9|
    |8|9|5|3|1|7|4|2|6|
    |6|1|3|9|4|2|8|5|7|
    
    |7|5|1|4|3|8|2|9|6|
    |6|8|4|9|1|2|3|5|7|
    |2|9|3|7|5|6|1|8|4|
    |9|2|5|8|4|7|6|1|3|
    |1|4|8|6|2|3|9|7|5|
    |3|6|7|1|9|5|4|2|8|
    |5|7|2|3|6|1|8|4|9|
    |4|1|6|5|8|9|7|3|2|
    |8|3|9|2|7|4|5|6|1|

## Author

Written 2017 by Stephan Fuhrmann. You can reach me via email to s (at) sfuhrm.de

## License

The project is licensed under [LGPL 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html).
