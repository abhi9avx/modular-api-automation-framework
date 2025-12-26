FROM gradle:8.5-jdk17

WORKDIR /app
COPY . .
RUN chmod +x ./gradlew

CMD ["./gradlew", "clean", "test"]
