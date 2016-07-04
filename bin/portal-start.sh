#!/bin/bash
cd /opt/yunhuni-peer-internet/bin
./portal-stop.sh
cd ../app-portal
nohup mvn clean tomcat7:run 1>> /opt/yunhuni/logs/app-portal.out 2>> /opt/yunhuni/logs/app-portal.out &
cd ../bin

if [ $# -gt 0 ]
    then
        if [ $1 == "tail" ]
        then
         tail -f /opt/yunhuni/logs/app-portal.out
    fi
fi

