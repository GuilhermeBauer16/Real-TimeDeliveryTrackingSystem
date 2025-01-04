CREATE TABLE driver_vehicles
(
    driver_id  CHAR(36) NOT NULL,
    vehicle_id CHAR(36) NOT NULL,
    PRIMARY KEY (driver_id, vehicle_id),
    FOREIGN KEY (driver_id) REFERENCES drivers (id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles (id)
);



