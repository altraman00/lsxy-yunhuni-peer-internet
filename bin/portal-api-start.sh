#!/usr/bin/env bash
cd ../app-portal-api
nohup mvn spring-boot:run 1>> /dev/null 2>> /dev/null &
cd ../bin