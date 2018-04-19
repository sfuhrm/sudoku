#! /bin/bash
#
# Copies the Javadoc to the web
#
# (c) 2017-2018 Stephan Fuhrmann

NEWVERSION=$1

SRCDIR="sudoku/target/apidocs/"
if [ ! -e ${SRCDIR} ]; then
  echo "Need API docs in ${SRCDIR}"
  exit
fi

ssh p45319591@sfuhrm rm -fr api/sudoku
scp -r $SRCDIR p45319591@sfuhrm:api/sudoku

