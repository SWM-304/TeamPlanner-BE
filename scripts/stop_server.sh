#!/bin/bash
# 기능 : 도커컴포즈를 통해 구동중인 서비스를 중지함
basefolder="/home/ubuntu/cityweb"
cd ${basefolder}
sudo docker-compose down
echo "======== Server Down Completed! ========"