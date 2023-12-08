FROM openjdk:17-jdk-slim
ADD build/libs/task-management-system-0.0.1-SNAPSHOT.jar /usr/local/app.jar
ENTRYPOINT ["java", "-jar", "/usr/local/app.jar","--spring.profiles.active=dev"]