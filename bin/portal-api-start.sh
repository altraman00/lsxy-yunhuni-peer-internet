#!/usr/bin/env bash
cd ../app-portal-api
nohup mvn spring-boot:run 1>> /opt/yunhuni/logs/app-portal-api.out 2>> /opt/yunhuni/logs/app-portal-api.out &
cd ../bin

if [ $# -gt 0 ]
    then
        if [ $1 == "tail" ]
        then
         tail -f /opt/yunhuni/logs/app-portal-api.out
    fi
fi



