# gamelist-spring

<h1 align="center">
  Game List V1 (Ruby on Rails Backend)
  <br>
</h1>

<h4 align="center">Game List is a social game-tracking app that allows users to easily search, filter, and track their video game collections, enabling them to share their gaming status and interact with others through comments, likes, and following. The V1 frontend made in React.js can be accessed <a href="https://github.com/Yzma/game_list_client">here.</a></h4>
<br>

<p align="center">
  <a href="#tech-stack">Tech Stack</a> •
  <a href="#screenshots">Screenshots</a> •
  <a href="#how-to-use">Getting Started</a> •
  <a href="#authors">Authors</a>
</p>

<h3 align="center">Live Demo: <a href="https://gamelistapp.netlify.app/home/">https://gamelistapp.netlify.app/home</a></h3>
<br>

## Tech Stack & Features

- [Ruby on Rails](https://rubyonrails.org/)
- [GraphQL](https://graphql.org/)
- [BCrypt](https://github.com/bcrypt-ruby/bcrypt-ruby)
- [RSpec](https://rspec.info/)
- [Postgres SQL](https://www.postgresql.org/)

## Database ERD

![alt text](http://url/to/img.png)

## Getting Started

**Prerequisites:**

- [Ruby](https://www.ruby-lang.org/en/)
- [Git](https://git-scm.com)
- [psql](https://www.postgresql.org/docs/current/app-psql.html)

1. Clone the repository

```sh
git clone https://github.com/Yzma/game_list.git
```

2. Navigate to the project directory

```sh
cd game_list
```

3. Install dependencies

```sh
gem install
```

4. Fill out all variables in .env file.

```yaml
DOCKER_POSTGRES_USER=
DOCKER_POSTGRES_PASSWORD=
DOCKER_POSTGRES_DB_DEV=
DOCKER_POSTGRES_DB_TEST=
DOCKER_POSTGRES_NAME=
```

5. Make sure to fill out all 3 application yaml files from demo: applicatoin.yml, application-development.yml, and application-test.yml. Setup database urls that avoid conflicts with other applications. Also fill application.yml file with jwt token secret key.
```yaml
#application-dev.yml
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
#application-test.yml
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
#application.yml
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
6. Check docker-compose.yml file to make sure ports are not conflicting with other applications, and also make sure to fill out all variables in .env file.

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
7. Download Docker Desktop App from official website: https://www.docker.com/products/docker-desktop/. Run docker compose in terminal to start up the database

```sh
docker-compose up
```




8. Seed the database (TODO)

```sh
rails db:seed
```

9. Run the application

```sh
rails start
```

## Authors

- <a href="https://github.com/Yzma">Andrew Caruso</a>
- <a href="https://github.com/changLiCoding">Chang Li<a>
- <a href="https://github.com/tienviet10">Viet Tran<a>
