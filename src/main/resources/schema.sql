-- Detailing API Database Schema
-- Based on ERD v4 (docs/erd/v4/erd.md)
-- PostgreSQL 16+

-- =============================================
-- DROP EXISTING OBJECTS (for clean rebuild)
-- =============================================

DROP TABLE IF EXISTS notification_log CASCADE;
DROP TABLE IF EXISTS notification CASCADE;
DROP TABLE IF EXISTS booking_add_on CASCADE;
DROP TABLE IF EXISTS package_add_on CASCADE;
DROP TABLE IF EXISTS vehicle_type_package CASCADE;
DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS time_slot CASCADE;
DROP TABLE IF EXISTS time_slot_template CASCADE;
DROP TABLE IF EXISTS delivery_type CASCADE;
DROP TABLE IF EXISTS add_on CASCADE;
DROP TABLE IF EXISTS package CASCADE;
DROP TABLE IF EXISTS vehicle_type CASCADE;
DROP TABLE IF EXISTS business_settings CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;

DROP TYPE IF EXISTS notification_log_status CASCADE;
DROP TYPE IF EXISTS notification_type CASCADE;
DROP TYPE IF EXISTS time_slot_status CASCADE;
DROP TYPE IF EXISTS booking_status CASCADE;
DROP TYPE IF EXISTS user_role CASCADE;

-- =============================================
-- ENUM TYPES
-- =============================================

CREATE TYPE user_role AS ENUM ('ADMIN', 'CLIENT');

CREATE TYPE booking_status AS ENUM (
    'PENDING',
    'CONFIRMED',
    'COMPLETED',
    'CANCELLED_BY_CUSTOMER',
    'CANCELLED_BY_ADMIN'
);

CREATE TYPE time_slot_status AS ENUM ('AVAILABLE', 'BOOKED', 'BLOCKED');

CREATE TYPE notification_type AS ENUM (
    'BOOKING_CONFIRMATION',
    'BOOKING_MODIFICATION',
    'BOOKING_CANCELLATION'
);

CREATE TYPE notification_log_status AS ENUM ('PENDING', 'SENT', 'FAILED');

-- =============================================
-- CORE ENTITIES
-- =============================================

-- User: Admin accounts (CLIENT role for future use)
CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Business Settings: Single row configuration
CREATE TABLE business_settings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address TEXT NOT NULL
);

-- =============================================
-- CATALOG ENTITIES
-- =============================================

-- Vehicle Type: Car, Van, SUV, etc.
CREATE TABLE vehicle_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    description TEXT,
    is_deliverable BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Package: Full Wash, Exterior Wash, Interior Cleaning, etc.
CREATE TABLE package (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add-on: Optional extras (Polishing, Waxing, etc.)
CREATE TABLE add_on (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Delivery Type: Drop-off, Pickup
CREATE TABLE delivery_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL DEFAULT 0,
    requires_address BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- =============================================
-- SCHEDULING ENTITIES
-- =============================================

-- Time Slot Template: Reusable time patterns
CREATE TABLE time_slot_template (
    id SERIAL PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Time Slot: Actual bookable slots on specific dates
CREATE TABLE time_slot (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    status time_slot_status NOT NULL DEFAULT 'AVAILABLE',
    time_slot_template_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_time_slot_template
        FOREIGN KEY (time_slot_template_id)
        REFERENCES time_slot_template(id)
        ON DELETE CASCADE
);

-- =============================================
-- TRANSACTION ENTITY
-- =============================================

-- Booking: Customer reservations
CREATE TABLE booking (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    vehicle_reg_number VARCHAR(20) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    notes TEXT,
    address TEXT,
    status booking_status NOT NULL DEFAULT 'PENDING',
    vehicle_type_id INT,
    package_id INT,
    time_slot_id INT,
    delivery_type_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_vehicle_type
        FOREIGN KEY (vehicle_type_id)
        REFERENCES vehicle_type(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_booking_package
        FOREIGN KEY (package_id)
        REFERENCES package(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_booking_time_slot
        FOREIGN KEY (time_slot_id)
        REFERENCES time_slot(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_booking_delivery_type
        FOREIGN KEY (delivery_type_id)
        REFERENCES delivery_type(id)
        ON DELETE CASCADE
);

-- =============================================
-- JUNCTION TABLES
-- =============================================

-- Vehicle Type Package: Links vehicle types to available packages
CREATE TABLE vehicle_type_package (
    id SERIAL PRIMARY KEY,
    vehicle_type_id INT NOT NULL,
    package_id INT NOT NULL,
    CONSTRAINT fk_vehicle_type_package_vehicle_type
        FOREIGN KEY (vehicle_type_id)
        REFERENCES vehicle_type(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_vehicle_type_package_package
        FOREIGN KEY (package_id)
        REFERENCES package(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_vehicle_type_package UNIQUE (vehicle_type_id, package_id)
);

-- Package Add-on: Links packages to available add-ons
CREATE TABLE package_add_on (
    id SERIAL PRIMARY KEY,
    package_id INT NOT NULL,
    add_on_id INT NOT NULL,
    CONSTRAINT fk_package_add_on_package
        FOREIGN KEY (package_id)
        REFERENCES package(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_package_add_on_add_on
        FOREIGN KEY (add_on_id)
        REFERENCES add_on(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_package_add_on UNIQUE (package_id, add_on_id)
);

-- Booking Add-on: Links bookings to selected add-ons
CREATE TABLE booking_add_on (
    id SERIAL PRIMARY KEY,
    booking_id INT NOT NULL,
    add_on_id INT NOT NULL,
    CONSTRAINT fk_booking_add_on_booking
        FOREIGN KEY (booking_id)
        REFERENCES booking(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_booking_add_on_add_on
        FOREIGN KEY (add_on_id)
        REFERENCES add_on(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_booking_add_on UNIQUE (booking_id, add_on_id)
);

-- =============================================
-- NOTIFICATION ENTITIES
-- =============================================

-- Notification: Email templates
CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    type notification_type NOT NULL UNIQUE,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Notification Log: Sent emails audit trail
CREATE TABLE notification_log (
    id SERIAL PRIMARY KEY,
    sent_at TIMESTAMP,
    status notification_log_status NOT NULL DEFAULT 'PENDING',
    booking_id INT NOT NULL,
    notification_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_log_booking
        FOREIGN KEY (booking_id)
        REFERENCES booking(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_notification_log_notification
        FOREIGN KEY (notification_id)
        REFERENCES notification(id)
        ON DELETE CASCADE
);

-- =============================================
-- INDEXES
-- =============================================

CREATE UNIQUE INDEX idx_user_email ON "user"(email);
CREATE INDEX idx_booking_reference ON booking(reference);
CREATE INDEX idx_booking_status ON booking(status);
CREATE INDEX idx_time_slot_date ON time_slot(date);
CREATE INDEX idx_notification_log_status ON notification_log(status);

-- Partial unique index: only one active booking per time slot
-- Allows cancelled/completed bookings to keep time_slot reference for email history
CREATE UNIQUE INDEX idx_booking_time_slot_active ON booking (time_slot_id)
    WHERE time_slot_id IS NOT NULL
    AND status NOT IN ('CANCELLED_BY_CUSTOMER', 'CANCELLED_BY_ADMIN', 'COMPLETED');
