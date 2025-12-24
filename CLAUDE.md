# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Detailing API is a Spring Boot REST API for managing vehicle detailing services and bookings. Built with Spring Boot 3.5.5, Java 21, PostgreSQL, and Gradle.

See `docs/prd/` for full business requirements and `docs/erd/v4/erd.md` for the data model.

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
src/main/java/ee/detailing/api/
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

## Key Patterns

- **Lombok:** `@RequiredArgsConstructor` for constructor injection, `@Data`/`@Getter`/`@Setter` for entities/DTOs
- **MapStruct:** Interface-based mappers with `@Mapper(componentModel = "spring")`
- **Manual schema management:** `spring.jpa.hibernate.ddl-auto=none`, schema in `schema.sql`, demo data in `import.sql`

## Database

PostgreSQL on localhost:5432 (via Docker Compose). Connection: `mydatabase` / `myuser` / `secret`

Schema managed manually via SQL files - modify `schema.sql` for DDL changes.

## Development Workflow

See `docs/ROADMAP.md` for:
- Current implementation status
- Feature completion checklist
- Session history and context

**Session commands:**
- Start: "Read docs/ROADMAP.md and continue from where we left off"
- End: "Update ROADMAP.md with progress"
