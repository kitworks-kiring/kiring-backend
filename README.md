# Kiring: 회사 생활의 연결고리

![대표 이미지](https://github.com/user-attachments/assets/03dbb6af-f9f8-4ef0-ac03-566c5cb59b0e)

오늘 점심에는 무엇을 먹을지, 일정을 한 눈에 확인할 수 없을지, 퇴근은 또 어떻게 할지.. 회사원이라면 한번 쯤 해본 고민들입니다.

키링은 **회사 근처 식당 리스트, 교통 정보, 캘린터, 커뮤니티 기능 등 회사 생활에 필요한 정보를 통합하여 한 곳에서 제공**하는 것을 목표로 했습니다.

회사 생활을 더욱 편리하게 만들어주는 올인원 플랫폼 키링에서, 유용한 정보를 쉽고 빠르게 확인해보세요!

<br />

## 1️⃣ 백엔드 기술 스택

![BE 기술 스택](https://github.com/user-attachments/assets/e7b36ebb-ffd0-4272-a39a-3bb8119d927b)

<br />

### 🤖 백엔드

| **스택**            | **버전** | **선정 이유 및 효과**                                                                                                                                                                            |
| ------------------- | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Spring Boot**     | 3.2.x    | · 강력한 DI/AOP, 방대한 생태계, 다른 Spring 프로젝트와의 유기적인 통합을 통해 안정적이고 유지보수성이 높은 애플리케이션의 기반을 마련했습니다.                                                   |
| **Spring Security** | 6.2.x    | · OAuth2와 JWT를 결합한 복잡한 인증/인가 요구사항을 필터 체인 커스터마이징을 통해 유연하게 구현했습니다. <br /> · 메서드 시큐리티로 비즈니스 로직 단위의 세밀한 권한 제어를 적용했습니다.        |
| **Spring Data JPA** | 3.2.x    | · 객체지향적인 데이터 접근을 위해 채택했습니다. <br /> · Fetch Join, BatchSize 등 다양한 최적화 전략을 적용하며 JPA의 동작 원리를 깊이 있게 학습했습니다.                                        |
| **QueryDSL**        | 5.1.0    | · 문자열 기반 쿼리의 한계를 극복하고, 복잡한 동적 쿼리를 타입-세이프(Type-Safe)하게 작성하기 위해 도입했습니다. <br /> · 컴파일 시점에 쿼리 오류를 검증하여 런타임 안정성을 크게 향상시켰습니다. |
| **Jib**             | 3.4.x    | · Dockerfile 없이도 Java 애플리케이션에 최적화된 Docker 이미지를 빌드하고, 레이어 캐싱을 통해 CI/CD 파이프라인의 빌드 속도를 개선했습니다.                                                       |
| **JJWT**            | 0.12.x   | · Stateless 인증 시스템의 핵심인 JWT의 생성, 파싱, 검증을 안정적으로 처리하기 위해 사용했습니다.                                                                                                 |

<br />

### 🗂️ 데이터베이스

| **스택**  | **버전** | **선정 이유 및 효과**                                                                                                                                                                    |
| --------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **MySQL** | 8.0      | · 가장 널리 사용되는 RDBMS로 안정성이 검증되었습니다. <br /> · 특히 공간 인덱스(Spatial Index)와 관련 함수를 활용하여 효율적인 위치 기반 검색 기능을 구현했습니다.                       |
| **Redis** | 7.x      | · 향후 추가 개발 예정인 실시간 랭킹과 같은 기능과 트래픽에 따라 다중인스턴스를 구현할 계획을 가지고 도입했습니다. <br /> · 현재 좋아요 기능, 오늘의 회원추천 기능에서 사용하고 있습니다. |

<br />

### 🧬 인프라 & 데브옵스

| **스택**           | **버전** | **선정 이유 및 효과**                                                                                                                            |
| ------------------ | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| **AWS**            | -        | · EC2, RDS, ALB, ACM, Route 53 등 AWS의 강력한 관리형 서비스를 활용하여 확장 가능하고 안정적인 인프라를 구축했습니다.                            |
| **Docker**         | 26.x     | · 애플리케이션을 컨테이너화하여 어떤 환경에서든 동일한 실행을 보장하고, 배포의 일관성과 이식성을 확보했습니다.                                   |
| **GitHub Actions** | -        | · JIB을 사용해 빌드, 테스트, 이미지 푸시, EC2 배포까지의 전 과정을 최적화와 자동화하는 CI/CD 파이프라인을 구축하여 개발 생산성을 극대화했습니다. |

<br />

## 2️⃣ 주요 구현 전략

![주요 구현 전략](https://github.com/user-attachments/assets/c5fda0db-9305-4bf2-8a64-326385222dba)

### 🏛️ 아키텍처

- 클라우드 네이티브 환경에 최적화된 확장 가능하고 안정적인 아키텍처를 지향합니다.

- AWS의 관리형 서비스를 적극 활용하여 인프라 관리 부담을 최소화하고, 모든 배포 과정을 자동화하여 개발 생산성을 극대화했습니다.

- 사용자 요청 → Route 53 → ALB (HTTPS, ACM 인증서) → EC2 (Docker) → RDS (MySQL) 의 흐름을 가집니다.

<br />

### ✨ 기능 구현

- **인증/인가**: 카카오 OAuth2 소셜 로그인과 JWT (Access/Refresh Token) 기반의 Stateless 인증 시스템

- **맛집(Place) 서비스**: 위치 기반 근처 맛집 검색, 동적 정렬 및 카테고리 필터링, 좋아요/취소 기능

- **팀/멤버 관리**: 팀 생성 및 멤버 소속 관리, 사용자 프로필 조회 및 수정

- **캘린더 서비스**: 팀 공유 캘린더, 일반 일정 등록 및 팀원 생일 동적 표시

- **데이터 관리**: 엑셀 파일 일괄 업로드를 통한 데이터베이스 Bulk Insert

- **API 문서**: Swagger (OpenAPI 3.0)를 통한 자동 API 문서화

<br />

## 3️⃣ 기술적 도전과 해결 과정

### 👍 실시간 '좋아요'기능의 성능 병목 해결

☑️ **주요 기술**

- `Redis`, `Spring @Async`, `@EventListener`

☑️ **해결 전략 및 효과**

- 동시성에 따른 DB 쓰기 락 경합 및 커넥션 풀 고갈 문제를 Redis의 Atomic 연산과 Spring의 비동기 이벤트 처리로 해결했습니다.

- Redis에서 실시간 응답 처리 후, DB 저장은 백그라운드에서 수행함으로써 요청 실패율을 72% → 0%, 95% 응답시간을 1초 → 255ms로 개선했습니다.

<br />

### 🔍 검색 API의 유지보수성 확보

☑️ **주요 기술**

- `QueryDSL`

☑️ **해결 전략 및 효과**

- Native Query 방식으로 인한 코드 중복 및 런타임 오류 문제를 QueryDSL의 타입 세이프 동적 쿼리로 전환하여 해결했습니다.

- 정렬 조건 추가 시도마다 쿼리 메서드를 새로 만들 필요 없이 BooleanExpression과 OrderSpecifier 조합으로 확장 가능하게 설계했습니다.

<br />

## 4️⃣ 팀 협업 과정

### 🗃️ Github을 이용한 개발 과정 문서화

![협업1: Github](https://github.com/user-attachments/assets/dcba8364-c333-4d8d-af4b-fba6d39892f8)

<br />

### 🐰 코드 리뷰 AI 도입

![협업2: 코드리뷰 AI](https://github.com/user-attachments/assets/71c95a0b-6d9d-4fb5-a99b-4a289ddbed66)

<br />

### 📔 Notion을 이용한 로드맵 관리

![협업3: Notion](https://github.com/user-attachments/assets/6394a73b-1b42-41fc-badb-dcb9a9163213)

<br />

## 5️⃣ 주요 기능

### 🔐 소셜 로그인

![소셜 로그인 이미지](https://github.com/user-attachments/assets/e81c4725-0c10-4840-bce7-1208523ce170)

<br />

### 🏠 홈

![메인 페이지 이미지](https://github.com/user-attachments/assets/8cb0f297-1824-4b56-9862-283af192a6ca)

<br />

### 🍴 플레이스

![플레이스 페이지 이미지](https://github.com/user-attachments/assets/344666c5-a2a7-4194-88cd-3b841a1d3709)

<br />

### 🚍 교통

![교통 페이지 이미지](https://github.com/user-attachments/assets/c216fe28-4893-4511-8816-33a05948a2d7)

<br />

### 📅 캘린더

![캘린더 페이지 이미지](https://github.com/user-attachments/assets/cf47f2c1-fcf9-45fc-906a-c3fa9ef51f35)

<br />

### 💬 커뮤니티

![커뮤니티 페이지 이미지](https://github.com/user-attachments/assets/52fda276-6f09-431c-8448-5a103c19b127)

<br />

### 💌 종이비행기

![종이비행기 페이지 이미지](https://github.com/user-attachments/assets/b4fecb2c-d0b2-4a92-b3fd-850d0d4eb279)

<br />

### 📄 마이페이지

![마이페이지 이미지](https://github.com/user-attachments/assets/26b35cb6-2415-4ff6-9b7d-7c7431ff268d)

<br />

## 6️⃣ 키링 개발팀

|                                                                           백엔드                                                                           |                                                                         프론트엔드                                                                         |                                                                         프론트엔드                                                                         |                                                                         프론트엔드                                                                         |
| :--------------------------------------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------: |
| <img src="https://pub-cf3b9667253a490495a16433a99bd7ca.r2.dev/main-profile-img/%EB%B0%B1%EC%97%94%EB%93%9C/%EA%B9%80%ED%83%9C%EB%AF%BC.png" width="150" /> | <img src="https://pub-cf3b9667253a490495a16433a99bd7ca.r2.dev/main-profile-img/%ED%94%84%EB%A1%A0%ED%8A%B8/%EA%B9%80%ED%95%9C%EC%86%94.png" width="150" /> | <img src="https://pub-cf3b9667253a490495a16433a99bd7ca.r2.dev/main-profile-img/%ED%94%84%EB%A1%A0%ED%8A%B8/%EB%B0%B1%ED%98%9C%EC%9D%B8.png" width="150" /> | <img src="https://pub-cf3b9667253a490495a16433a99bd7ca.r2.dev/main-profile-img/%ED%94%84%EB%A1%A0%ED%8A%B8/%EC%96%91%EB%8B%A4%EC%9C%97.png" width="150" /> |
|                                                [김태민](https://github.com/kitworks-kiring/kiring-backend)                                                 |                    [김한솔](https://github.com/kitworks-kiring/kiring-frontend/pulls?q=is%3Apr+is%3Aclosed+assignee%3Ahansololiviakim)                     |                       [백혜인](https://github.com/kitworks-kiring/kiring-frontend/pulls?q=is%3Apr+is%3Aclosed+assignee%3Ahyein0112)                        |                        [양다윗](https://github.com/kitworks-kiring/kiring-frontend/pulls?q=is%3Apr+assignee%3Aydw1996+is%3Aclosed)                         |
