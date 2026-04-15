# Customer Rewards Program API

This Spring Boot project calculates customer rewards based on purchases over a 3-month period.

## Features

- Reward logic:
    - 1 point for $51-$100
    - 2 points per $1 over $100
- Fetch rewards by customer or all customers
- Transaction filtering and pagination
- Error handling and logging
- Swagger UI documentation
- API Versioning (`/v1`)

## API Endpoints

| Method | Endpoint                 | Description                       |
|--------|--------------------------|-----------------------------------|
| POST   | /v1/transactions/add     | Add a Transaction                 |
| GET    | /v1/transactions         | List all transactions             |
| GET    | /v1/rewards              | Get rewards for all customers     |
| GET    | /v1/rewards/{customerId} | Get rewards for specific customer |

Swagger: http://localhost:8080/swagger-ui/index.html

## Tech Stack

| Component        | Technology                  |
|------------------|-----------------------------|
| Framework        | Spring Boot 3.2.0           |
| Language         | Java 17                     |
| Build Tool       | Maven                       |
| Database         | H2 (in-memory, default)     |
| ORM              | Spring Data JPA / Hibernate |
| Validation       | Jakarta Bean Validation      |
| API Docs         | Springdoc OpenAPI (Swagger)  |
| Monitoring       | Spring Boot Actuator         |
| Code Generation  | Lombok                       |

## Dependencies (from pom.xml)

| Dependency                             | Purpose                          |
|----------------------------------------|----------------------------------|
| spring-boot-starter-web                | REST API / embedded Tomcat       |
| spring-boot-starter-data-jpa           | JPA / Hibernate ORM              |
| spring-boot-starter-validation         | Jakarta Bean Validation          |
| spring-boot-starter-actuator           | Health checks & monitoring       |
| spring-boot-starter-test               | Testing (JUnit, Mockito, etc.)   |
| springdoc-openapi-starter-webmvc-ui    | Swagger UI                       |
| h2                                     | In-memory database (runtime)     |
| lombok                                 | Boilerplate reduction            |
| jakarta.servlet-api                    | Servlet API (provided scope)     |

## Environment Variables

All configuration is externalised via environment variables with sensible defaults.
See [`.env.example`](.env.example) for the full list.

| Variable                                  | Default                   | Description                          |
|-------------------------------------------|---------------------------|--------------------------------------|
| `SERVER_PORT`                             | `8080`                    | HTTP port                            |
| `SPRING_APPLICATION_NAME`                 | `Rewards API`             | Application name                     |
| `SPRING_DATASOURCE_URL`                   | `jdbc:h2:mem:rewardsdb`   | JDBC connection URL                  |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME`     | `org.h2.Driver`           | JDBC driver class                    |
| `SPRING_DATASOURCE_USERNAME`              | `sa`                      | Database username                    |
| `SPRING_DATASOURCE_PASSWORD`              | _(empty)_                 | Database password                    |
| `SPRING_JPA_HIBERNATE_DDL_AUTO`           | `update`                  | Hibernate DDL strategy               |
| `SPRING_JPA_SHOW_SQL`                     | `false`                   | Log SQL statements                   |
| `SPRING_H2_CONSOLE_ENABLED`              | `true`                    | Enable H2 web console                |
| `SPRING_H2_CONSOLE_PATH`                 | `/h2-console`             | H2 console URL path                  |
| `SPRING_SQL_INIT_MODE`                    | `always`                  | SQL init mode (`always`/`never`)     |
| `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE`| `health,info`            | Actuator endpoints to expose         |
| `LOGGING_LEVEL_ROOT`                      | `INFO`                    | Root log level                       |
| `LOGGING_LEVEL_COM_ASSESSMENT_REWARDS`    | `DEBUG`                   | Application log level                |
| `CORS_ALLOWED_ORIGINS`                    | `*`                       | Comma-separated allowed CORS origins |

## Run Locally

### Prerequisites

- Java 17+
- Maven 3.9+

### Using Maven

```bash
./mvnw spring-boot:run
```

### Using Environment Variables

```bash
cp .env.example .env
# edit .env as needed
source .env && ./mvnw spring-boot:run
```

### Using Docker

```bash
# Build and run
docker build -t rewards-api .
docker run -p 8080:8080 rewards-api

# Or use docker-compose
cp .env.example .env
docker-compose up --build
```

## Running Tests

```bash
./mvnw test
```

## CI/CD

This project uses GitHub Actions for continuous integration. The pipeline (`.github/workflows/ci.yml`) runs on every push/PR to `assessment/TransactionRewardsAPI` and includes:

1. **Build** - Compiles the project and runs tests with Maven
2. **Docker** - Builds the Docker image and verifies the container starts and passes a health check

