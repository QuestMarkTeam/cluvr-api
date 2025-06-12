# 빌드 단계
FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /app
COPY .. .
RUN gradle bootJar

# 실행 단계
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]