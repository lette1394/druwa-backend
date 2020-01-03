#!/bin/bash
BUILD_PATH=$(ls /home/ubuntu/deploy/*.jar)
JAR_NAME=$(basename "$BUILD_PATH")
echo "> build 파일명: $JAR_NAME"

DEPLOY_PATH=/home/ubuntu/deploy/current/
echo "> 기존 파일 백업"
BACKUP_PATH=/home/ubuntu/deploy/$(date +%y%m%d_%H%M%S)
mkdir "$BACKUP_PATH"
cp $DEPLOY_PATH/*.jar "$BACKUP_PATH"

echo "> build 파일 이동"
cp $BUILD_PATH $DEPLOY_PATH

echo "> springboot-druwa-deploy-0.0.1-SNAPSHOT.jar 교체"
CP_JAR_PATH=$DEPLOY_PATH$JAR_NAME
APPLICATION_JAR_NAME=springboot-druwa-deploy-0.0.1-SNAPSHOT.jar
APPLICATION_JAR=$DEPLOY_PATH$APPLICATION_JAR_NAME

echo "> 현재 실행중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f 'org.springframework.boot.loader.JarLauncher')

if [ -z "$CURRENT_PID" ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "> $APPLICATION_JAR 배포"
unzip -o $APPLICATION_JAR -d $DEPLOY_PATH

echo "> application.yaml prod 모드 교체"
cp '/home/ubuntu/deploy/overwrite-files/application.yaml' '/home/ubuntu/deploy/current/BOOT-INF/classes/'

cd $DEPLOY_PATH
nohup java -Dspring.profiles.active=prod org.springframework.boot.loader.JarLauncher . > /dev/null 2> /dev/null < /dev/null &