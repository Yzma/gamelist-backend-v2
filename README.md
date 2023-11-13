<h1 align="center">
  Game List V2 (Java Spring Backend)
  <br>
</h1>

<h4 align="center">Game List is a social game-tracking app that allows users to easily search, filter, and track their video game collections, enabling them to share their gaming status and interact with others through comments, likes, and following. The V2 frontend made in React.js can be accessed <a href="https://github.com/Yzma/game-list-frontend-v2">here.</a></h4>
<br>

<p align="center">
  <a href="#tech-stack">Tech Stack</a> •
  <a href="#database-erd">Database ERD</a> •
  <a href="#getting-started">Getting Started</a> •
  <a href="#authors">Authors</a> 
</p>

## Tech Stack

- [Java](https://www.java.com/en/)
- [Spring Framework](https://spring.io/projects/spring-framework)
- [MapStruct](https://mapstruct.org/)
- [Java JWT](https://github.com/auth0/java-jwt)
- [JUnit](https://junit.org/junit5/)
- [Postgres SQL](https://www.postgresql.org/)

## Database ERD

![Database ERD](/Database%20ERD.png)

## Getting Started

**Prerequisites:**

- [Java](https://www.java.com/en/)
- [Git](https://git-scm.com)
- [Docker](https://www.docker.com/)

1. Clone the repository

```sh
git clone https://github.com/Yzma/gamelist-backend-v2.git
```

2. Navigate to the project directory

```sh
cd gamelist-backend-v2
```

3. Fill out all variables in .env file.

```yaml
DOCKER_POSTGRES_USER=
DOCKER_POSTGRES_PASSWORD=
DOCKER_POSTGRES_DB_DEV=
DOCKER_POSTGRES_DB_TEST=
DOCKER_POSTGRES_NAME=
```

4. Make sure to fill out all 3 application yaml files in src/main/resources: application.yml, application-development.yml, and application-test.yml. Setup database urls that avoid conflicts with other applications. Also fill application.yml file with jwt token secret key.
```yaml
# src/main/resources/application-dev.yml
spring:
  datasource:
    username: postgres
    password: postgres
    url: jdbc:postgresql://localhost:5332/game_list_dev
    driver-class-name: org.postgresql.Driver
  flyway:
    enabled: true
    baseline-on-migrate: true
  jpa:
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
    database: postgresql
  sql:
    init:
      mode: never

logging:
  level:
    org.hibernate.sql: DEBUG
    flyway: DEBUG
server:
  port: 8080

```

```yaml
# src/main/resources/application-test.yml
application:
  title: GameList
  version: 1.0.0

spring:
  datasource:
    url: jdbc:postgresql://localhost:5333/game_list_test
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    properties:
      active: test
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
    database: postgresql
  sql:
    init:
      mode: never

```

```yaml
# src/main/resources/application.yml
application:
  title: GameList
  version: 1.0.0

security:
  jwt:
    secret-key: [JWT_SECRET_HERE]
spring:
  profiles:
    active: dev

management:
  endpoints:
    web:
      exposure:
        include: "*"

```

6. Fill out the docker-compose.yml file, it should look something like this:

```yaml
version: '3.8'

services:
  dev_db:
    image: postgres:15-alpine
    ports:
      - "5332:5432"
    environment:
      - POSTGRES_USER=${DOCKER_POSTGRES_USER}
      - POSTGRES_PASSWORD=${DOCKER_POSTGRES_PASSWORD}
      - POSTGRES_DB=${DOCKER_POSTGRES_DB_DEV}

  test_db:
    image: postgres:15-alpine
    ports:
      - "5333:5432"
    environment:
      - POSTGRES_USER=${DOCKER_POSTGRES_USER}
      - POSTGRES_PASSWORD=${DOCKER_POSTGRES_PASSWORD}
      - POSTGRES_DB=${DOCKER_POSTGRES_DB_TEST}
```
7. Download Docker Desktop App from the official website: https://www.docker.com/products/docker-desktop/. And run the following command in your terminal

```sh
docker-compose up
```

8. Now open your favorite IDE and run the application. We used [IntelliJ](https://www.jetbrains.com/idea/)

## Authors

- <a href="https://github.com/Yzma">Andrew Caruso</a>
- <a href="https://github.com/changLiCoding">Chang Li<a>
- <a href="https://github.com/tienviet10">Viet Tran<a>
