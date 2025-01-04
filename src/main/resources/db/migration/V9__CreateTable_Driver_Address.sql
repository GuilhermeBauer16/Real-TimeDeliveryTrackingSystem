CREATE TABLE driver_address
(
    driver_id  CHAR(36) NOT NULL,
    address_id CHAR(36) NOT NULL,
    PRIMARY KEY (driver_id, address_id),
    FOREIGN KEY (driver_id) REFERENCES drivers (id),
    FOREIGN KEY (address_id) REFERENCES addresses (id)
);



