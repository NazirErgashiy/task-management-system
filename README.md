# Task-Management-System
###### Test task from company - "***Effective Mobile***"

## Prerequisites:
- Gradle 7
- JDK 17

## How to build:
    gradle clean build

## How to start application locally:
Use [RUN.bat](./RUN.bat) script

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

## Gradle test report:
Available after building project - [index.html](./build/reports/tests/test/index.html)

## Jacoco tests coverage report:
Available after building project - [index.html](./build/reports/jacoco/test/html/index.html)