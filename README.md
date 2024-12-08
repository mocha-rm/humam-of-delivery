# 배달의 인간

## API 명세서
#### 1. 회원 CRUD, 로그인, 로그아웃

##### 회원 가입 
- **URL**: `/api/members/signup`
- **Method**: `POST`
- **상태코드**: '201 CREATED, 400 BAD REQUEST'
- 대소문자 + 영문 + 숫자 + 특수문자  최소 1글자씩 포함,
비밀번호는 최소 8글자 이상.
상태 :
OWNER :  가게사장,
USER : 일반유저
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |name|String|회원 이름|O|
  |email|String|이메일|O|
  |password|String|비밀번호|O|
  |role|String|회원 구분(사장,일반유저)|O|
  

  ```json
  {
    "name": "김사장",
    "email": "owner@owner.com",
    "password": "!Qwer1234",
    "role": "OWNER"
  }
- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |name|String|이름 확인|
  |email|String|이메일 확인|
  |role|String|회원 구분 확인|
  |createdAt|LocalDateTime|회원 생성 날짜 저장|
  |modifiedAt|LocalDateTime|회원 수정 날짜 저장|
  
  ```json
  {
    "name": "김사장",
    "email": "odwner@owner.com",
    "role": "OWNER",
    "createdAt": "2024-12-08T16:29:16.433709",
    "modifiedAt": "2024-12-08T16:29:16.433709"
  }

##### 로그인
- **URL**: `/api/members/login`
- **Method**: `POST`
- **상태코드**: 201 CREATED, 200 OK, 400 BAD REQUEST, 404 NOT FOUND
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |email|String|이메일|O|
  |password|String|비밀번호|O|
  

  ```json
  {
    "email": "owner@owner.com",
    "password": "!Qwer1234",
  }
- **응답**

  
  "로그인이 완료되었습니다"

##### 로그아웃
- **URL**: `/api/members/logout`
- **Method**: `POST`
- **상태코드**: 200 OK, 401 UNAUTHORIZED, 403 FORBIDDEN
- **요청**

- **응답**

  
  "로그아웃 되었습니다"

  
##### 프로필 조회
- **URL**: `/api/{user_id}`
- **Method**: `GET`
- **상태코드**: 200 OK, 401 UNAUTHORIZED, 403 FORBIDDEN, 404 NOT FOUND

- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |name|String|이름|
  |email|String|이메일|
  |role|String|회원 구분|
  |createDate|LocalDate|회원 생성 날짜 확인|
  |upadteDate|LocalDate|회원 수정 날짜 확인|
  |storeNum|int|가게 개수|
  |stores|List|스토어 정보들|
  ```json
  {
    "name": "김사장",
    "email": "owner@owner.com",
    "role": "OWNER",
    "createdAt": "2024-12-06T16:37:31.590923",
    "modifiedAt": "2024-12-06T16:37:31.590923",
    "storeNum": 3,
    "stores": [
        {
            "storeId": 1,
            "storeName": "맛있는가게",
            "storeStatus": "OPEN"
        },
        {
            "storeId": 2,
            "storeName": "맛있는가게",
            "storeStatus": "OPEN"
        },
        {
            "storeId": 3,
            "storeName": "맛있는가게",
            "storeStatus": "OPEN"
        }
    ]
  }


##### 프로필 수정
- **URL**: `/api/members/{user_id}`
- **Method**: `PATCH`
- **상태코드**: 200 OK, 400 BAD REQUEST, 401 UNAUTHORIZED, 403 FORBIDDEN, 404 NOT FOUND
- **요청**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |name|String|회원 이름|
  |email|String|이메일|
  |password|String|기존 비밀번호|
  |newPassword|String|새로운 비밀번호|
  ```json
  {
    "name": "배달왕",
    "email": "bbb@bbb.com"
    "password": "Q1wer34!er34",
    "newPassword": "!@34dsfQdfW"
  }
- **응답**

  
  "회원 정보가 수정되었습니다"

  
##### 회원 탈퇴
- **URL**: `/api/members/{user_id}/deactivate`
- **Method**: `PATCH`
- **상태코드**: 200 OK, 400 BAD REQUEST, 401 UNAUTHORIZED, 403 FORBIDDEN
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |password|String|비밀번호|O|
  

  ```json
  {
    "password": "!Qwer1234",
  }
- **응답**

  
  "회원탈퇴가 완료되었습니다"
