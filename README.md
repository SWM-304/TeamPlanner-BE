# 1. 프로젝트 개요

### 1-1 프로젝트 소개
<img width="917" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/e7386190-c03b-4b27-a7f8-e0ce1ffd3e3f">




### 1-2 프로젝트 기술스택
![image](https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/9d94a132-134d-4717-b6fd-61f2f15eee6b)


# 2. Architecture
## 2-1. AWS Architecture
<img width="700" alt="image" src="https://github.com/minwoo1999/TeamPlanner-BE/assets/79193811/33a9c4d8-5cba-4e3a-b6ce-9e113d7fea81">



## 2-2. CI/CD Acritecture

<img width="900" alt="image" src="https://github.com/minwoo1999/TeamPlanner-BE/assets/79193811/1d6d6e91-988a-43e9-a93b-97da81016c8c">



## 2-3.ChatService Acritecture

<img width="624" alt="image" src="https://github.com/SWM-304/TeamPlanner-FE/assets/79193811/b412cb4a-bd9e-4376-8d82-1b7c3891a51d">


## 2-4.Notification 
SSE를 사용할 때 단일서버일 경우에는 문제가 없지만 서버를 Scale-out 할 때 문제가 발생한다.<br>
사용자 정보를 SseEmitter가 서버 메모리에 저장되어 있기 때문이다. <br> 그래서 알림을 전송할 때 publish 및 dynamodb에 저장하고 redis topic에 담아 Scale-out할 때도 모든 서버에서 subscribe할 수 있도록 처리하였습니다.

<img width="814" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/7e9cd8e2-970e-4891-8d9b-4851d9f1f0ae">

# 3. API서버개발 및 API문서화/테스트
백엔드 API 서버 개발은 Java와 Spring 기반으로 진행하였으며, OpenAPI Spec에 맞는 RESTful한 API를 개발하였습니다. API 서버 개발은 Swagger와 Postman을 통해 문서화 및 테스트를 진행하였습니다.
<img width="1511" alt="image" src="https://github.com/minwoo1999/TeamPlanner-BE/assets/79193811/c6869976-a116-4328-8b1d-6c5c8ea67b42">

<img width="860" alt="image" src="https://github.com/minwoo1999/TeamPlanner-BE/assets/79193811/885aee5b-aa1d-421c-989b-4edd12122351">

Postman을 이용하여, Development 환경과 Deployment 환경 모두에서 API 테스팅을 할 수 있도록 자동화 구성하였습니다.

ArgoCD Application Detail Tree
<img width="1507" alt="image" src="https://github.com/minwoo1999/TeamPlanner-BE/assets/79193811/4e0b02c1-9b24-4b56-94e1-a1c8ead79ad3">


# 4. 수행 방법 및 프로젝트 관리
## 4-1. 개발 프로세스
<img width="997" alt="image" src="https://github.com/minwoo1999/TeamPlanner-BE/assets/79193811/5db52c47-c183-4a32-90a9-e015f0f13361">


