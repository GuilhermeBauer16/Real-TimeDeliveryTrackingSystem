CREATE TABLE drivers
(
    id             CHAR(36)    NOT NULL,
    driver_license VARCHAR(20) NOT NULL,
    phone_number   VARCHAR(15) NOT NULL,
    user_id        CHAR(36)    NOT NULL,
    PRIMARY KEY (id),

    FOREIGN KEY (user_id) REFERENCES users (id)
);


