#!/bin/bash
# 기능 : 도커컴포즈로 도커서비스 구동 및 로그파일 생성
homefolder="/home/ubuntu"
basefolder="${homefolder}/cityweb"
NOW="$(date +'%Y%m%d')_$(date +'%H%M%S')"
LOGFILE=${homefolder}/start_$NOW.log
cd ${basefolder}
sudo docker-compose up -d
cd ${homefolder}
sudo ls -al > $LOGFILE
echo "======== Server Started! ========"