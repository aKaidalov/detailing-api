# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

JValeting is a Spring Boot REST API for managing vehicle valet services and bookings. Built with Spring Boot 3.5.5, Java 21, PostgreSQL, and Gradle.

## Build & Development Commands

```bash
# Build
./gradlew build
./gradlew clean build

# Run application (requires PostgreSQL via Docker)
docker-compose up -d          # Start PostgreSQL
./gradlew bootRun             # Start app on http://localhost:8080

# Tests
./gradlew test                # Run all tests
./gradlew test --tests "ClassName.methodName"  # Run single test
```

## Architecture

**Layered REST API with feature modules:**

```
src/main/java/ee/joonasvaleting/jvaleting/
├── {feature}/
│   ├── *Entity.java      # JPA entity
│   ├── *Controller.java  # REST endpoints
│   ├── *Service.java     # Business logic
│   ├── *Repository.java  # Spring Data JPA
│   ├── *Dto.java         # API data transfer
│   └── *Mapper.java      # Entity ↔ DTO conversion
└── common/
    ├── entity/AuditingEntity.java    # Base class with createdAt/updatedAt
    └── exceptions/                    # Custom exceptions
```

**Current feature modules:** `bookingstatus`, `serviceitem`, `timeslot`, `vehicle`, `vehicletype`

## Key Patterns

- **Lombok:** `@RequiredArgsConstructor` for constructor injection, `@Data`/`@Getter`/`@Setter` for entities/DTOs
- **MapStruct:** Interface-based mappers with `@Mapper(componentModel = "spring")`
- **Manual schema management:** `spring.jpa.hibernate.ddl-auto=none`, schema in `schema.sql`, demo data in `import.sql`

## Domain Model

- **VehicleType** → **Vehicle** (one-to-many)
- **ServiceItem** → self-referencing parent-child hierarchy (for service bundles)
- **TimeSlot** - booking time windows with availability and max bookings
- **BookingStatus** - status tracking (Pending, Confirmed, In Progress, Completed, Cancelled, No Show)

## Database

PostgreSQL on localhost:5432 (via Docker Compose). Connection: `mydatabase` / `myuser` / `secret`

Schema managed manually via SQL files - modify `schema.sql` for DDL changes.
