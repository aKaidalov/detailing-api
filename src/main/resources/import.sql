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
INSERT INTO vehicle_type (name, icon, base_price, description, is_deliverable, is_active, display_order, created_at, updated_at)
VALUES
    ('Car', '🚗', 20.00, 'Standard passenger car', TRUE, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Van', '🚐', 40.00, 'Van or minibus', TRUE, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
INSERT INTO delivery_type (name, icon, price, requires_address, is_active)
VALUES
    ('I bring it myself', '👤', 0.00, FALSE, TRUE),
    ('We pick up the car', '🚚', 15.00, TRUE, TRUE);

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

-- =============================================
-- ADDITIONAL TIME SLOTS (Jan 20-23, 2026 for testing)
-- =============================================
-- Jan 20
INSERT INTO time_slot (date, status, time_slot_template_id, created_at, updated_at)
VALUES
    ('2026-01-20', 'BOOKED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=6, 09:00-11:00
    ('2026-01-20', 'BOOKED', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=7, 11:00-13:00
    ('2026-01-20', 'BOOKED', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=8, 14:00-16:00
    ('2026-01-20', 'AVAILABLE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- id=9, 16:00-18:00

-- Jan 21
INSERT INTO time_slot (date, status, time_slot_template_id, created_at, updated_at)
VALUES
    ('2026-01-21', 'BOOKED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=10, 09:00-11:00
    ('2026-01-21', 'BOOKED', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=11, 11:00-13:00
    ('2026-01-21', 'BOOKED', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=12, 14:00-16:00
    ('2026-01-21', 'BOOKED', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);    -- id=13, 16:00-18:00

-- Jan 22
INSERT INTO time_slot (date, status, time_slot_template_id, created_at, updated_at)
VALUES
    ('2026-01-22', 'BOOKED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=14, 09:00-11:00
    ('2026-01-22', 'BOOKED', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=15, 11:00-13:00
    ('2026-01-22', 'AVAILABLE', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- id=16, 14:00-16:00
    ('2026-01-22', 'AVAILABLE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- id=17, 16:00-18:00

-- Jan 23
INSERT INTO time_slot (date, status, time_slot_template_id, created_at, updated_at)
VALUES
    ('2026-01-23', 'BOOKED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=18, 09:00-11:00
    ('2026-01-23', 'BOOKED', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),    -- id=19, 11:00-13:00
    ('2026-01-23', 'AVAILABLE', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- id=20, 14:00-16:00
    ('2026-01-23', 'AVAILABLE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- id=21, 16:00-18:00

-- =============================================
-- ADDITIONAL BOOKINGS (for pagination testing)
-- =============================================
-- Booking 4: Jan 20, 09:00 - CONFIRMED
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260120-001', 'Anna', 'Kask', 'anna.kask@example.com', '+372 5554 4444', '111AAA', 60.00, NULL, NULL, 'CONFIRMED', 1, 1, 6, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 5: Jan 20, 11:00 - PENDING
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260120-002', 'Mikk', 'Rebane', 'mikk.rebane@example.com', '+372 5555 5555', '222BBB', 75.00, 'Please call before arrival', 'Viru väljak 4, Tallinn', 'PENDING', 2, 1, 7, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 6: Jan 20, 14:00 - COMPLETED
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260120-003', 'Laura', 'Saar', 'laura.saar@example.com', '+372 5556 6666', '333CCC', 30.00, NULL, NULL, 'COMPLETED', 1, 2, 8, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 7: Jan 21, 09:00 - CANCELLED_BY_CUSTOMER
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260121-001', 'Tõnis', 'Mets', 'tonis.mets@example.com', '+372 5557 7777', '444DDD', 50.00, 'Cancelled due to travel', NULL, 'CANCELLED_BY_CUSTOMER', 1, 1, 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 8: Jan 21, 11:00 - CONFIRMED
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260121-002', 'Kristina', 'Lepp', 'kristina.lepp@example.com', '+372 5558 8888', '555EEE', 95.00, 'New car, first wash', NULL, 'CONFIRMED', 1, 1, 11, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 9: Jan 21, 14:00 - PENDING
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260121-003', 'Andres', 'Kuusk', 'andres.kuusk@example.com', '+372 5559 9999', '666FFF', 65.00, NULL, 'Lasnamäe 25, Tallinn', 'PENDING', 2, 2, 12, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 10: Jan 21, 16:00 - CONFIRMED
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260121-004', 'Liisa', 'Paju', 'liisa.paju@example.com', '+372 5560 0000', '777GGG', 40.00, NULL, NULL, 'CONFIRMED', 1, 3, 13, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 11: Jan 22, 09:00 - CANCELLED_BY_ADMIN
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260122-001', 'Martin', 'Vaher', 'martin.vaher@example.com', '+372 5561 1111', '888HHH', 50.00, 'Cancelled - slot unavailable', NULL, 'CANCELLED_BY_ADMIN', 1, 1, 14, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 12: Jan 22, 11:00 - COMPLETED
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260122-002', 'Kadri', 'Org', 'kadri.org@example.com', '+372 5562 2222', '999III', 85.00, 'Regular customer', NULL, 'COMPLETED', 2, 1, 15, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 13: Jan 23, 09:00 - PENDING
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260123-001', 'Jaan', 'Sepp', 'jaan.sepp@example.com', '+372 5563 3333', '101JJJ', 70.00, NULL, 'Mustamäe tee 10, Tallinn', 'PENDING', 1, 1, 18, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking 14: Jan 23, 11:00 - CONFIRMED
INSERT INTO booking (reference, first_name, last_name, email, phone, vehicle_reg_number, total_price, notes, address, status, vehicle_type_id, package_id, time_slot_id, delivery_type_id, created_at, updated_at)
VALUES ('DET-20260123-002', 'Mari', 'Lill', 'mari.lill@example.com', '+372 5564 4444', '102KKK', 180.00, 'Full detail with ceramic coating', NULL, 'CONFIRMED', 1, 1, 19, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =============================================
-- ADDITIONAL BOOKING ADD-ONS
-- =============================================
-- Booking 8: Quick ceramic coating + Body waxing
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (8, 2);
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (8, 3);

-- Booking 12: Textile seat cleaning + Leather seat care
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (12, 6);
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (12, 7);

-- Booking 14: Polishing + Car ceramic coating + Body waxing
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (14, 3);
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (14, 4);
INSERT INTO booking_add_on (booking_id, add_on_id) VALUES (14, 5);
