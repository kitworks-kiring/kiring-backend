# 1. 사용할 기반 이미지 선택 (예: Eclipse Temurin JDK 21 JRE)
FROM eclipse-temurin:21-jre-jammy
# 2. 작업 디렉토리 설정 (컨테이너 내부)
WORKDIR /app
# 3. 빌드된 JAR 파일을 컨테이너 내부로 복사
#    (첫 번째 인자는 호스트 경로, 두 번째 인자는 컨테이너 내부 경로)
#    아래 ARG와 COPY는 멀티 스테이지 빌드를 사용하지 않을 경우의 예시입니다.
#    일반적으로 CI에서 빌드된 JAR를 사용합니다.
#    여기서는 build/libs/app.jar로 가정합니다. CI에서는 이 파일을 Docker 컨텍스트로 가져와야 합니다.
COPY build/libs/*.jar app.jar
# 4. 애플리케이션이 사용할 포트 노출 (Spring Boot 기본 포트 8080)
EXPOSE 8080
# 5. 컨테이너 시작 시 실행될 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]

# (선택) 추가적인 환경변수나 최적화 옵션 설정 가능
# ENV JAVA_OPTS="-Xms256m -Xmx512m"
# ENTRYPOINT ["java", "${JAVA_OPTS}", "-jar", "app.jar"]