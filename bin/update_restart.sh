#!/bin/bash
###
# 初次部署时需要强制安装
#     /opt/yunhuni-peer-internet/bin/update_restart.sh -A {app-name} -P development -S -I -L
#CI配置去掉 -I -L 参数
#    /opt/yunhuni-peer-internet/bin/update_restart.sh -A {app-name} -P development -S
#参数:
#-S 代表SPRINGBOOT应用  如果是-T 则是Tomcat应用  目前除了portal,其他模块均是SPRINGBOOT
#-I  代表需要强制安装,如果不带该参数,会根据git pull的结构来决定是否需要install
#-L 代表执行tail 输出日志信息 配置到CI的时候不要带这个参数
#-P 代表执行环境 可设置为local development production
#-A 代表应用模块名称,对应到程序主目录下的模块目录名
#
###
#update and restrt
#git reset --hard
#git pull
YUNHUNI_HOME="/opt/yunhuni-peer-internet"
APP_NAME=""
EXECUTE_HOME="/opt/yunhuni/"
TOMCAT_HOME=/opt/apach-tomcat
ENV_PROFILE="-Pdevelopment"
#tomcat还是springboot
IS_TOMCAT=false
IS_TOMCAT_DEPLOY=false
IS_SPRINGBOOT=false
#是否需要强制安装
FORCE_INSTALL=false
#是否需要强制清除安装
FORCE_CLEAN=true
#是否需要在最后TAIL LOG
TAIL_LOG=false

source /etc/profile
ulimit -c unlimited

JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseCMSCompactAtFullCollection -Xmn256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/yunhuni/crashed.heap"

while getopts "A:P:H:M:O:STILDC" opt; do
  case $opt in
    A)
      APP_NAME="$OPTARG"
      ;;
    P)
      ENV_PROFILE="-P$OPTARG"
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
    M)
      TOMCAT_HOME="$OPTARG";
      ;;
    I)
      FORCE_INSTALL=true;
      ;;
    O)
      JAVA_OPTS="$JAVA_OPTS $OPTARG";
      ;;
    C)
      FORCE_CLEAN=false;
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

echo "APPNAME=$APP_NAME";


if [ "$APP_NAME"x = ""x ]
then
   echo "usage：./update-restart.sh -A portal -P development"
   exit 1;
fi

#先停止制定的APP服务
echo "停止现有服务...."
ps -ef | grep "$APP_NAME" | grep -v update | grep -v grep| grep -v tail |awk '{print $2}' | xargs kill -9

export MAVEN_OPTS="$MAVEN_OPTS -Xms256m -Xmx512m"
echo "MAVEN 构建参数：$MAVEN_OPTS"

cd $YUNHUNI_HOME
git remote prune origin
#更新代码和安装模块组件
pull_ret=`git pull`
if [ "$pull_ret"x = "Already up-to-date."x ]; then
    #是否需要强制安装模块
    if [ $FORCE_INSTALL = true ]; then
        echo "安装模块代码"
        cd $YUNHUNI_HOME
        mvn clean
        mvn package -U $ENV_PROFILE -DskipTests=true -pl $APP_NAME -am
    else
        echo "已经是最新代码了 不用INSTALL了";
    fi
else
    #是否需要强制安装模块
    if [ $FORCE_CLEAN = true ]; then
        echo "清除安装模块代码"
        cd $YUNHUNI_HOME
        mvn clean
        mvn package -U $ENV_PROFILE -DskipTests=true -pl $APP_NAME -am
    else
        echo "已经是最新代码了 不用CLEAN INSTALL了";
    fi
fi
if [ $? -ne 0 ];then
        echo "mvn compile failed"
        exit 1
fi


#启动服务脚本
cd $YUNHUNI_HOME/$APP_NAME
echo "判断是否是TOMCAT:$IS_TOMCAT"
if [ $IS_TOMCAT = true ]; then
  echo "starting  tomcat ..."
  nohup mvn -U $ENV_PROFILE clean tomcat7:run 1>> /opt/yunhuni/logs/$APP_NAME.out 2>> /opt/yunhuni/logs/$APP_NAME.out &
  #mvn -U $ENV_PROFILE clean tomcat7:run
elif [ $IS_SPRINGBOOT = true ]; then
  echo "starting springboot application...."
#  nohup mvn -U $ENV_PROFILE spring-boot:run 1>> /opt/yunhuni/logs/$APP_NAME.out 2>> /opt/yunhuni/logs/$APP_NAME.out &
  JAR_FILE=`find ./ -name "app-*.jar"`
  echo "execute jar file :$JAR_FILE"
  echo "启动服务：java $JAVA_OPTS -jar $JAR_FILE"
  nohup java $JAVA_OPTS -jar $JAR_FILE >> /opt/yunhuni/logs/$APP_NAME.out 2>&1 &
elif [ $IS_TOMCAT_DEPLOY = true ]; then
  echo "deploy war to tomcat...."
  WAR_FILE=`find ./ -name "app-*.war"`
  echo "cp $WAR_FILE $TOMCAT_HOME/webapps/ROOT.war"
  cp $WAR_FILE $TOMCAT_HOME/webapps/ROOT.war
fi

sleep 10;
PROCESS_NUM=`ps -ef | grep $APP_NAME | grep "java" | grep -v "grep" | wc -l`

if [ $IS_TOMCAT_DEPLOY = false ]; then
    if [ $PROCESS_NUM -eq 1 ];
        then
            echo "start sucess"
        else
            echo "start fail"
            exit 1
    fi
fi
echo "OK";

if [ $TAIL_LOG = true ]; then
    tail -f /opt/yunhuni/logs/$APP_NAME.out
fi
