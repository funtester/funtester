#!/bin/bash
mvn install:install-file -DgroupId=dk.brics -DartifactId=automaton -Dversion=1.11 -Dpackaging=jar -Dfile=automaton-1.11.jar -DgeneratePom=true