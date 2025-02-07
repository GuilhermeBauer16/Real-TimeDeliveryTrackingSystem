CREATE TABLE temporary_products
(
    id          CHAR(36)       NOT NULL,
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    quantity    int NOT NULL ,
        PRIMARY KEY (id)
);




