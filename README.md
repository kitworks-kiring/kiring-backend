# Kiring (키링) - 사내 생활 통합 플랫폼 백엔드

<p align="center">
  <img src="https://github.com/kitworks-kiring/kiring-frontend/blob/main/public/images/og-image.png" alt="Kiring Logo" width="100"/>
</p>

<h3 align="center">맛집 정보부터 팀 일정 공유, 쪽지 기능까지. 즐거운 회사 생활을 위한 모든 기능을 하나로 모은 백엔드 시스템입니다.</h3>

<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk" alt="Java"/>
  <img src="https://img.shields.io/badge/JPA_/_Hibernate-6.x-59666C?style=for-the-badge&logo=hibernate" alt="JPA/Hibernate"/>
  <img src="https://img.shields.io/badge/QueryDSL-5.1-469A64?style=for-the-badge" alt="QueryDSL"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Docker-26.1-2496ED?style=for-the-badge&logo=docker" alt="Docker"/>
  <img src="https://img.shields.io/github/actions/workflow/status/kitworks-kiring/kiring-backend/deploy.yml?branch=develop&style=for-the-badge" alt="CI/CD Status"/>
</p>

---

## 🏛️ 아키텍처
이 프로젝트는 클라우드 네이티브 환경에 최적화된 확장 가능하고 안정적인 아키텍처를 지향합니다. AWS의 관리형 서비스를 적극 활용하여 인프라 관리 부담을 최소화하고, 모든 배포 과정을 자동화하여 개발 생산성을 극대화했습니다.

