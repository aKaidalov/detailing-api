-- Detailing API Demo Data
-- Based on PRD (docs/prd/)
-- Run after schema.sql

-- =============================================
-- ADMIN USER
-- =============================================
-- Password: admin123 (BCrypt hash)
INSERT INTO "user" (email, password_hash, role, is_active, created_at, updated_at)
VALUES ('admin@detailing.ee', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'ADMIN', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =============================================
-- BUSINESS SETTINGS
-- =============================================
INSERT INTO business_settings (name, phone, email, address)
VALUES ('Detailing Tallinn', '+372 5555 1234', 'info@detailing.ee', 'Pärnu mnt 100, 11312 Tallinn, Estonia');

-- =============================================
-- VEHICLE TYPES
-- =============================================
INSERT INTO vehicle_type (name, base_price, description, is_deliverable, is_active, display_order, created_at, updated_at)
VALUES
    ('Car', 20.00, 'Standard passenger car', TRUE, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Van', 40.00, 'Van or minibus', TRUE, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =============================================
-- PACKAGES
-- =============================================
INSERT INTO package (name, description, price, is_active, display_order, created_at, updated_at)
VALUES
    ('Full Wash', 'Complete interior and exterior cleaning', 30.00, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Exterior Wash', 'Pre-soak, tar removal, hand wash, wheel wash, drying, door jamb, tire shine', 10.00, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Interior Cleaning', 'Floor mats, windows, surfaces, vents, vacuuming', 10.00, TRUE, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =============================================
-- ADD-ONS
-- =============================================
INSERT INTO add_on (name, description, price, is_active, display_order, created_at, updated_at)
VALUES
    ('Rust spot removal', 'Remove rust spots from body panels', 10.00, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Quick ceramic coating', 'Fast-apply ceramic protection', 15.00, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Body waxing', 'Full body wax treatment', 30.00, TRUE, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Polishing', 'Complete body polishing', 100.00, TRUE, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Car ceramic coating', 'Premium ceramic coating', 100.00, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Textile seat cleaning', 'Deep clean fabric seats', 50.00, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Leather seat care', 'Clean and condition leather seats', 50.00, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Odor removal with ozone', 'Ozone treatment for odor elimination', 35.00, TRUE, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Leather ceramic coating', 'Ceramic protection for leather', 100.00, TRUE, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Extra dirty vehicle surcharge', 'Additional charge for heavily soiled vehicles', 20.00, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =============================================
-- DELIVERY TYPES
-- =============================================
INSERT INTO delivery_type (name, price, requires_address, is_active)
VALUES
    ('I bring it myself', 0.00, FALSE, TRUE),
    ('We pick up the car', 15.00, TRUE, TRUE);

-- =============================================
-- TIME SLOT TEMPLATES
-- =============================================
INSERT INTO time_slot_template (start_time, end_time, is_active)
VALUES
    ('09:00', '11:00', TRUE),
    ('11:00', '13:00', TRUE),
    ('14:00', '16:00', TRUE),
    ('16:00', '18:00', TRUE);

-- =============================================
-- NOTIFICATION TEMPLATES
-- =============================================
INSERT INTO notification (type, subject, body, is_active)
VALUES
    ('BOOKING_CREATED',
     'Booking Received - #{bookingRef}',
     'Dear {clientName},

Thank you for your booking request!

Your booking is currently <strong>pending confirmation</strong>. We will review it and confirm shortly.

Booking Reference: #{bookingRef}
Requested Date: {date}
Requested Time: {time}
Services: {services}
Add-ons: {subServices}
Delivery: {deliveryOption}
Total Price: {totalPrice}

Location: {businessAddress}

To cancel your booking, <a href="{cancellationLink}">click here</a>.

Best regards,
{businessName}
{businessPhone}',
     TRUE),

    ('BOOKING_CONFIRMED',
     'Booking Confirmed - #{bookingRef}',
     'Dear {clientName},

Great news! Your booking has been <strong>confirmed</strong>.

Booking Reference: #{bookingRef}
Date: {date}
Time: {time}
Services: {services}
Add-ons: {subServices}
Delivery: {deliveryOption}
Total Price: {totalPrice}

Location: {businessAddress}

We look forward to seeing you!

To cancel your booking, <a href="{cancellationLink}">click here</a>.

Best regards,
{businessName}
{businessPhone}',
     TRUE),

    ('BOOKING_MODIFIED',
     'Booking Updated - #{bookingRef}',
     'Dear {clientName},

Your booking has been updated.

Booking Reference: #{bookingRef}
New Date: {date}
New Time: {time}
Services: {services}
Add-ons: {subServices}
Delivery: {deliveryOption}
New Total Price: {totalPrice}

Location: {businessAddress}

To cancel your booking, <a href="{cancellationLink}">click here</a>.

Best regards,
{businessName}
{businessPhone}',
     TRUE),

    ('BOOKING_COMPLETED',
     'Thank You - #{bookingRef}',
     'Dear {clientName},

Thank you for visiting {businessName}!

Your service has been completed.

Booking Reference: #{bookingRef}
Date: {date}
Services: {services}
Add-ons: {subServices}
Total Price: {totalPrice}

We hope you are satisfied with our service. To book again, <a href="{rebookingLink}">click here</a>.

Best regards,
{businessName}
{businessPhone}',
     TRUE),

    ('BOOKING_CANCELLED_BY_CUSTOMER',
     'Booking Cancelled - #{bookingRef}',
     'Dear {clientName},

Your booking has been cancelled as requested.

Booking Reference: #{bookingRef}
Original Date: {date}
Original Time: {time}

To make a new booking, <a href="{rebookingLink}">click here</a>.

Best regards,
{businessName}
{businessPhone}',
     TRUE),

    ('BOOKING_CANCELLED_BY_ADMIN',
     'Booking Cancelled - #{bookingRef}',
     'Dear {clientName},

Unfortunately, your booking has been cancelled by {businessName}.

Booking Reference: #{bookingRef}
Original Date: {date}
Original Time: {time}

We apologize for any inconvenience. Please contact us if you have any questions.

To make a new booking, <a href="{rebookingLink}">click here</a>.

Best regards,
{businessName}
{businessPhone}',
     TRUE);

-- =============================================
-- VEHICLE TYPE PACKAGE (vehicle_type <-> package links)
-- All packages available for all vehicle types
-- =============================================
-- Car (id=1) -> all packages
INSERT INTO vehicle_type_package (vehicle_type_id, package_id) VALUES (1, 1); -- Car -> Full Wash
INSERT INTO vehicle_type_package (vehicle_type_id, package_id) VALUES (1, 2); -- Car -> Exterior Wash
INSERT INTO vehicle_type_package (vehicle_type_id, package_id) VALUES (1, 3); -- Car -> Interior Cleaning
-- Van (id=2) -> all packages
INSERT INTO vehicle_type_package (vehicle_type_id, package_id) VALUES (2, 1); -- Van -> Full Wash
INSERT INTO vehicle_type_package (vehicle_type_id, package_id) VALUES (2, 2); -- Van -> Exterior Wash
INSERT INTO vehicle_type_package (vehicle_type_id, package_id) VALUES (2, 3); -- Van -> Interior Cleaning

-- =============================================
-- PACKAGE ADD-ON (package <-> add_on links)
-- Based on PRD: which add-ons are available for which packages
-- =============================================
-- Full Wash (id=1) -> all add-ons (1-10)
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 1);  -- Rust spot removal
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 2);  -- Quick ceramic coating
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 3);  -- Body waxing
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 4);  -- Polishing
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 5);  -- Car ceramic coating
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 6);  -- Textile seat cleaning
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 7);  -- Leather seat care
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 8);  -- Odor removal with ozone
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 9);  -- Leather ceramic coating
INSERT INTO package_add_on (package_id, add_on_id) VALUES (1, 10); -- Extra dirty vehicle surcharge

