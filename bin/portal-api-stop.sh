#!/usr/bin/env bash
ps -ef | grep app-portal-api | grep -v grep |awk '{print $2}' | xargs kill -9
