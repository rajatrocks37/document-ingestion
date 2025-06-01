-- src/main/resources/data.sql

-- Insert a default user with encrypted password
INSERT INTO USERS (username, password, role, enabled)
VALUES (
    'admin',
    '$2a$10$omIhqsv.rGmJ/GSCCX5dTeuNpwFwjjITn3jNlgsASxk7.lvtBV1nu', -- bcrypt for 'admin123'
    'ADMIN',
    true
)
ON CONFLICT (username) DO NOTHING;
