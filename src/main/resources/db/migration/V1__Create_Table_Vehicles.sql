CREATE TABLE vehicles
(
    id           CHAR(36)                                                      NOT NULL,
    license_plate VARCHAR(20)                                                   NOT NULL UNIQUE,
    type         ENUM ('CAR', 'TRUCK', 'VAN', 'MOTORCYCLE')                    NOT NULL,
    status       ENUM ('AVAILABLE', 'IN_USE', 'MAINTENANCE', 'OUT_OF_SERVICE') NOT NULL,

    PRIMARY KEY (id)
);
