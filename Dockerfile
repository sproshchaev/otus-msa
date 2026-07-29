# Базовый образ: JRE 21 на Alpine Linux
FROM eclipse-temurin:21-jre-alpine

# Метаданные образа
LABEL name="hello-service"
LABEL course="Microservice Architecture"

# Рабочая директория внутри образа
WORKDIR /app

# Переменные окружения (значения по умолчанию)
ENV GREETING="Hello"
ENV DATA_DIR="/data"

# Копируем собранный jar внутрь образа
COPY target/hello-service.jar app.jar

# Документируем порт, который слушает приложение
EXPOSE 8080

# Команда запуска в exec-форме
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
