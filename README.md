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

#### 2. 가게 CRUD

##### 가게 생성
- **URL**: `/api/owner/stores`
- **Method**: `POST`
- **상태코드**: '201 CREATED, 401 UNAUTHORIZED, 403 FORBIDDEN'
- OWNER 사용자만 생성 가능
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |name|String|가게 이름|O|
  |status|StoreStatus|영업 상태|O|
  |minCost|Integer|최저 주문 금액|O|
  |openAt|LocalTime|오픈 시간|O|
  |closeAt|LocalTime|영업 종료 시간|O|
  

  ```json
  {
    "name": "맛있는 가게",
    "status": "OPEN",
    "minCost": "14000",
    "openAt": "10:00",
    "closeAt": "21:00"
  }
- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |id|Long|가게 아이디|
  |name|String|가게 이름|
  |status|StoreStatus|영업 상태|
  |minCost|Integer|최저 주문 금액|
  |openAt|LocalTime|오픈 시간|
  |closeAt|LocalTime|영업 종료 시간|
  |createdAt|LocalDateTime|회원 생성 날짜 저장|
  |modifiedAt|LocalDateTime|회원 수정 날짜 저장|
  
  ```json
  {
    "id": 3,
    "name": "맛있는가게",
    "status": "OPEN",
    "minCost": 14000,
    "openAt": "10:00:00",
    "closeAt": "21:00:00",
    "createdAt": "2024-12-08T16:37:25.317133",
    "modifiedAt": "2024-12-08T16:37:25.317133"
  }


##### 가게 다건 조회
- **URL**: `/api/stores?name=””`
- **Method**: `GET`
- **상태코드**: 200 OK

- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |id|Long|가게 아이디|
  |name|String|가게 이름|
  |status|StoreStatus|영업 상태|
  |minCost|Integer|최저 주문 금액|
  |openAt|LocalTime|오픈 시간|
  |closeAt|LocalTime|영업 종료 시간|
  |createdAt|LocalDateTime|회원 생성 날짜 저장|
  |modifiedAt|LocalDateTime|회원 수정 날짜 저장|
  ```json
  {
    [
        {
            "id": 1,
            "name": "맛있는가게",
            "status": "OPEN",
            "minCost": 14000,
            "openAt": "10:00:00",
            "closeAt": "21:00:00",
            "createdAt": "2024-12-08T16:37:25.317133",
            "modifiedAt": "2024-12-08T16:37:25.317133"
        },
        {
            "id": 2,
            "name": "맛있는가게",
            "status": "OPEN",
            "minCost": 14000,
            "openAt": "10:00:00",
            "closeAt": "21:00:00",
            "createdAt": "2024-12-08T16:37:25.317133",
            "modifiedAt": "2024-12-08T16:37:25.317133"
        }
    ]
  }


##### 가게 수정
- **URL**: `/api/stores/{id}`
- **Method**: `PATCH`
- **상태코드**: 200 OK, 400 BAD REQUEST, 401 UNAUTHORIZED, 404 NOT FOUND
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |status|StoreStatus|영업 상태|O|

  ```json
  {
    "status": "SHUT",
  }
- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |id|Long|가게 아이디|
  |name|String|가게 이름|
  |status|StoreStatus|영업 상태|
  |minCost|Integer|최저 주문 금액|
  |openAt|LocalTime|오픈 시간|
  |closeAt|LocalTime|영업 종료 시간|
  |createdAt|LocalDateTime|회원 생성 날짜 저장|
  |modifiedAt|LocalDateTime|회원 수정 날짜 저장|
  ```json
  {
    "id": 1,
    "name": "맛있는가게",
    "status": "SHUT",
    "minCost": 14000,
    "openAt": "10:00:00",
    "closeAt": "21:00:00",
    "createdAt": "2024-12-08T16:37:25.317133",
    "modifiedAt": "2024-12-08T16:37:25.317133"
  }

#### 3. 주문 CRUD

##### 주문 생성
- **URL**: `/api/user/orders`
- **Method**: `POST`
- **상태코드**: '201 CREATED, 400 BAD REQUEST, 401 UNAUTHORIZED
- USER 사용자만 생성 가능
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |storeId|Long|가게 아이디|O|
  |menuName|String|메뉴 이름|O|
 
  

  ```json
  {
    "storeId": 1,
    "menuName": "부대전골",
  }
- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |id|Long|주문 아이디|
  |storeId|Long|가게 아이디|
  |userId|Long|주문자 아이디|
  |menuName|String|메뉴 이름|
  |status|MenuStatus|배달 상태|
  |createdAt|LocalDateTime|주문 생성 날짜 저장|
  |modifiedAt|LocalDateTime|주문 수정 날짜 저장|
  
  ```json
  {
    "id": 2,
    "storeId": 1,
    "userId": 3,
    "menuName": "부대전골",
    "status": "ORDER_COMPLETED",
    "createdAt": "2024-12-06T19:38:48.874986",
    "modifiedAt": "2024-12-06T19:38:48.874986"
  }
##### 주문 조회
- **URL**: `/api/orders`
- **Method**: `GET`
- **상태코드**: 200 OK

- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |id|Long|주문 아이디|
  |storeId|Long|가게 아이디|
  |userId|Long|주문자 아이디|
  |menuName|String|메뉴 이름|
  |status|OrderStatus|배달 상태|
  |createdAt|LocalDateTime|주문 생성 날짜 저장|
  |modifiedAt|LocalDateTime|주문 수정 날짜 저장|
  ```json
  [
    {
        "id": 1,
        "storeId": 1,
        "userId": 3,
        "menuName": "부대전골",
        "status": "DELIVERY_COMPLETED",
        "createdAt": "2024-12-06T16:40:45.118041",
        "modifiedAt": "2024-12-06T16:41:04.519476"
    },
    {
        "id": 2,
        "storeId": 1,
        "userId": 3,
        "menuName": "부대전골",
        "status": "ORDER_COMPLETED",
        "createdAt": "2024-12-06T19:38:48.874986",
        "modifiedAt": "2024-12-06T19:38:48.874986"
    }
  ]
##### 주문 수정
- **URL**: `/api/owner/orders/{id}`
- **Method**: `PATCH`
- **상태코드**: '200 OK, 400 BAD REQUEST, 401 UNAUTHORIZED, 404 NOT FOUND
- USER 사용자만 생성 가능
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |orderStatus|OrderStatus|가게 아이디|O|

 
  

  ```json
  {
    "orderStatus": "COOKING"
  }

- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |id|Long|주문 아이디|
  |storeId|Long|가게 아이디|
  |userId|Long|주문자 아이디|
  |menuName|String|메뉴 이름|
  |status|MenuStatus|배달 상태|
  |createdAt|LocalDateTime|주문 생성 날짜 저장|
  |modifiedAt|LocalDateTime|주문 수정 날짜 저장|
  
  ```json
  {
    "id": 1,
    "storeId": 1,
    "userId": 1,
    "menuName": "와퍼",
    "status": "COOKING",
    "createdAt": "2024-12-06T19:50:48.636194",
    "modifiedAt": "2024-12-06T19:59:33.354738"
  }


#### 4. 메뉴 CRUD

##### 메뉴 생성
- **URL**: `/api/owner/menus`
- **Method**: `POST`
- **상태코드**: '201 CREATED, 401 UNAUTHORIZED, 404 NOT FOUND
- OWNER 사용자만 생성 가능
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |storeId|Long|가게 아이디|O|
  |menuName|String|메뉴 이름|O|
  |price|Integer|가격|O|
  
 
  

  ```json
  {
    "storeId": 1,
    "menuName": "부대전골",
    "price": 9000
  }
- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |menuid|Long|메뉴 아이디|
  |menuName|String|메뉴 이름|
  |price|Integer|가격|
  |createdAt|LocalDateTime|주문 생성 날짜 저장|
  |modifiedAt|LocalDateTime|주문 수정 날짜 저장|
  
  ```json
  {
    "menuId": 10,
    "menuName": "부대전골",
    "price": 9000,
    "status": "ACTIVE",
    ”created_at": "2024-12-03T13:55:22.12587",
    ”modified_at”: "2024-12-04T13:55:22.12587"
  }


##### 메뉴 수정
- **URL**: `/api/owner/menus/{menuId}`
- **Method**: `PATCH`
- **상태코드**: '200 OK, 401 UNAUTHORIZED, 404 NOT FOUND
- OWNER 사용자만 수정 가능
- **요청**



  |이름|타입|설명|필수|
  |:----:|:----:|:-------:|:--:|
  |storeId|Long|가게 아이디|O|
  |menuName|String|메뉴 이름|O|
  |price|Integer|가격|O|
  
 
  

  ```json
  {
    "storeId": 1,
    "menuName": "된장찌개",
    "price": 9000
  }
- **응답**

  
  |이름|타입|설명|
  |:----:|:----:|:-------:|
  |menuid|Long|메뉴 아이디|
  |menuName|String|메뉴 이름|
  |price|Integer|가격|
  |createdAt|LocalDateTime|주문 생성 날짜 저장|
  |modifiedAt|LocalDateTime|주문 수정 날짜 저장|
  
  ```json
  {
    "menuId": 10,
    "menuName": "된장찌개",
    "price": 9000,
    "status": "ACTIVE",
    ”created_at": "2024-12-03T13:55:22.12587",
    ”modified_at”: "2024-12-04T13:55:22.12587"
  }


##### 메뉴 삭제
- **URL**: `/api/owner/menus/{menuId}`
- **Method**: `DELETE`
- **상태코드**: '200 OK, 401 UNAUTHORIZED, 404 NOT FOUND
- OWNER 사용자만 삭제 가능
- **응답**

  "삭제 되었습니다"