-- Exterior Wash (id=2) -> exterior-related add-ons
INSERT INTO package_add_on (package_id, add_on_id) VALUES (2, 1);  -- Rust spot removal
INSERT INTO package_add_on (package_id, add_on_id) VALUES (2, 2);  -- Quick ceramic coating
INSERT INTO package_add_on (package_id, add_on_id) VALUES (2, 3);  -- Body waxing
INSERT INTO package_add_on (package_id, add_on_id) VALUES (2, 4);  -- Polishing
INSERT INTO package_add_on (package_id, add_on_id) VALUES (2, 5);  -- Car ceramic coating
INSERT INTO package_add_on (package_id, add_on_id) VALUES (2, 10); -- Extra dirty vehicle surcharge

-- Interior Cleaning (id=3) -> interior-related add-ons
INSERT INTO package_add_on (package_id, add_on_id) VALUES (3, 6);  -- Textile seat cleaning
INSERT INTO package_add_on (package_id, add_on_id) VALUES (3, 7);  -- Leather seat care
INSERT INTO package_add_on (package_id, add_on_id) VALUES (3, 8);  -- Odor removal with ozone
INSERT INTO package_add_on (package_id, add_on_id) VALUES (3, 9);  -- Leather ceramic coating
INSERT INTO package_add_on (package_id, add_on_id) VALUES (3, 10); -- Extra dirty vehicle surcharge

-- =============================================
-- TIME SLOTS (for testing)
-- =============================================
-- Using dates in January 2025
INSERT INTO time_slot (date, status, time_slot_template_id, created_at, updated_at)
VALUES
    ('2026-01-15', 'BOOKED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=1, 09:00-11:00, booked
    ('2026-01-15', 'AVAILABLE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- id=2, 11:00-13:00, available
    ('2026-01-16', 'BOOKED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=3, 09:00-11:00, booked
    ('2026-01-16', 'BOOKED', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=4, 14:00-16:00, booked
    ('2026-01-17', 'AVAILABLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- id=5, 09:00-11:00, available

-- =============================================
-- BOOKINGS (for testing)
-- =============================================
-- Booking 1: Car + Full Wash + Self delivery = 20 + 30 + 0 = 50
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20250115-001', 'John', 'Smith', 'john.smith@example.com', '+372 5551 1111', '123ABC', 50.00, 'Please wash carefully', NULL, 'CONFIRMED', 1, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 2: Van + Exterior Wash + Pickup = 40 + 10 + 15 = 65
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20250116-001', 'Maria', 'Johnson', 'maria.j@example.com', '+372 5552 2222', '456DEF', 65.00, NULL, 'Tartu mnt 50, Tallinn', 'PENDING', 2, 2, 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 3: Car + Interior Cleaning + Self delivery + Add-ons (Textile seat cleaning + Odor removal) = 20 + 10 + 0 + 50 + 35 = 115
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20250116-002', 'Peeter', 'Tamm', 'peeter.tamm@example.com', '+372 5553 3333', '789GHI', 115.00, 'Dog was in the car, needs deep cleaning', NULL, 'CONFIRMED', 1, 3, 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =============================================
-- BOOKING ADD-ONS (for booking 3)
-- =============================================
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (3, 6);  -- Textile seat cleaning
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (3, 8);  -- Odor removal with ozone
