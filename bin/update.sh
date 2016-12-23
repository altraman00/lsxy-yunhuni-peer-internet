#!/usr/bin/env bash
#该脚本用于测试环境从NEXUS上下载最新部署文件进行自动部署脚本，该脚本执行的前提是需要在nexus上已经存在最新的部署文件
#部署最新的WAR组件的命令样例：
# yunhuni_update.sh -A app-portal -D -P test -M /opt/tomcat_app_portal
#部署最新JAR组件的命令样例：
# yunhuni_update.sh -A app-area-server -P test -S
#该命令存放于CI  c02:/opt/yunhuni/yunhuni_update.sh
#
APP_NAME=""
ENV="test"
IS_TOMCAT_DEPLOY=false
IS_SPRINGBOOT=false
YUNHUNI_HOME=/opt/yunhuni
TOMCAT_HOME=/opt/apach-tomcat
BUILDNUM=""


source /etc/profile
ulimit -c unlimited

if [ -z $NEXUS_PATH ]; then
    NEXUS_PATH=http://10.44.185.24:8081/nexus/content/groups/public
fi

#是否需要在最后TAIL LOG
TAIL_LOG=false
DEPLOY_PACKAGE="jar"
JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseCMSCompactAtFullCollection -Xmn256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/yunhuni/crashed.heap"

while getopts "A:P:H:M:O:B:STLD" opt; do
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
      DEPLOY_PACKAGE="war"
      ;;
    S)
      IS_SPRINGBOOT=true;
      ;;
    L)
      TAIL_LOG=true;
      ;;
    H)
      YUNHUNI_HOME="$OPTARG";
      ;;
    M)
      TOMCAT_HOME="$OPTARG";
      ;;
    O)
      JAVA_OPTS="$JAVA_OPTS $OPTARG";
      ;;
    B)
      BUILDNUM="-$OPTARG";
      ;;

    \?)
      echo "Invalid option: -$OPTARG"
      ;;
  esac
done

echo "JAVA参数：$JAVA_OPTS"

if [ $ENV = "test" ]; then
    VERSION="TEST$BUILDNUM-SNAPSHOT"
elif [ $ENV = "development" ]; then
    VERSION="DEV$BUILDNUM-SNAPSHOT"
fi


echo "下载最新版本组件包"
echo "curl $NEXUS_PATH/com/lsxy/$APP_NAME/$VERSION/maven-metadata.xml"
LAST_VERSION=`curl $NEXUS_PATH/com/lsxy/$APP_NAME/$VERSION/maven-metadata.xml | grep value | awk -F '>' '{print $2}' | awk -F '<' '{print $1}' | head -n  1`
echo "最新版本号:$LAST_VERSION"
if [ -f $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.$DEPLOY_PACKAGE ]; then
    echo "备份原有文件到:$YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.bak"
    mv $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.$DEPLOY_PACKAGE $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.bak
fi

wget -x -O $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.$DEPLOY_PACKAGE  $NEXUS_PATH/com/lsxy/$APP_NAME/$VERSION/$APP_NAME-$LAST_VERSION.$DEPLOY_PACKAGE

if [ $? -ne 0 ];then
        echo "组件包下载失败"
	if [ -f $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.bak ]; then
            echo "恢复原有文件： $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.$DEPLOY_PACKAGE"
            mv $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.bak $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.$DEPLOY_PACKAGE
        fi
        exit 1
fi



if [ $IS_TOMCAT_DEPLOY = true ]; then
    echo "WAR包部署到TOMCAT : cp $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.$DEPLOY_PACKAGE $TOMCAT_HOME/webapps/ROOT.war"
    cp $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.$DEPLOY_PACKAGE $TOMCAT_HOME/webapps/ROOT.war
    echo "deploy success"
    exit 0;
fi

if [ $IS_SPRINGBOOT = true ]; then
    #先停止制定的APP服务
    echo "停止现有服务...."
    ps -ef | grep "$APP_NAME" | grep -v grep | grep -v update | grep -v tail |awk '{print $2}' | xargs kill -9

    echo "启动服务：java -jar $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar"
    nohup java $JAVA_OPTS -jar $YUNHUNI_HOME/$APP_NAME-$LAST_VERSION.jar >> /opt/yunhuni/logs/$APP_NAME.out 2>&1 &

    if [ $TAIL_LOG = true ]; then
        tail -f /opt/yunhuni/logs/$APP_NAME.out
    else
        sleep 10;
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
fi