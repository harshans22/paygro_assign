Sure — here is the **entire `README.md`** in a single block, clean and copy-paste ready:

---

```markdown
# Paygro Logistics Backend

A Spring Boot application for managing Loads and Bookings with PostgreSQL. Built using a layered architecture and industry best practices.

## Features

- Normalized PostgreSQL schema with constraints and relationships
- DTO → Service → Repository architecture (testable and maintainable)
- REST APIs with input validation, pagination, filtering, and status transitions
- Global exception handling using `@ControllerAdvice`
- Swagger/OpenAPI documentation
- High test coverage with JUnit 5 and Mockito
- Dockerized setup with PostgreSQL
- Dummy data auto-loaded via `init.sql`

## Project Structure



src/
├── main/
│ └── java/org/example/paygro_assign/
│ ├── controller/
│ ├── dto/
│ ├── enums/
│ ├── exceptions/
│ ├── model/
│ ├── repository/
│ ├── service/
│ └── PaygroAssignApplication.java
└── test/


## Running the App with Docker

### Prerequisites

* Docker
* Docker Compose

### Start the App

```bash
docker-compose up --build
```

* App URL: `http://localhost:8080`
* Swagger UI: `http://localhost:8080/swagger-ui.html`

### Stop the App

```bash
docker-compose down
```

## Running Tests

```bash
./mvnw test
# or, if Maven is installed
```
