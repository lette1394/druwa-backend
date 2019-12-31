#!/bin/bash
BUILD_PATH=$(ls /home/ubuntu/deploy/*.jar)
JAR_NAME=$(basename $BUILD_PATH)
echo "> build 파일명: $JAR_NAME"

echo "> build 파일 복사"
DEPLOY_PATH=/home/ubuntu/deploy/current/
cp $BUILD_PATH $DEPLOY_PATH

echo "> springboot-druwa-deploy-0.0.1-SNAPSHOT.jar 교체"
CP_JAR_PATH=$DEPLOY_PATH$JAR_NAME
APPLICATION_JAR_NAME=springboot-druwa-deploy-0.0.1-SNAPSHOT.jar
APPLICATION_JAR=$DEPLOY_PATH$APPLICATION_JAR_NAME

ln -Tfs $CP_JAR_PATH $APPLICATION_JAR

echo "> 현재 실행중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f $APPLICATION_JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $APPLICATION_JAR 배포"
unzip -o $APPLICATION_JAR
nohup java -Dspring.profiles.active=prod org.springframework.boot.loader.JarLauncher . > /dev/null 2> /dev/null < /dev/null &