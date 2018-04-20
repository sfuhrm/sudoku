#! /bin/bash
#
# Sets the version of the current software at multiple places.
# Call it like this:
# set-version.sh 1.0.0-SNAPSHOT
#
# (c) 2017-2018 Stephan Fuhrmann

ROOT=$(cd $(dirname $0)/..; pwd)
OLDPWD=$PWD
NEWVERSION=$1

if [ "x${NEWVERSION}" = "x" ]; then
	echo "Please give a version as a parameter, for example 0.1.4-SNAPSHOT"
	exit 10
fi

if [ "${NEWVERSION##*-}" = "SNAPSHOT" ]; then
  ISSNAPSHOT=1
else
  ISSNAPSHOT=0
fi

cd ${ROOT}

echo "- pom.xml"
mvn versions:set -DnewVersion=${NEWVERSION} || exit
rm -f pom.xml.versionsBackup

echo "- README.md"
sed < README.md > README.md.new -e"s#<version>.*</version>#<version>${NEWVERSION}</version>#g" || exit
if [ $ISSNAPSHOT = 0 ]; then
  mv README.md.new README.md || exit
fi

cd $OLDPWD
