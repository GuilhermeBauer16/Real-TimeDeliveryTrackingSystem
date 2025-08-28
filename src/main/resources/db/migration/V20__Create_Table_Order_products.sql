CREATE TABLE order_products
(
    order_id   CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES temporary_products (id) ON DELETE CASCADE
);

