# Стадия сборки
FROM gradle:8.7-jdk21 AS builder

# Создаем рабочую директорию внутри контейнера
WORKDIR /workspace

# Копируем только необходимые для сборки файлы
COPY app/build.gradle.kts .
COPY app/settings.gradle.kts .
COPY app/gradle ./gradle
COPY app/src ./src

# Запускаем сборку (с кэшированием Gradle)
RUN gradle --no-daemon shadowJar

# Финальная стадия
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем собранный JAR из стадии builder
COPY --from=builder /workspace/build/libs/app.jar .

# Устанавливаем переменные окружения
ENV PORT=8080
EXPOSE $PORT

# Команда запуска
CMD ["java", "-jar", "app.jar"]