ps -ef | grep app-portal | grep -v grep |awk '{print $2}' | xargs kill -9
