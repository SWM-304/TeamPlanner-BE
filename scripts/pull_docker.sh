#!/bin/bash

# 기능 : 도커이미지를 가져와서 태그설정 및 이미지 삭제
# Public 이미지 받기 trick: sudo docker logout public.ecr.aws

# 이미지 존재 여부를 확인하고 이미지가 존재하면 삭제
if sudo docker images | grep -q "129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver:latest"; then
    sudo docker rmi -f 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver:latest
fi

# 로그아웃 및 이미지 받아오기
sudo docker logout public.ecr.aws
sleep 0.5
sudo aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com
sleep 0.5
sudo docker pull 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver:latest
