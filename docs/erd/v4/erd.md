# ERD: Vehicle Care Booking System (V4)

## Entity Relationship Diagram

```mermaid
erDiagram
    %% ==========================================
    %% CORE ENTITIES
    %% ==========================================

    user {
        serial id PK
        varchar_255 email
        varchar_255 password_hash
        enum role
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    business_settings {
        serial id PK
        varchar_255 name
        varchar_20 phone
        varchar_255 email
        text address
    }

    %% ==========================================
    %% CATALOG ENTITIES
    %% ==========================================

    vehicle_type {
        serial id PK
        varchar_100 name
        decimal_10_2 base_price
        text description
        boolean is_deliverable
        boolean is_active
        int display_order
        timestamp created_at
        timestamp updated_at
    }

    package {
        serial id PK
        varchar_100 name
        text description
        decimal_10_2 price
        boolean is_active
        int display_order
        timestamp created_at
        timestamp updated_at
    }

    add_on {
        serial id PK
        varchar_100 name
        text description
        decimal_10_2 price
        boolean is_active
        int display_order
        timestamp created_at
        timestamp updated_at
    }

    delivery_type {
        serial id PK
        varchar_50 name
        decimal_10_2 price
        boolean requires_address
        boolean is_active
    }

    %% ==========================================
    %% SCHEDULING ENTITIES
    %% ==========================================

    time_slot_template {
        serial id PK
        time start_time
        time end_time
        boolean is_active
    }

    time_slot {
        serial id PK
        date date
        enum status
        timestamp created_at
        timestamp updated_at
        int time_slot_template_id FK
    }

    %% ==========================================
    %% TRANSACTION ENTITY
    %% ==========================================

    booking {
        serial id PK
        varchar_100 first_name
        varchar_100 last_name
        varchar_255 email
        varchar_20 phone
        varchar_20 vehicle_reg_number
        decimal_10_2 total_price
        text notes
        text address
        enum status
        timestamp created_at
        timestamp updated_at
        int vehicle_type_id FK
        int package_id FK
        int time_slot_id FK "UNIQUE"
        int delivery_type_id FK "NOT NULL"
    }

    %% ==========================================
    %% NOTIFICATION ENTITIES
    %% ==========================================

    notification {
        serial id PK
        enum type
        varchar_255 subject
        text body
        boolean is_active
    }

    notification_log {
        serial id PK
        timestamp sent_at
        enum status
        timestamp created_at
        timestamp updated_at
        int booking_id FK
        int notification_id FK
    }

    %% ==========================================
    %% JUNCTION TABLES
    %% ==========================================

    vehicle_type_package {
        serial id PK
        int vehicle_type_id FK
        int package_id FK
    }

    package_add_on {
        serial id PK
        int package_id FK
        int add_on_id FK
    }

    booking_add_on {
        serial id PK
        int booking_id FK
        int add_on_id FK
    }

    %% ==========================================
    %% RELATIONSHIPS
    %% ==========================================

    %% Catalog relationships (M:N via junction tables)
    vehicle_type ||--o{ vehicle_type_package : "has packages"
    package ||--o{ vehicle_type_package : "available for"

    package ||--o{ package_add_on : "has add-ons"
    add_on ||--o{ package_add_on : "available in"

    %% Booking to catalog (N:1 direct FKs)
    booking }o--|| vehicle_type : "for vehicle"
    booking }o--|| package : "includes"
    booking }o--|| delivery_type : "delivery method"

    %% Booking to add-ons (M:N via junction)
    booking ||--o{ booking_add_on : "has"
    add_on ||--o{ booking_add_on : "selected in"

    %% Scheduling
    time_slot_template ||--o{ time_slot : "generates"
    booking |o--|| time_slot : "scheduled at"

    %% Notifications
    booking ||--o{ notification_log : "has"
    notification ||--o{ notification_log : "sent as"
```

---

## Entities Summary

| Category | Entity | Description |
|----------|--------|-------------|
| Core | `user` | Admin/Client accounts with role-based access |
| Core | `business_settings` | Business configuration (single row) |
| Catalog | `vehicle_type` | Car, Van, SUV with base pricing |
| Catalog | `package` | Main service packages (Full Wash, Exterior, Interior) |
| Catalog | `add_on` | Optional extras (Polishing, Waxing, etc.) |
| Catalog | `delivery_type` | Delivery options (Bring myself, Pickup) |
| Scheduling | `time_slot_template` | Reusable time slot definitions |
| Scheduling | `time_slot` | Actual bookable slots on specific dates |
| Transaction | `booking` | Customer reservation with all selections |
| Notification | `notification` | Email templates |
| Notification | `notification_log` | Sent notification audit trail |
| Junction | `vehicle_type_package` | Links vehicle types to available packages |
| Junction | `package_add_on` | Links packages to available add-ons |
| Junction | `booking_add_on` | Links bookings to selected add-ons |

---

## Pricing Formula

```
total_price = vehicle_type.base_price
            + package.price
            + SUM(add_on.price for each booking_add_on)
            + delivery_type.price
```

---

## ENUM Values

| Field | Values |
|-------|--------|
| `user.role` | ADMIN, CLIENT |
| `booking.status` | PENDING, CONFIRMED, COMPLETED, CANCELLED_BY_CUSTOMER, CANCELLED_BY_ADMIN |
| `time_slot.status` | AVAILABLE, BOOKED, BLOCKED |
| `notification.type` | BOOKING_CONFIRMATION, BOOKING_MODIFICATION, BOOKING_CANCELLATION |
| `notification_log.status` | PENDING, SENT, FAILED |

---

## Key Constraints

| Constraint | Implementation |
|------------|----------------|
| Exactly 1 package per booking | Direct FK: `booking.package_id` |
| 1 booking per time slot | UNIQUE on `booking.time_slot_id` |
| Delivery always required | NOT NULL on `booking.delivery_type_id` |
| Package-vehicle compatibility | Check via `vehicle_type_package` junction |
| Add-on availability | Check via `package_add_on` junction |
