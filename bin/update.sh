#!/usr/bin/env bash

APP_NAME=""
ENV="test"
IS_TOMCAT_DEPLOY=false
IS_SPRINGBOOT=false
YUNHUNI_HOME=/opt/yunhuni
NEXUS_PATH=http://10.44.185.24:8081/nexus/content/groups/public
#是否需要在最后TAIL LOG
TAIL_LOG=false

while getopts "A:P:H:STLD" opt; do
  case $opt in
    A)
      APP_NAME="$OPTARG"
      ;;
    P)
      ENV="$OPTARG"
      ;;
    T)
      IS_TOMCAT=true;
      ;;
    D)
      IS_TOMCAT_DEPLOY=true;
      ;;
    S)
      IS_SPRINGBOOT=true;
      ;;
    L)
      TAIL_LOG=true;
      ;;
    H)
      YUNHUNI_HOME="$OPTARG"
      ;;
    \?)
      echo "Invalid option: -$OPTARG"
      ;;
  esac
done

if [ $ENV = "test" ]; then
    VERSION=TEST-SNAPSHOT
elif [ $ENV = "development" ]; then
    VERSION=DEV-SNAPSHOT
fi

echo "下载最新版本组件包"

LAST_VERSION=`curl $NEXUS_PATH/com/lsxy/$APP_NAME/$VERSION/maven-metadata.xml | grep value | awk -F '>' '{print $2}' | awk -F '<' '{print $1}' | head -n  1`
echo "最新版本号:$LAST_VERSION"
if [ -f $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar ]; then
    echo "备份原有文件到:$YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.bak"
    mv $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.bak
fi

wget -x -O $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar  $NEXUS_PATH/com/lsxy/app-api-gateway/TEST-SNAPSHOT/$APP_NAME-$LAST_VERSION.jar

if [ $? -ne 0 ];then
        echo "组件包下载失败"
	if [ -f $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.bak ]; then
            echo "恢复原有文件： $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar"
            mv $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.bak $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar
        fi
        exit 1
fi

export MAVEN_OPTS="-Xms256m -Xmx512m"
echo "MAVEN 构建参数：$MAVEN_OPTS"
#先停止制定的APP服务
echo "停止现有服务...."
ps -ef | grep "$APP_NAME" | grep -v grep |awk '{print $2}' | xargs kill -9

echo "启动服务：java -jar $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar"
nohup java -jar $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar >> /opt/yunhuni/logs/$APP_NAME.out 2>&1 &

if [ $TAIL_LOG = true ]; then
    tail -f /opt/yunhuni/logs/$APP_NAME.out
else
    sleep 20;
    PROCESS_NUM=`ps -ef | grep $APP_NAME | grep "java" | grep -v "grep" | wc -l`
    if [ $IS_TOMCAT_DEPLOY = false ]; then
        if [ $PROCESS_NUM -eq 1 ];
            then
                echo "start sucess"
		ps -ef | grep $APP_NAME | grep "java" | awk '{print $2}'
            else
                echo "start fail"
                exit 1
        fi
    fi
fi

exit 0;