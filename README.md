# Cluvr API

Cluvr API는 사용자들이 다양한 클럽을 생성하고 참여할 수 있는 소셜 플랫폼의 백엔드 API입니다. 스터디, 프로젝트, 커뮤니티 등 다양한 목적의 클럽을 운영하고, 멤버들과 소통할 수 있는 종합적인 플랫폼을 제공합니다.


---

## 📅 개발 기간

**2025.05.27 \~  2025.07.04**

## ⚒ 개발 팀 소개

| 역할 | 리더 |  부리더 | 팀원 | 팀원 | 팀원 | 팀원 |
|:--:|:--:|:--:|:--:|:--:|:--:|:--:|
|이름|나원준|박신영|박선영|이용환|정승원|정은세|
|GitHub|[dnjs5024](https://github.com/dnjs5024)|[sinyoung0403](https://github.com/sinyoung0403)|[Tcimel](https://github.com/Tcimel)|[YongLeeCode](https://github.com/YongLeeCode)|[alpomjeong](https://github.com/alpomjeong)|[escomputer](https://github.com/escomputer)|
|기술블로그|[나원준 tistory](https://study5024.tistory.com/)|[박신영 tistory](https://sintory-04.tistory.com/)|[박선영 tistory](https://americanoallday.tistory.com/)|[이용환 vlog](https://velog.io/@yong-lee/)|[정승원 tistory](https://alpomjeong.tistory.com/)|[정은세 tistory](https://escomputer.tistory.com/)|

---

## ⚙ 기술 스택

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

## 🧩 ERD

(이미지 첨부 예정)

---

## ♣️ 전체 시스템 구조

(이미지 첨부 예정)

---

## 📁 주요 패키지 구조

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

## 🛠 주요 기능 요약

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

## 📖 API 명세서

* (Postman 또는 Swagger 링크 예정)

---

## 🗂 Wiki 문서

- 프로젝트를 진행하며 남긴 기술적 선택의 근거, 구조 설계 과정, 주요 도메인 설명 등을 아래 문서에 정리했습니다.
- WIKI HOME LINK: [📖 WIKI HOME](https://github.com/QuestMarkTeam/cluvr-api/wiki)

### 1. 도메인 설명

- [유저(User) 도메인 개요 및 주요 기능 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki)
- [인증인가(Auth) 도메인 개요 및 주요 기능 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki)
- [게시판(Board) 도메인 개요 및 주요 기능 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki)
- [클럽(Club) 도메인 개요 및 주요 기능 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki)
- [클럽 가입(JoinRequest) 도메인 개요 및 주요 기능 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki)
- [문제 양식 및 가입 양식(ApplicationForm) 도메인 개요 및 주요 기능 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki/%5B%EA%B8%B0%EB%8A%A5%5D-%ED%81%B4%EB%9F%BD-%EA%B3%B5%EC%A7%80%EC%82%AC%ED%95%AD(Notice)-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B0%9C%EC%9A%94-%EB%B0%8F-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC)
- [클럽 TIL(Today‐I‐Learn) 도메인 개요 및 주요 기능 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki)
- [클럽 TIL Review 도메인 개요 및 주요 기능 정리](https://github.com/QuestMarkTeam/cluvr-api/wiki)


### 2. 기술적 의사결정 및 성능개선

- [기술적 의사결정 및 성능개선](https://github.com/QuestMarkTeam/cluvr-api/wiki#6-%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)

### 3. 트러블 슈팅

- [트러블슈팅](https://github.com/QuestMarkTeam/cluvr-api/wiki#6-%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)

---

## ⚙ 프로젝트 실행

```bash
# 실행 순서
1. git clone https://github.com/your-username/cluvr-api.git
2. cp .env.example .env
3. docker-compose up -d
4. ./gradlew bootRun 또는 java -jar build/libs/cluvr-api.jar
```

---

## 🔐 환경 변수 예시 (.env)

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
RMQ_PASSWORD=admin1234

# JWT
JWT_SECRET_KEY=your-jwt-secret

# AWS S3
ACCESS_AWS=your-access-key
SECRET_AWS=your-secret-key
```
