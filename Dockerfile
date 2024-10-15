FROM openjdk:11-jre-slim
COPY target/kafka-stream-app-1.0-SNAPSHOT.jar /app/word-count-app.jar
ENTRYPOINT ["java", "-jar", "/app/word-count-app.jar"]