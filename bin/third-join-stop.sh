#!/usr/bin/env bash
ps -ef | grep app-third-join-gateway | grep -v grep |awk '{print $2}' | xargs kill -9
