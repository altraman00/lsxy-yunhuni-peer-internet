#!/usr/bin/env bash
mvn versions:set -DnewVersion=TEST-SNAPSHOT
mvn clean compile deploy -Ptest -DskipTests=true