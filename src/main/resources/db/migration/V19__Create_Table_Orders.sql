CREATE TABLE orders
(
    id          CHAR(36) NOT NULL,
    customer_id CHAR(36) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    order_status ENUM('PAYMENT_APPROVED', 'PAYMENT_REJECTED', 'CANCELED', 'DELIVERING', 'DELIVERED') NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE
);

