# Mental Growth Diary 

<hr/> 

## ERD
![Mental](https://github.com/user-attachments/assets/bf3ad4b6-0bec-4aaa-b62e-600ed39e1fbd)



## 주제   

하루를 기록하고 마음을 치유하는 심리 상담 일기 서비스입니다.   
복잡한 마음을 정리하기 위해 감정 일기를 기록하고, 자신의 상태를 이해하며,    
상담 전문가의 도움을 받아 마음의 성장 하는것을 목표로 합니다.

## 기능
### 1. 사용자(내담자) 기능

#### 1.1 회원가입 및 로그인   

* ##### 회원가입:   
  * 사용자(내담자)는 이메일과 비밀번호를 이용해 회원가입 가능.   
  
* ##### JWT 토큰 발급 로그인:
  * 사용자가 로그인 시 이메일과 비밀번호를 제출하면, 인증 성공 후 JWT 토큰 발급.   
  * JWT 토큰을 통해 세션 유지 없이 사용자 인증 및 권한 확인 가능.   
  * 로그인 이후 토큰을 포함한 Authorization Header로 요청 처리.

#### 1.2 상담사 선정 기능  
* 상담사의 이름, 상담 유형(예: 위로, 현실적 조언), 상담 키워드(예: 스트레스 관리) 등의 상세 정보를 통해 상담사를 선택할 수 있는 기능.    
  
#### 1.3 일기 작성 및 관리    
* 날짜별 일기 작성, 수정, 삭제 기능.
* 고정된 질문을 통해 일기 작성 유도:
  * 오늘 하루 중 감사했던 일이나 기쁜 일이 있었는지 기록하기.   
  * 오늘 어떤 일로 힘들었는지 기록하기.   
  * 내일의 나에게 더 좋은 모습을 보여주기 위해 어떻게 변화할지 기록하기.
 
#### 1.4 일기 조회 및 분석  
* 해시태그를 사용한 기분 선택: 일기 작성 시 그날의 기분을 해시태그로 표현.    
* 기분별 일기 조회: 사용자가 입력한 해시태그를 통해 특정 기분이 기록된 일기만 필터링하여 조회 가능.    

#### 1.5 일기 피드백 요청
* 작성한 일기에 대해 상담사에게 피드백 요청.
* 요청한 일기 하루치에 대해 상담사가 피드백을 제공.

#### 1.6 오프라인 상담 요청    
* 상담사 예약 현황을 확인하고 빈 시간에 오프라인 상담 상담을 요청.    
  
#### 1.7 알림 기능    
* 상담사가 피드백을 작성하면 내담자에게 피드백 확인 메일로 알림 발송.
  
### 2. 상담사 기능    

#### 2.1 회원가입 및 로그인    
* 상담사 또한 회원가입 및 JWT 토큰 기반 로그인을 통해 시스템에 접근 가능.
  
#### 2.2 일기 열람 및 분석    
* 내담자가 요청한 하루치 일기를 열람하고 심리적 평가를 진행.     
* 해당 데이터는 해당 상담사만 조회 가능.
  
#### 2.3 조언 및 피드백 작성    
* 일기 내용을 기반으로 내담자에게 맞춤형 조언 및 피드백 작성.
  
#### 2.4 상담 요청 관리    
* 내담자의 오프라인 상담 요청을 확인하고 수락/거절할 수 있는 기능.      
* 상담 요청과 함께 상담 시간 확인.
     
#### 2.5 내담자 관리    

* 현재 자신과 연결된 내담자를 조회.    
* 동적 검색: 날짜, 내담자 이름, 기분, 예약 현황, 피드백 작성 여부 등을 기준으로 필터링 가능.
  
#### 2.6 알림 기능    
* 내담자가 일기를 작성한 후 2일 이상 피드백이 작성되지 않은 경우 스케줄러를 사용해서 상담자에게 피드백 작성 요청 알림 메일 발송.    
* 당일 예약된 오프라인 상담 일정 알림.
  
### 3. 공통 기능    
#### 3.1 JWT 기반 인증 및 권한 관리    
* JWT 토큰 발급:    
* 로그인 성공 시 JWT 토큰을 발급하며, 이 토큰을 통해 사용자의 세션 관리 없이 인증 및 권한 확인.    
* 권한 관리:    
    * 상담사는 자신과 연결된 내담자의 데이터만 열람 가능.
    * 내담자는 자신의 데이터만 수정/삭제 가능.
      
#### 3.2 로그 관리    
* AOP를 사용하여 다음 작업에 대한 변경 사항을 기록:
  * 일기 작성, 수정, 삭제.
  * 예약 생성 및 상태 변경.
  * 메일 발송 내역.

<hr/> 

##### 기술 스택
Backend: Spring Boot, Spring Security, Spring Data JPA, Spring Mail, JSON Web Token (JWT)      
Database: MariaDB      
Build Tool: Gradle      
Logging: AOP를 활용한 로그 관리      
Email Notification: Spring Mail로 알림 기능 구현      
