#!/usr/bin/env bash
./portal-stop.sh
cd ../app-portal
nohup mvn clean tomcat7:run 1>> /opt/yunhuni/logs/app-portal.out 2>> /opt/yunhuni/logs/app-portal.out &
cd ../bin
tail -f /opt/yunhuni/logs/app-portal.out
