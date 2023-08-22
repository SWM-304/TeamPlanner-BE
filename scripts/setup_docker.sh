#!/bin/bash
#기능:프로그램확인후도커및도커콤포즈 설치진행
if [[ $(which docker) && $(which docker-compose) ]]; then
     echo "==== Docker already installed ===="
  else
curl -fsSL https://get.docker.com -o /tmp/get-docker.sh sudo sh /tmp/get-docker.sh
sleep 1
sudo apt-get install -y docker-compose
echo "======== Docker & Docker-Compose Installed! ========"
fi