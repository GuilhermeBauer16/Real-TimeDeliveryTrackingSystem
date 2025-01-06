ALTER TABLE users
    ADD COLUMN verify_code VARCHAR(6),
    ADD COLUMN authenticated BOOLEAN DEFAULT FALSE,
    ADD COLUMN code_expiration DATETIME;



