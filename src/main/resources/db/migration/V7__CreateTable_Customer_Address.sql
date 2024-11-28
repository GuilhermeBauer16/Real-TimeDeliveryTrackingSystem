

CREATE TABLE customers_address
(
    customer_id CHAR(36) NOT NULL,
    address_id  CHAR(36) NOT NULL,
    PRIMARY KEY (customer_id, address_id),
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (address_id) REFERENCES addresses (id)
);



