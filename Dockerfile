# Стадия сборки
FROM gradle:8.7-jdk21 AS builder

WORKDIR /workspace

COPY app/build.gradle.kts .
COPY app/settings.gradle.kts .
COPY app/gradle ./gradle
COPY app/src ./src

RUN gradle --no-daemon shadowJar

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=builder /workspace/build/libs/app.jar .

ENV PORT=8080
EXPOSE $PORT
CMD ["java", "-jar", "app.jar"]