![Kiring Backend Architecture](https://user-images.githubusercontent.com/11267440/229016191-8f5b5f83-e18e-49b8-a72c-f60dd1804f37.png)
_`사용자 요청 → Route 53 → ALB (HTTPS, ACM 인증서) → EC2 (Docker) → RDS (MySQL)` 의 흐름을 가집니다._

## ✨ 주요 기능
- **인증/인가:** 카카오 OAuth2 소셜 로그인과 JWT (Access/Refresh Token) 기반의 Stateless 인증 시스템
- **맛집(Place) 서비스:** 위치 기반 근처 맛집 검색, 동적 정렬 및 카테고리 필터링, 좋아요/취소 기능
- **팀/멤버 관리:** 팀 생성 및 멤버 소속 관리, 사용자 프로필 조회 및 수정
- **캘린더 서비스:** 팀 공유 캘린더, 일반 일정 등록 및 팀원 생일 동적 표시
- **데이터 관리:** 엑셀 파일 일괄 업로드를 통한 데이터베이스 Bulk Insert
- **API 문서:** Swagger (OpenAPI 3.0)를 통한 자동 API 문서화

## 🛠️ 기술 스택 및 선정 이유

### Backend
| 기술 | 버전 | 선정 이유 및 적용 효과 |
| --- | --- | --- |
| **Spring Boot** | 3.2.x | 강력한 DI/AOP, 방대한 생태계, 다른 Spring 프로젝트와의 유기적인 통합을 통해 안정적이고 유지보수성이 높은 애플리케이션의 기반을 마련했습니다. |
| **Spring Security** | 6.2.x | OAuth2와 JWT를 결합한 복잡한 인증/인가 요구사항을 필터 체인 커스터마이징을 통해 유연하게 구현했습니다. 메서드 시큐리티로 비즈니스 로직 단위의 세밀한 권한 제어를 적용했습니다. |
| **Spring Data JPA**| 3.2.x | 객체지향적인 데이터 접근을 위해 채택했습니다. Fetch Join, `@BatchSize` 등 다양한 최적화 전략을 적용하며 JPA의 동작 원리를 깊이 있게 학습했습니다. |
| **QueryDSL** | 5.1.0 | 문자열 기반 쿼리의 한계를 극복하고, 복잡한 동적 쿼리를 타입-세이프(Type-Safe)하게 작성하기 위해 도입했습니다. 컴파일 시점에 쿼리 오류를 검증하여 런타임 안정성을 크게 향상시켰습니다. |
| **Jib** | 3.4.x | `Dockerfile` 없이도 Java 애플리케이션에 최적화된 Docker 이미지를 빌드하고, 레이어 캐싱을 통해 CI/CD 파이프라인의 빌드 속도를 개선했습니다. |
| **JJWT** | 0.12.x | Stateless 인증 시스템의 핵심인 JWT의 생성, 파싱, 검증을 안정적으로 처리하기 위해 사용했습니다. |

### Database
| 기술 | 버전 | 선정 이유 및 적용 효과 |
| --- | --- | --- |
| **MySQL** | 8.0 | 가장 널리 사용되는 RDBMS로 안정성이 검증되었습니다. 특히 **공간 인덱스(Spatial Index)**와 관련 함수를 활용하여 효율적인 위치 기반 검색 기능을 구현했습니다. |
| **Redis** | 7.x | '좋아요' 기능과 같이 읽기/쓰기가 빈번한 기능의 성능 개선을 위해 도입을 설계했습니다. DB 부하를 줄이고 실시간 랭킹과 같은 기능을 확장할 수 있는 기반을 마련했습니다. |
| **H2 Database**| 2.2.x | 테스트 환경을 프로덕션 DB와 완벽히 분리하여, 네트워크나 외부 환경에 의존하지 않는 빠르고 안정적인 단위/통합 테스트 환경을 구축했습니다. |

### Infra & DevOps
| 기술 | 버전 | 선정 이유 및 적용 효과 |
| --- | --- | --- |
| **AWS** | - | EC2, RDS, ALB, ACM, Route 53 등 AWS의 강력한 관리형 서비스를 활용하여 확장 가능하고 안정적인 인프라를 구축했습니다. |
| **Docker** | 26.x | 애플리케이션을 컨테이너화하여 어떤 환경에서든 동일한 실행을 보장하고, 배포의 일관성과 이식성을 확보했습니다. |
| **GitHub Actions**| - | 코드 포맷 검사, 빌드, 테스트, 이미지 푸시, EC2 배포까지의 전 과정을 자동화하는 **CI/CD 파이프라인**을 구축하여 개발 생산성을 극대화했습니다. |

## 🔥 기술적 도전과 해결 과정

### 1. N+1 쿼리로 인한 API 성능 저하 해결
- **문제 진단:** 맛집 목록 조회 시, 각 맛집의 카테고리를 가져오기 위해 **10건 조회에 11개 이상의 쿼리가 실행**되는 N+1 병목을 확인했습니다. 이로 인해 초기 API 응답 속도가 **평균 500ms 이상** 소요되었습니다.
- **해결 과정:**
  1. **Fetch Join 시도 및 한계 발견:** `JOIN FETCH`를 적용했으나, 컬렉션 페치 조인 시 DB가 아닌 **애플리케이션 메모리에서 페이징이 처리되는 문제**로 인해 오히려 성능이 저하되는 현상을 분석했습니다.
  2. **`@BatchSize`를 통한 최종 해결:** 엔티티의 컬렉션 필드에 `@BatchSize`를 적용, **IN-Clause 최적화**를 통해 DB 레벨 페이징의 이점은 유지하면서 단 한 번의 추가 쿼리로 연관 데이터를 효율적으로 조회하도록 개선했습니다.
- **결과:** API 응답 속도를 **평균 80ms 미만으로 80% 이상 단축**하고, DB 부하를 줄여 서비스 확장성을 확보했습니다.

### 2. 복잡한 동적 쿼리 리팩토링
- **문제 진단:** '내 주변 맛집 검색' 기능에서 "거리순", "좋아요순" 등 다양한 정렬 조건과 필터링을 네이티브 쿼리와 `switch` 문으로 처리하면서 코드 중복과 유지보수의 어려움이 발생했습니다.
- **해결 과정:** **QueryDSL을 도입**하여 문자열 기반 쿼리를 제거하고, 클라이언트가 `Pageable`로 전달하는 어떤 정렬/필터링 조건이든 **단 하나의 리포지토리 메서드** 내에서 동적으로 `WHERE`와 `ORDER BY` 절을 안전하게 생성하도록 리팩토링했습니다.
- **결과:** 3개 이상의 중복 쿼리 메서드를 단일 동적 쿼리로 통합하여 **코드 라인 수를 약 60% 감소**시켰고, 컴파일 시점에 쿼리 오류를 검증할 수 있게 되어 **런타임 안정성을 크게 향상**시켰습니다.

### 3. CI/CD 파이프라인 구축 및 배포 자동화
- **문제 진단:** 초기 개발 단계의 수동 배포는 **15분 이상 소요**되었고, 사람의 실수가 개입될 여지가 많았습니다.
- **해결 과정:** **GitHub Actions** 워크플로우를 작성하여 코드 푸시 시 빌드, 테스트, **Jib을 이용한 최적화된 Docker 이미지 생성**, Docker Hub 푸시, **`ssh-action`을 통한 EC2 배포**까지 전 과정을 자동화했습니다. 또한, AWS ALB와 ACM을 구성하여 **HTTPS 통신 및 SSL 종료**를 구현했습니다.
- **결과:** **수동 배포 시간을 3분 내외로 80% 이상 단축**하여 개발 생산성을 극대화하고, 안전한 서비스 환경을 구축했습니다.

## 🚀 API 명세
API에 대한 상세한 설명과 테스트는 Swagger UI를 통해 확인하실 수 있습니다.
- **Swagger UI:** `[https://api.kiring.shop/swagger-ui.html](https://api.kiring.shop/swagger-ui.html)`

| Method | URL | 설명 |
| --- | --- | --- |
| `POST` | `/api/v1/auth/token/refresh` | Access Token 재발급 |
| `GET`  | `/api/v1/places/nearby`      | 내 주변 맛집 검색 (동적 정렬/필터링) |
| `POST` | `/api/v1/places/{placeId}/like`| 맛집 좋아요 토글 |
| `GET`  | `/api/v1/calendar/weekly`    | 주간 캘린더(일정+생일) 조회 |
| `POST` | `/api/v1/data/places/upload` | 엑셀로 맛집 데이터 일괄 등록 |

## ⚙️ 프로젝트 실행 방법
1.  **Git Clone**
    ```bash
    git clone [https://github.com/kitworks-kiring/kiring-backend.git](https://github.com/kitworks-kiring/kiring-backend.git)
    cd kiring-backend
    ```
2.  **`application.yml` 설정**
    `src/main/resources/` 경로에 `application.yml` 파일을 생성하고, 데이터베이스, JWT, OAuth2 관련 설정값을 입력합니다.
3.  **빌드 및 실행**
    ```bash
    ./gradlew clean build
    java -jar build/libs/kiring-backend-0.0.1-SNAPSHOT.jar
    ```

---
