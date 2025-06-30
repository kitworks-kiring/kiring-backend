# Kiring (키링) - 사내 생활 통합 플랫폼 백엔드

<p align="center">
  <img src="https://github.com/kitworks-kiring/kiring-frontend/blob/main/public/images/og-image.png" alt="Kiring Logo" />
</p>

<h3 align="center">맛집 정보부터 팀 일정 공유, 쪽지 기능까지. 즐거운 회사 생활을 위한 모든 기능을 하나로 모은 백엔드 서비스입니다.</h3>

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
이 프로젝트는 클라우드 네이티브 환경에 최적화된 확장 가능하고 안정적인 아키텍처를 지향합니다. 
AWS의 관리형 서비스를 적극 활용하여 인프라 관리 부담을 최소화하고, 모든 배포 과정을 자동화하여 개발 생산성을 극대화했습니다.

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
| **Redis** | 7.x | 향후 추가 개발 예정인 실시간 랭킹과 같은 기능과 트래픽에 따라 다중인스턴스를 구현할 계획을 가지고 도입했습니다. 현재 좋아요 기능, 오늘의 회원추천 기능에서 사용하고 있습니다. |

### Infra & DevOps
| 기술 | 버전 | 선정 이유 및 적용 효과 |
| --- | --- | --- |
| **AWS** | - | EC2, RDS, ALB, ACM, Route 53 등 AWS의 강력한 관리형 서비스를 활용하여 확장 가능하고 안정적인 인프라를 구축했습니다. |
| **Docker** | 26.x | 애플리케이션을 컨테이너화하여 어떤 환경에서든 동일한 실행을 보장하고, 배포의 일관성과 이식성을 확보했습니다. |
| **GitHub Actions**| - | **JIB**사용하여 빌드, 테스트, 이미지 푸시, EC2 배포까지의 전 과정을 최적화와 자동화하는 **CI/CD 파이프라인**을 구축하여 개발 생산성을 극대화했습니다. |

## 🔥 기술적 도전과 해결 과정


---
