INSERT INTO users (id, email, password, name, role, active) VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin', 'admin', 'Administrador', 'ADMIN', true) ON CONFLICT DO NOTHING;
