FROM openjdk:17-jdk-slim
ADD build/libs/task-management-system-0.0.1-SNAPSHOT.jar /usr/local/app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev", "-jar", "/usr/local/app.jar"]