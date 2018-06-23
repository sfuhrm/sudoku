# How to deploy on Sonatype

This is a short summary on how to deploy the artifact to
the Sonatype Nexus Repository.

## Stage deploy

The local artifact will need to have a -SNAPSHOT extension in the
version.

Issue the command

    mvn clean deploy

NOTE: There will be multiple requests for the PGP passphrase.

## Production deploy

The local artifact will need to have NO -SNAPSHOT extension in the
version.

For 1.0.3 being the current version and 1.0.4 the next, issue the commands

    mvn versions:set -DnewVersion=1.0.3
    mvn clean deploy
    mvn versions:set -DnewVersion=1.0.4-SNAPSHOT

NOTE: There will be multiple requests for the PGP passphrase.

## Remote administration

Administer the projects and artefacts on https://oss.sonatype.org/
