FROM openjdk:17-jdk-slim
COPY target/*.jar application.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "application.jar"]