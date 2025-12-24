# Detailing API - Development Roadmap

## Current Status
**Phase:** 1 of 5 - Foundation
**Focus:** Not started
**Last working on:** Project setup complete, ready to begin implementation

---

## Phase 1: Foundation
> Database schema and core infrastructure

- [ ] Create schema.sql with all 14 tables
- [ ] Create import.sql with demo data
- [ ] User entity + repository
- [ ] BusinessSettings entity + repository
- [ ] Basic security configuration (session-based auth)

## Phase 2: Catalog Module
> Service catalog management (no external dependencies)

- [ ] VehicleType module (entity/repo/service/controller/DTOs)
- [ ] Package module + VehicleService junction
- [ ] AddOn module + PackageAddOn junction
- [ ] DeliveryType module

## Phase 3: Scheduling Module
> Time slot management

- [ ] TimeSlotTemplate module
- [ ] TimeSlot module
- [ ] Availability logic

## Phase 4: Booking Module
> Customer booking flow (depends on Phase 2 + 3)

- [ ] Booking entity + BookingAddOn junction
- [ ] Booking CRUD operations
- [ ] Price calculation service
- [ ] Booking validation (slot availability, package-vehicle compatibility)
- [ ] Booking status workflow (PENDING → CONFIRMED → COMPLETED)

## Phase 5: Notifications & Polish
> Email notifications and final features

- [ ] Notification template management
- [ ] NotificationLog + email sending service
- [ ] Admin analytics endpoints
- [ ] Error handling polish
- [ ] API documentation (OpenAPI/Swagger)

---

## Decisions Log

| Date | Decision | Rationale |
|------|----------|-----------|
| — | Session-based auth for MVP | Simpler than JWT, per PRD |
| — | Manual schema.sql management | More control, per CLAUDE.md |
| — | Feature modules pattern | One package per domain entity |

---

## Session History

| Date | Summary | Next Steps |
|------|---------|------------|
| 2024-12-24 | Created roadmap structure | Begin Phase 1: schema.sql |

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
