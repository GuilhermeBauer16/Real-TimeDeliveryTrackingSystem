CREATE TABLE addresses
(
    id          CHAR(36)     NOT NULL,
    street      VARCHAR(255) NOT NULL,
    city        VARCHAR(100) NOT NULL,
    state       VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20)  NOT NULL,
    country     VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)

);


