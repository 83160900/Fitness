-- Limpa e insere o usuario admin na tabela users
INSERT INTO users (id, email, password, name, role, active)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin', 'admin', 'Administrador', 'ADMIN', true)
ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password, name = EXCLUDED.name, role = EXCLUDED.role, active = EXCLUDED.active;

-- Inserir um Personal de teste
INSERT INTO users (id, email, password, name, role, active, specialty, formation, experience)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'personal@teste.com', '1234', 'Marcio Personal', 'PERSONAL', true, 'Musculacao e Hipertrofia', 'Educacao Fisica - USP', '10 anos de experiencia em academias')
ON CONFLICT (email) DO UPDATE SET name = EXCLUDED.name, specialty = EXCLUDED.specialty;

-- Inserir um Profissional de Nutricao
INSERT INTO users (id, email, password, name, role, active, specialty, formation, experience)
VALUES ('550e8400-e29b-41d4-a716-446655440005', 'nutro@teste.com', '1234', 'Dra. Ana Nutri', 'NUTROLOGO', true, 'Nutricao Esportiva', 'Nutricao - UNICAMP', '5 anos com atletas de elite')
ON CONFLICT (email) DO UPDATE SET name = EXCLUDED.name;

-- Vincular alunos aos profissionais (Tabela user_professionals)
-- Joao Silva (0002) vinculado ao Marcio Personal (0001) e Ana Nutri (0005)
INSERT INTO user_professionals (student_id, professional_id) VALUES 
('550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440005'),
('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001')
ON CONFLICT DO NOTHING;
