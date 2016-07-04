#!/usr/bin/env bash
cd ../app-portal-api
nohup mvn spring-boot:run 1>> /opt/yunhuni/logs/app-portal-api.out 2>> /opt/yunhuni/logs/app-portal-api.out &
cd ../bin
tail -f /opt/yunhuni/logs/app-portal-api.out
