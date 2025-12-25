# Detailing API - Development Roadmap

## Current Status
**Phase:** 1 of 5 - Foundation
**Focus:** schema.sql
**Last working on:** Infrastructure setup complete, ready for schema.sql

---

## Phase 0: Infrastructure Setup
> Dependencies, configuration, and tooling

- [x] Add springdoc-openapi dependency
- [x] Add spring-security dependency
- [x] Create test configuration (same Docker PostgreSQL)
- [x] Basic security config (permit all)

---

## Phase 1: Foundation
> Database schema and core infrastructure

- [ ] Create schema.sql with all 14 tables
- [ ] Create import.sql with demo data
- [ ] User entity + repository
- [ ] BusinessSettings entity + repository

### Phase 1 API Outline
```
POST   /api/v1/admin/login
GET    /api/v1/admin/business-settings
PUT    /api/v1/admin/business-settings
```

---

## Phase 2: Catalog Module
> Service catalog management (no external dependencies)

- [ ] VehicleType module (entity/repo/service/controller/DTOs)
- [ ] Package module + VehicleService junction
- [ ] AddOn module + PackageAddOn junction
- [ ] DeliveryType module

### Phase 2 API Outline
```
# Public (client booking flow)
GET    /api/v1/vehicle-types
GET    /api/v1/vehicle-types/{id}/packages
GET    /api/v1/packages/{id}/add-ons
GET    /api/v1/delivery-types

# Admin CRUD - Vehicle Types
GET    /api/v1/admin/vehicle-types
POST   /api/v1/admin/vehicle-types
PUT    /api/v1/admin/vehicle-types/{id}
DELETE /api/v1/admin/vehicle-types/{id}

# Admin CRUD - Packages (same pattern)
GET    /api/v1/admin/packages
POST   /api/v1/admin/packages
PUT    /api/v1/admin/packages/{id}
DELETE /api/v1/admin/packages/{id}

# Admin CRUD - Add-ons (same pattern)
GET    /api/v1/admin/add-ons
POST   /api/v1/admin/add-ons
PUT    /api/v1/admin/add-ons/{id}
DELETE /api/v1/admin/add-ons/{id}

# Admin CRUD - Delivery Types (same pattern)
GET    /api/v1/admin/delivery-types
POST   /api/v1/admin/delivery-types
PUT    /api/v1/admin/delivery-types/{id}
DELETE /api/v1/admin/delivery-types/{id}
```

---

## Phase 3: Scheduling Module
> Time slot management

- [ ] TimeSlotTemplate module
- [ ] TimeSlot module
- [ ] Availability logic

### Phase 3 API Outline
```
# Public
GET    /api/v1/time-slots?date={date}

# Admin - Templates
GET    /api/v1/admin/time-slot-templates
POST   /api/v1/admin/time-slot-templates
PUT    /api/v1/admin/time-slot-templates/{id}
DELETE /api/v1/admin/time-slot-templates/{id}

# Admin - Slots
GET    /api/v1/admin/time-slots?from={date}&to={date}
POST   /api/v1/admin/time-slots
PUT    /api/v1/admin/time-slots/{id}
DELETE /api/v1/admin/time-slots/{id}
```

---

## Phase 4: Booking Module
> Customer booking flow (depends on Phase 2 + 3)

- [ ] Booking entity + BookingAddOn junction
- [ ] Booking CRUD operations
- [ ] Price calculation service
- [ ] Booking validation (slot availability, package-vehicle compatibility)
- [ ] Booking status workflow (PENDING -> CONFIRMED -> COMPLETED)

### Phase 4 API Outline
```
# Public (client)
POST   /api/v1/bookings
GET    /api/v1/bookings/{ref}
DELETE /api/v1/bookings/{ref}

# Admin
GET    /api/v1/admin/bookings
GET    /api/v1/admin/bookings/{id}
PUT    /api/v1/admin/bookings/{id}
PUT    /api/v1/admin/bookings/{id}/status
DELETE /api/v1/admin/bookings/{id}
```

---

## Phase 5: Notifications & Polish
> Email notifications and final features

- [ ] Notification template management
- [ ] NotificationLog + email sending service
- [ ] Admin analytics endpoints
- [ ] Error handling polish

### Phase 5 API Outline
```
# Admin - Notification Templates
GET    /api/v1/admin/notifications
PUT    /api/v1/admin/notifications/{type}

# Admin - Analytics
GET    /api/v1/admin/analytics/bookings?period={day|week|month}
GET    /api/v1/admin/analytics/revenue?period={day|week|month}
```

---

## Decisions Log

| Date | Decision | Rationale |
|------|----------|-----------|
| 2024-12-25 | API versioning: /api/v1/ | Future-proofing for breaking changes |
| 2024-12-25 | Same Docker PostgreSQL for tests | MVP simplicity, no TestContainers |
| — | Session-based auth for MVP | Simpler than JWT, per PRD |
| — | Manual schema.sql management | More control, per CLAUDE.md |
| — | Feature modules pattern | One package per domain entity |

---

## Session History

| Date | Summary | Next Steps |
|------|---------|------------|
| 2024-12-25 | Infrastructure setup (deps, security, test config) | schema.sql |
| 2024-12-24 | Created roadmap structure | Begin Phase 1 |

---

## How to Use This Document

**Starting a session:**
```
Read docs/ROADMAP.md and continue from where we left off
```

**Ending a session:**
```
Update docs/ROADMAP.md: mark completed items, update "Last working on", add session entry
```

**Starting a feature (in new chat):**
```
Read docs/ROADMAP.md. I want to implement [Feature X].
Create a detailed breakdown for this session.
```
