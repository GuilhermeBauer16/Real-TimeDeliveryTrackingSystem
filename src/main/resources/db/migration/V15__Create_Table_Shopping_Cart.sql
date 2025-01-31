CREATE TABLE shopping_carts
(
    id          CHAR(36) NOT NULL,
    customer_id VARCHAR(255),
    total_price DOUBLE,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);



