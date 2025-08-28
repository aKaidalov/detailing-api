DROP TABLE IF EXISTS vehicle;
DROP TABLE IF EXISTS vehicle_type;
DROP TABLE IF EXISTS service_item;
DROP TABLE IF EXISTS time_slot;

CREATE TABLE IF NOT EXISTS vehicle_type (
    id SERIAL PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vehicle (
    id SERIAL PRIMARY KEY,
    vehicle_type_id INT NOT NULL,
    licence_plate VARCHAR(20) UNIQUE,
    vin_code VARCHAR(17) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vehicle_type
        FOREIGN KEY (vehicle_type_id)
        REFERENCES vehicle_type (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    -- Ensure at least one of licence_plate or vin_code is provided
    CONSTRAINT licence_or_vin CHECK (
        licence_plate IS NOT NULL OR vin_code IS NOT NULL
    )
);

CREATE TABLE IF NOT EXISTS service_item (
    id SERIAL PRIMARY KEY,
    parent_service_item_id INT,
    service_item_name VARCHAR(150) UNIQUE NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_service_item_parent
        FOREIGN KEY (parent_service_item_id)
        REFERENCES service_item (id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS time_slot (
    id SERIAL PRIMARY KEY,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    max_bookings INT NOT NULL DEFAULT 1 CHECK (max_bookings > 0)
);

