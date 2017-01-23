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
     -h (-help)                             : Show this command line help (default:
                                              true)
     -i (-input) PATH                       : Input file to read for solving
     -n (-count) N                          : The number of outputs to create
                                              (default: 1)
     -q (-quiet)                            : No output (default: false)
     -t (-time)                             : Show timing information (default:
                                              false)

One example session is given here which creates 3 full boards:

    java -jar target/sudoku-client-*-SNAPSHOT-jar-with-dependencies.jar  -e Full -n 3
    465287391
    932514687
    871396524
    549872136
    186435972
    327961845
    794158263
    653729418
    218643759
    
    426857319
    397214568
    158396427
    739682145
    682145973
    514739286
    273961854
    941578632
    865423791
    
    891453627
    762189345
    534276891
    126395784
    457812936
    983764512
    645928173
    219637458
    378541269


## Author

Written 2017 by Stephan Fuhrmann. You can reach me via email to s (at) sfuhrm.de

## License

The project is licensed under [LGPL 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html).
