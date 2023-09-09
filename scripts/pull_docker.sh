#!/bin/bash
# 기능 : 도커 이미지를 가져와서 태그 설정
# Public 이미지 받기 trick: sudo docker logout public.ecr.aws

# Docker 로그인 토큰을 환경 변수에 저장
DOCKER_LOGIN_TOKEN=$(aws ecr get-login-password --region ap-northeast-2)

# Docker 로그인
echo "$DOCKER_LOGIN_TOKEN" | docker login --username AWS --password-stdin 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com

# 이미지 이름
image_name="129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver:latest"

# 이미지가 존재하는지 확인
if docker images | awk '{print $1}' | grep -q "^$image_name\$"; then
    # 이미지가 존재하는 경우 삭제
    sudo docker rmi -f "$image_name"
    echo "Docker 이미지 '$image_name'를 삭제했습니다."
else
    echo "Docker 이미지 '$image_name'는 이미 존재하지 않습니다."
fi

# Docker 이미지 가져오기
docker pull "$image_name"
