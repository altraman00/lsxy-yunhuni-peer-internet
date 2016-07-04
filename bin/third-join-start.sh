#!/usr/bin/env bash
cd ../app-third-join-gateway
nohup mvn spring-boot:run 1>> /opt/yunhuni/logs/third-join-gateway.out 2>> /opt/yunhuni/logs/third-join-gateway.out &
cd ../bin
tail -f /opt/yunhuni/logs/third-join-gateway.out
