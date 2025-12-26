FROM gradle:8.5-jdk17

WORKDIR /app
COPY . .

CMD ["./gradlew", "clean", "test"]
