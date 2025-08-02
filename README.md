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

```

src/
├── main/
│   └── java/org/example/paygro\_assign/
│        ├── controller/
│        ├── dto/
│        ├── enums/
│        ├── exceptions/
│        ├── model/
│        ├── repository/
│        ├── service/
│        └── PaygroAssignApplication.java
└── test/
(mirrors main structure for unit tests)

````

## API Endpoints

### LoadController (`/load`)

| Method | Endpoint       | Description                     |
|--------|----------------|---------------------------------|
| POST   | `/load`        | Create a new load               |
| GET    | `/load`        | List loads (filter, paginate)   |
| GET    | `/load/{id}`   | Get details of a load           |
| PUT    | `/load/{id}`   | Update an existing load         |
| DELETE | `/load/{id}`   | Delete a load                   |

#### Sample Request: Create Load

```json
POST /load
{
  "shipperId": "shipperA",
  "facility": {
    "loadingPoint": "New York",
    "unloadingPoint": "Chicago",
    "loadingDate": "2025-08-10T08:00:00Z",
    "unloadingDate": "2025-08-12T18:00:00Z"
  },
  "productType": "Electronics",
  "truckType": "Flatbed",
  "noOfTrucks": 3,
  "weight": 12000,
  "comment": "Handle with care"
}
````

### BookingController (`/booking`)

| Method | Endpoint        | Description                  |
| ------ | --------------- | ---------------------------- |
| POST   | `/booking`      | Create a new booking         |
| GET    | `/booking`      | List bookings (with filters) |
| GET    | `/booking/{id}` | Get booking details          |
| PUT    | `/booking/{id}` | Update booking               |
| DELETE | `/booking/{id}` | Delete or cancel a booking   |

#### Sample Request: Create Booking

```json
POST /booking
{
  "loadId": "uuid-of-load",
  "transporterId": "transporter1",
  "proposedRate": 1500,
  "comment": "Can deliver by tomorrow"
}
```

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
