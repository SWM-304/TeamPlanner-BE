#!/bin/bash
# 기능 : 도커이미지를 가져와서 태그설정
# Public 이미지 받기 trick :sudo docker logout public.ecr.aws
sudo docker logout public.ecr.aws
sleep 0.5
sudo aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com
sleep 0.5
sudo docker pull 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver:latest
sleep 0.5
sudo docker tag 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver:latest pub-web:city-2.0
sleep 0.5
sudo docker rmi -f 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver:latest