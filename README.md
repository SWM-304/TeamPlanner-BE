# 1. 프로젝트 개요

### 1-1 프로젝트 소개
<img width="1200" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/4150268e-842a-4c93-bd21-b4640b4b5c38">



### 1-2 프로젝트 기술스택
<img width="1512" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/43606521/bb1c12e4-ed99-43f1-b8eb-b7d15ad24bea">

# 2. Architecture
## 2-1. AWS Architecture
### V1

<img width="895" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/86bc1775-1383-45ea-a4e0-951cffb4eeec">


### V2

<img width="1035" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/e4912491-7741-49f7-b451-fda1aa5e64f5">

### V3

<img width="872" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/06637ce3-cd49-4fb7-a579-07a28b576285">

## 2-2. CI/CD Acritecture

### V1

<img width="620" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/5511a01a-0f14-47b6-9991-cc6a75fe5822">


### V2

<img width="684" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/4cd64a7b-071c-4405-a974-f746354af484">

### V3

![image](https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/848b5418-df2a-488c-a719-b089f530a243)


## 2-3. ChatService Acritecture

### V1

<img width="824" alt="image" src="https://github.com/SWM-304/TeamPlanner-FE/assets/79193811/b412cb4a-bd9e-4376-8d82-1b7c3891a51d">

# ISSUES

### Notification 
SSE를 사용할 때 단일서버일 경우에는 문제가 없지만 서버를 Scale-out 할 때 문제가 발생한다.<br>
사용자 정보를 SseEmitter가 서버 메모리에 저장되어 있기 때문이다. <br> 그래서 알림을 전송할 때 publish 및 dynamodb에 저장하고 redis topic에 담아 Scale-out할 때도 모든 서버에서 subscribe할 수 있도록 처리하였다.

<img width="825" alt="image" src="https://github.com/SWM-304/TeamPlanner-BE/assets/79193811/bbf484c0-2865-49ca-8add-2ec23a404222">

