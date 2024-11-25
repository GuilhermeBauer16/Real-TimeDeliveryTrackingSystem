CREATE TABLE users (
    id char(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_profile ENUM('CUSTOMER','DRIVER', 'ADMIN') NOT NULL,
    PRIMARY KEY(id)
);


