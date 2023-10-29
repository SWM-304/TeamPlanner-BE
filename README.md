# 1. 프로젝트 개요

### 1-1 프로젝트 소개
<img width="917" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/e7386190-c03b-4b27-a7f8-e0ce1ffd3e3f">




### 1-2 프로젝트 기술스택
![image](https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/9d94a132-134d-4717-b6fd-61f2f15eee6b)


# 2. Architecture
## 2-1. AWS Architecture
### V1

<img width="894" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/678d8e71-be83-40e1-b0b8-2dbdfbfb2798">


### V2

<img width="1042" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/5baa9553-079f-448e-a6c1-2aa35fe19e23">

### V3

<img width="1161" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/264bf88f-072d-4d5b-9fc0-5c5caabffeb0">

### V4

<img width="916" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/dad460df-1cdf-4730-96e5-70bf5b97525d">


## 2-2. CI/CD Acritecture

<img width="587" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/005ef2a2-c6d3-4375-b06e-f510fd629412">



# 3.ChatService Acritecture

<img width="824" alt="image" src="https://github.com/SWM-304/TeamPlanner-FE/assets/79193811/b412cb4a-bd9e-4376-8d82-1b7c3891a51d">


# 4.Notification 
SSE를 사용할 때 단일서버일 경우에는 문제가 없지만 서버를 Scale-out 할 때 문제가 발생한다.<br>
사용자 정보를 SseEmitter가 서버 메모리에 저장되어 있기 때문이다. <br> 그래서 알림을 전송할 때 publish 및 dynamodb에 저장하고 redis topic에 담아 Scale-out할 때도 모든 서버에서 subscribe할 수 있도록 처리하였다.

<img width="814" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/7e9cd8e2-970e-4891-8d9b-4851d9f1f0ae">


