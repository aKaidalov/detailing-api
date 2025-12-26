# Detailing API - Development Roadmap

## Current Status
**Phase:** 4 of 5 - Booking Module
**Focus:** Booking entity, price calculation, validation
**Last working on:** Phase 3 complete (TimeSlotTemplate, TimeSlot, Availability logic)

---

## Phase 0: Infrastructure Setup
> Dependencies, configuration, and tooling

- [x] Add springdoc-openapi dependency
- [x] Add spring-security dependency
- [x] Create test configuration (same Docker PostgreSQL)
- [x] Basic security config (permit all)

---

## Phase 1: Foundation ✓
> Database schema and core infrastructure

- [x] Create schema.sql with all 14 tables
- [x] Create import.sql with demo data
- [x] User entity + repository
- [x] BusinessSettings entity + repository
- [x] Auth module (login, session-based)
- [x] Phase 1 API endpoints

### Phase 1 API ✓
```
POST   /api/v1/admin/login
GET    /api/v1/admin/business-settings
PUT    /api/v1/admin/business-settings
```

---

## Phase 2: Catalog Module ✓
> Service catalog management (no external dependencies)

- [x] VehicleType module (entity/repo/service/controller/DTOs)
- [x] Package module + VehicleTypePackage junction
- [x] AddOn module + PackageAddOn junction
- [x] DeliveryType module
- [x] Integration tests (4 controller tests, 24 test cases)

### Phase 2 API ✓
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

## Phase 3: Scheduling Module ✓
> Time slot management

- [x] TimeSlotTemplate module (entity/repo/service/controller/DTOs)
- [x] TimeSlot module (entity/repo/service/controller/DTOs)
- [x] Availability logic (public endpoint returns AVAILABLE slots only)
- [x] Integration tests (2 controller tests, 14 test cases)

### Phase 3 API ✓
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

| Decision | Rationale |
|----------|-----------|
| API versioning: /api/v1/ | Future-proofing for breaking changes |
| Same Docker PostgreSQL for tests | MVP simplicity, no TestContainers |
| Session-based auth for MVP | Simpler than JWT, per PRD |
| Manual schema.sql management | More control, per CLAUDE.md |
| Feature modules pattern | One package per domain entity |
| DebugRunner for dev password | Auto-fix BCrypt hash mismatch on startup |

---

## Session History

| Summary                                                                     | Next Steps |
|-----------------------------------------------------------------------------|------------|
| Phase 3 complete: Scheduling modules (TimeSlotTemplate, TimeSlot) + integration tests (15 files) | Phase 4 - Booking |
| Phase 2 complete: Catalog modules (VehicleType, Package, AddOn, DeliveryType) + integration tests (28 files)         | Phase 3 - Scheduling |
| Phase 1 complete: User, BusinessSettings, Auth, SecurityConfig, DebugRunner | Phase 2 - Catalog |
| import.sql complete (all demo data)                                         | User + BusinessSettings entities |
| schema.sql complete (14 tables, 5 ENUMs)                                    | import.sql |
| Infrastructure setup (deps, security, test config)                          | schema.sql |
| Created roadmap structure                                                   | Begin Phase 1 |

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
