-- Insert demo vehicle types
INSERT INTO vehicle_type (type_name, created_at, updated_at)
VALUES
    ('Van', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Car', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Motorcycle', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- Insert demo vehicles (linking to vehicle_type by id)
INSERT INTO vehicle (vehicle_type_id, licence_plate, vin_code, created_at, updated_at)
VALUES
    (1, '123ABC', '1HGCM82633A004352', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Car
    (2, '456XYZ', '1FTFW1ET1EKF51234', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- SUV
    (3, '789MNO', '2HGFA165X8H123456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Motorcycle
    (1, '555BBB', '3VWFE21C04M000123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Car
    (2, '777CCC', '5YJSA1E26HF000678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- SUV


-- Insert demo parent services
INSERT INTO service_item (parent_service_item_id, service_item_name, description, price, created_at, updated_at)
VALUES
    (NULL, 'Interior', 'Parent category: interior services', 20.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (NULL, 'Exterior', 'Parent category: exterior services', 30.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (NULL, 'Full Service', 'Parent category: bundles & extras', 50.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert demo child services (linking to parent by id)
-- Assume: Interior = id=1, Exterior = id=2, Full Service = id=3
INSERT INTO service_item (parent_service_item_id, service_item_name, description, price, created_at, updated_at)
VALUES
    (1, 'Deep clean fabric seats', 'Add-on for fabric seats', 50.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 'Leather seat maintenance', 'Leather clean & condition', 50.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 'Ozone odor removal', 'Ozone treatment for odors', 35.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    (2, 'Iron contamination removal', 'Decon for ferrous fallout', 10.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Ceramic quick detailing', 'Spray ceramic topper', 15.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Wax coating (up to 6 mo protection)', 'Hybrid wax/sealant; up to ~6 months', 30.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Machine polishing', 'Paint enhancement; price from €150', 150.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Headlight restoration (each)', 'Per headlight unit', 20.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Full ceramic coating', 'Professional ceramic coating; price from €150', 150.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    (3, 'Airport valeting', 'Full service valet; price from €50', 50.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'Extra dirty vehicle surcharge', 'Surcharge up to €20 added on top of the selected package', 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
