#!/usr/bin/env bash
./app-portal-stop.sh
echo "app-portal stoped"
git pull
echo "start app-portal"
./app-portal-start.sh
