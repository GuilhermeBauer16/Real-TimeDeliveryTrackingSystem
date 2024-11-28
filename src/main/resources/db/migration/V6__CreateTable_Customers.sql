CREATE TABLE customers
(
    id           CHAR(36)    NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    user_id      CHAR(36)    NOT NULL,
    PRIMARY KEY (id),

    FOREIGN KEY (user_id) REFERENCES users (id)
);


