#!/usr/bin/env bash
cd ../app-third-join-gateway
nohup mvn spring-boot:run 1>> /opt/yunhuni/logs/third-join-gateway.out 2>> /opt/yunhuni/logs/third-join-gateway.out &
cd ../bin
if [ $# -gt 0 ]
    then
        if [ $1 == "tail" ]
        then
         tail -f /opt/yunhuni/logs/third-join-gateway.out
    fi
fi




