INSERT INTO users (

    id,
    name,
    email,
    password,
    user_profile,
    verify_code,
    authenticated,
    code_expiration
) VALUES (
             '550e8400-e29b-41d4-a716-446655440000',
             'admin',
             'admin@example.com',
             '$2a$10$MsSkaaTj/ZiOB4rKTuZ3Au4GMwaV6OIS07Va2abgcklDeoyrZkpRa',
             'ROLE_ADMIN',
             '123456',
             TRUE,
             '2025-01-20 12:00:00'
         );
