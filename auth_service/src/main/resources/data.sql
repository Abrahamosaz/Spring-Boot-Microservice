CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- create user table if it does not exists
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) DEFAULT 'USER',
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);


-- This is a sample bcrypt hash of 'password123' with cost factor 10
INSERT INTO users (
    name,
    email,
    password,
    role,
    created_at,
    updated_at
)
VALUES (
    'Admin User',
    'admin@example.com',
    '$2a$12$Ncm68WzsI.0WKq9/PcwuWuNkKSpTrw7L.RgXZWS/p/.RMCCAGfj0K', --password (password123)
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;