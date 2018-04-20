#! /bin/bash
#
# Copies the Javadoc to the web
#
# (c) 2017-2018 Stephan Fuhrmann

ROOT=$(cd $(dirname $0)/..; pwd)
REMOTE_TARGET=api/sudoku
REMOTE_USER=sfuhrm

SRCDIR="${ROOT}/sudoku/target/apidocs/"
if [ ! -e ${SRCDIR} ]; then
  echo "Need API docs in ${SRCDIR}"
  exit
fi

ssh ${REMOTE_USER} rm -fr ${REMOTE_TARGET}
scp -r $SRCDIR ${REMOTE_USER}:${REMOTE_TARGET}
