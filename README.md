# Cluvr API

Cluvr API는 사용자들이 다양한 클럽을 생성하고 참여할 수 있는 소셜 플랫폼의 백엔드 API입니다. 스터디, 프로젝트, 커뮤니티 등 다양한 목적의 클럽을 운영하고, 멤버들과 소통할 수 있는 종합적인 플랫폼을 제공합니다.


---

## 개발 기간

**2025.05.27 \~  2025.07.04**

## 개발 팀 소개

| 역할 | 리더 |  부리더 | 팀원 | 팀원 | 팀원 | 팀원 |
|:--:|:--:|:--:|:--:|:--:|:--:|:--:|
|이름|나원준|박신영|박선영|이용환|정승원|정은세|
|GitHub|[dnjs5024](https://github.com/dnjs5024)|[sinyoung0403](https://github.com/sinyoung0403)|[Tcimel](https://github.com/Tcimel)|[YongLeeCode](https://github.com/YongLeeCode)|[alpomjeong](https://github.com/alpomjeong)|[escomputer](https://github.com/escomputer)|
|기술블로그|[나원준 tistory](https://study5024.tistory.com/)|[박신영 tistory](https://sintory-04.tistory.com/)|[박선영 tistory](https://americanoallday.tistory.com/)|[이용환 vlog](https://velog.io/@yong-lee/)|[정승원 tistory](https://alpomjeong.tistory.com/)|[정은세 tistory](https://escomputer.tistory.com/)|

---

## 기술 스택

### Backend

![Java](https://img.shields.io/badge/Java-17-orange?style=flat\&logo=java\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.0-green?style=flat\&logo=springboot\&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-green?style=flat\&logo=spring-security\&logoColor=white)
![Spring WebFlux](https://img.shields.io/badge/Spring_WebFlux-3.0+-green?style=flat\&logo=spring\&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-3.0+-green?style=flat\&logo=spring\&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-5.0+-blue?style=flat\&logo=querydsl\&logoColor=white)

### Database & Cache

![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue?style=flat\&logo=mysql\&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-7.2+-red?style=flat\&logo=redis\&logoColor=white)
![Redisson](https://img.shields.io/badge/Redisson-3.27+-red?style=flat\&logo=redis\&logoColor=white)

### Message Queue

![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.0+-orange?style=flat\&logo=rabbitmq\&logoColor=white)
![Spring AMQP](https://img.shields.io/badge/Spring_AMQP-3.0+-orange?style=flat\&logo=spring\&logoColor=white)

### Infra & DevOps

![AWS Cognito](https://img.shields.io/badge/AWS_Cognito-2.25+-yellow?style=flat\&logo=amazon-aws\&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS_S3-2.25+-yellow?style=flat\&logo=amazon-s3\&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-20.0+-blue?style=flat\&logo=docker\&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker_Compose-2.0+-blue?style=flat\&logo=docker\&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-2.0+-red?style=flat\&logo=jenkins\&logoColor=white)

### Monitoring

![Prometheus](https://img.shields.io/badge/Prometheus-2.0+-orange?style=flat\&logo=prometheus\&logoColor=white)
![Spring Boot Actuator](https://img.shields.io/badge/Spring_Boot_Actuator-3.0+-green?style=flat\&logo=spring\&logoColor=white)
![Micrometer](https://img.shields.io/badge/Micrometer-1.0+-blue?style=flat\&logo=micrometer\&logoColor=white)

### Test & Build

![Gradle](https://img.shields.io/badge/Gradle-8.0+-blue?style=flat\&logo=gradle\&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-1.18+-pink?style=flat\&logo=lombok\&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit_5-5.0+-green?style=flat\&logo=junit5\&logoColor=white)
![TestContainers](https://img.shields.io/badge/TestContainers-1.0+-blue?style=flat\&logo=testcontainers\&logoColor=white)

---

## ERD

![cluvr (1)_pages-to-jpg-0002](https://github.com/user-attachments/assets/987630ce-2ee9-41a9-819a-12a3b34d0fee)

- [Cluvr ERD 링크](https://dbdiagram.io/d/cluvr-686734f9f413ba350838f022)

---


## API 명세서

* [Postman API Document](https://documenter.getpostman.com/view/45603497/2sB2x8DqVq)

---

## 전체 시스템 구조

### 1. 시스템 아키텍처

![시스템아키텍처](https://github.com/user-attachments/assets/4d36ca2a-2909-4b88-ad91-ff000c9d5061)

### 2. 시스템 플로우

![Auth _ Mermaid Chart-2025-07-07-010106](https://github.com/user-attachments/assets/b66da81f-04c6-4780-8ac2-cbfbfa5ee691)

<detalis> <summary>  <b>서비스 플로우 확인</b> </summary>
  
![시스템 플로우](https://github.com/user-attachments/assets/b7e0d306-be82-4684-9f52-e6734e0b8381)

</details>

---

## 주요 패키지 구조

```
src/main/java/com/example/cluvrapi/domain/
├── auth/            인증 및 권한 관리
├── board/           게시판 시스템
├── category/        카테고리 관리
├── club/            클럽 관리
├── clubMember/      클럽 멤버 관리
├── clover/          클로버(내공 시스템)
├── common/          공통 유틸리티
├── gem/             Gem(포인트) 시스템
├── image/           이미지 업로드
├── join/            가입 프로세스
├── notice/          공지사항
├── notification/    알림 시스템
├── payment/         결제 시스템
├── reaction/        좋아요 및 반응
├── reply/           댓글
├── replyChild/      대댓글
├── til/             TIL
├── tilReview/       TIL 리뷰 시스템
├── user/            사용자 정보
```

---

## 주요 기능 요약

### ✅ 클럽 관리

* 클럽 생성/조회/수정/삭제
* 공개/비공개 설정, 가입 방식 커스터마이징
* 클럽 멤버 관리 (클럽장/관리자/일반)

### ✅ 가입 시스템

* 문제제출형, 신청서형, 초대코드, 즉시가입 등 다양한 방식 지원
* 클럽별 양식 관리 및 통계 제공

### ✅ Gem & 결제

* 활동 보상 기반 포인트 시스템
* 클럽 확장, 가입비, 아이템 구매 등 Gem 사용
* 포인트 구매 및 결제 내역 관리

### ✅ 게시판/댓글/반응

* 게시글 작성, 수정, 삭제, 페이징 조회
* 댓글 및 대댓글 지원
* 좋아요 및 반응 기능

### ✅ 인증 및 보안

* AWS Cognito + JWT 기반 인증
* OAuth2 로그인 지원
* 역할 기반 접근 제어

### ✅ 모니터링/분석

* Prometheus 및 Actuator 기반 모니터링
* 사용자 활동 로그 및 통계 수집

### ✅ 알림 시스템

* 실시간 알림 (RabbitMQ 기반)
* 이메일 발송 및 사용자 설정 지원

---

## Wiki 문서

- 프로젝트를 진행하며 남긴 기술적 선택의 근거, 구조 설계 과정, 주요 도메인 설명 등을 아래 문서에 정리했습니다.
- WIKI HOME LINK: [📖 WIKI HOME](https://github.com/QuestMarkTeam/cluvr-api/wiki)

### 1. 도메인 설명

- [인증인가- Auth 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%EC%9D%B8%EC%A6%9D-%EC%9D%B8%EA%B0%80(Auth)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [유저- User 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%EC%9C%A0%EC%A0%80(User)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [재화- Gem 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-GEM-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [결제- Payment 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%EA%B2%B0%EC%A0%9C(Payment)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [내공- Clover 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-CLOVER--%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [클럽- Club 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%ED%81%B4%EB%9F%BD(Club)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [클럽- Application Form 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%EB%AC%B8%EC%A0%9C-%EC%96%91%EC%8B%9D-%EB%B0%8F-%EA%B0%80%EC%9E%85-%EC%96%91%EC%8B%9D(ApplicationForm)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [클럽- Join Request 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%ED%81%B4%EB%9F%BD-%EA%B0%80%EC%9E%85(Join-Request)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [클럽- Notice 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%ED%81%B4%EB%9F%BD-%EA%B3%B5%EC%A7%80%EC%82%AC%ED%95%AD(Notice)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [클럽- TIL 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%ED%81%B4%EB%9F%BD-TIL(Today%E2%80%90I%E2%80%90Learn)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [클럽- TIL Review 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%ED%81%B4%EB%9F%BD-TIL-Review-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [커뮤니티- Board 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%EA%B2%8C%EC%8B%9C%EA%B8%80(board)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [커뮤니티- Reply 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%EB%8C%93%EA%B8%80(reply-&-replyChildren)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [커뮤니티- Reaction 도메인](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%EB%A6%AC%EC%95%A1%EC%85%98(Reaction)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)


### 2. 기술적 의사결정 및 성능개선

- [CI/CD 구조 개선 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-CI-CD-%EA%B5%AC%EC%A1%B0-%EA%B0%9C%EC%84%A0-%EC%A0%95%EB%A6%AC)
- [Cognito 기반 OAuth2 도입](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-Cognito-%EA%B8%B0%EB%B0%98-OAuth2-%EB%8F%84%EC%9E%85)
- [OpenAI API 연동 기술적 의사결정(ChatGPT연동)](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-OpenAI-API-%EC%97%B0%EB%8F%99-%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95(ChatGPT%EC%97%B0%EB%8F%99))
- [시스템 아키텍처 전환 및 구조 개편](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EC%A0%84%ED%99%98-%EB%B0%8F-%EA%B5%AC%EC%A1%B0-%EA%B0%9C%ED%8E%B8)
- [인증 아키텍처 (API Gateway & Cognito)](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-%EC%9D%B8%EC%A6%9D-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-(API-Gateway-&-Cognito))
- [클럽 가입 로직 재설계 책임 주체 명확화와 도메인 분리](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-%ED%81%B4%EB%9F%BD-%EA%B0%80%EC%9E%85-%EB%A1%9C%EC%A7%81-%EC%9E%AC%EC%84%A4%EA%B3%84-%EC%B1%85%EC%9E%84-%EC%A3%BC%EC%B2%B4-%EB%AA%85%ED%99%95%ED%99%94%EC%99%80-%EB%8F%84%EB%A9%94%EC%9D%B8-%EB%B6%84%EB%A6%AC)
- [클럽 비공개 가입을 위한 초대코드 시스템](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-%ED%81%B4%EB%9F%BD-%EB%B9%84%EA%B3%B5%EA%B0%9C-%EA%B0%80%EC%9E%85%EC%9D%84-%EC%9C%84%ED%95%9C-%EC%B4%88%EB%8C%80%EC%BD%94%EB%93%9C-%EC%8B%9C%EC%8A%A4%ED%85%9C)
- [OpenAI 배치 성능 개선](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EC%84%B1%EB%8A%A5%EA%B0%9C%EC%84%A0%5D-OpenAI-API-%EA%B8%B0%EB%B0%98-TIL-%EC%9E%90%EB%8F%99-%ED%94%BC%EB%93%9C%EB%B0%B1-%EB%B0%B0%EC%B9%98-%EC%8B%9C%EC%8A%A4%ED%85%9C)

### 3. 트러블 슈팅

- [API Gateway와 Cognito 연동 문제 해결](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85%5D-API-Gateway%EC%99%80-Cognito-%EC%97%B0%EB%8F%99-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)
- [Redisson Lock을 이용한 클럽 즉시 가입 동시성 문제 해결](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85%5D-Redisson-Lock%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%81%B4%EB%9F%BD-%EC%A6%89%EC%8B%9C-%EA%B0%80%EC%9E%85-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)

---

## 프로젝트 실행

```bash
# 실행 순서
1. git clone https://github.com/your-username/cluvr-api.git
2. cp .env.example .env
3. docker-compose up -d
4. ./gradlew bootRun 또는 java -jar build/libs/cluvr-api.jar
```

---

## 🔐 환경 변수 (.env)

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=cluvr
DB_PASSWORD=your-db-password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# AWS Cognito
USER_POOL_ID=your-pool-id
CLIENT_ID=your-client-id
CLIENT_SECRET=your-secret

# RabbitMQ
RMQ_USERNAME=admin
RMQ_PASSWORD=pwd

# JWT
JWT_SECRET_KEY=your-jwt-secret

# AWS S3
ACCESS_AWS=your-access-key
SECRET_AWS=your-secret-key
```
