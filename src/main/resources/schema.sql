DROP TABLE IF EXISTS vehicle_type;
DROP TABLE IF EXISTS vehicle;

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
        ON UPDATE CASCADE

    -- Ensure at least one of licence_plate or vin_code is provided
    CONSTRAINT licence_or_vin CHECK (
        licence_plate IS NOT NULL OR vin_code IS NOT NULL
    )
);

