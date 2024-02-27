# Этап был успешно пройден!

---

# Task-Management-System
###### Test task from company - "***Effective Mobile***"

## Prerequisites:
- Gradle 7
- JDK 17

## How to build:
    gradle clean build

## How to start application locally:
```Available after building project``` - Use [RUN.bat](./RUN.bat) script

## Build Docker image with application inside:
    docker build ./ -t task-management-system

## Start application in Docker container (Postgres):
    docker-compose up

## Swagger documentation:
    http://localhost:8080/swagger-ui/

## Logging with admin rights:
POST
Endpoint: 

    http://localhost:8080/api/v1/auth/authenticate
 
Body:

    {
    "email":"admin@server.com",
    "password":"admin"
    }

Response:

    {
    "token":"TOKEN_EXAMPLE"
    }

#### How to use token:
Paste it to postman -> authorization column (type: Bearer token).

## Gradle test report:
```Available after building project``` - [index.html](./build/reports/tests/test/index.html)
###### ./build/reports/tests/test/index.html

## Jacoco tests coverage report:
```Available after building project``` - [index.html](./build/reports/jacoco/test/html/index.html)
###### ./build/reports/jacoco/test/html/index.html