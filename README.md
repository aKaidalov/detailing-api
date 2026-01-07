# Detailing API

Car Detailing Booking System – Spring Boot application for managing a small detailing company's workflow. Clients can book services online, while admins manage time slots, vehicles, service items, and booking statuses.

## Tech Stack

- Java 21
- Spring Boot 3.5
- PostgreSQL
- Gradle

## Features

- Service catalog management
- Time slot scheduling
- Online booking system
- Vehicle & client management
- Admin dashboard API

## Getting Started

### Prerequisites

- Java 21
- Docker (for PostgreSQL)

### 1. Build

```bash
./gradlew clean build
```

### 2. Run Tests

```bash
./gradlew test
```

### 3. Start the Application

Start PostgreSQL:
```bash
docker-compose up -d
```

Run the API:
```bash
./gradlew bootRun
```

The API will be available at http://localhost:8080

Health check:
```bash
curl http://localhost:8080/actuator/health
```

## Related

- [detailing-web](https://github.com/akaidalov/detailing-web) — React frontend
