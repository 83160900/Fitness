-- Limpa e insere o usuario admin na tabela users
INSERT INTO users (id, email, password, name, role, active, lgpd_consent)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin', 'admin', 'Administrador', 'ADMIN', true, true)
ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password, name = EXCLUDED.name, role = EXCLUDED.role, active = EXCLUDED.active;

-- Inserir um Personal de teste
INSERT INTO users (id, email, password, name, role, active, specialty, formation, experience, lgpd_consent)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'personal@teste.com', '1234', 'Marcio Personal', 'PERSONAL', true, 'Musculacao e Hipertrofia', 'Educacao Fisica - USP', '10 anos de experiencia em academias', true)
ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password, name = EXCLUDED.name, specialty = EXCLUDED.specialty;

-- Inserir um Profissional de Nutricao
INSERT INTO users (id, email, password, name, role, active, specialty, formation, experience, lgpd_consent)
VALUES ('550e8400-e29b-41d4-a716-446655440005', 'nutro@teste.com', '1234', 'Dra. Ana Nutri', 'NUTROLOGO', true, 'Nutricao Esportiva', 'Nutricao - UNICAMP', '5 anos com atletas de elite', true)
ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password, name = EXCLUDED.name;

-- Inserir Alunos
INSERT INTO users (id, email, password, name, role, active, photo_url, lgpd_consent)
VALUES 
('550e8400-e29b-41d4-a716-446655440002', 'aluno1@teste.com', '1234', 'Joao Silva', 'ALUNO', true, 'https://i.pravatar.cc/150?u=aluno1', true),
('550e8400-e29b-41d4-a716-446655440003', 'aluno2@teste.com', '1234', 'Maria Souza', 'ALUNO', true, 'https://i.pravatar.cc/150?u=aluno2', true),
('550e8400-e29b-41d4-a716-446655440004', 'aluno3@teste.com', '1234', 'Carlos Oliveira', 'ALUNO', true, 'https://i.pravatar.cc/150?u=aluno3', true)
ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password;

-- Vincular alunos aos profissionais (Tabela user_professionals)
-- Joao Silva (0002) vinculado ao Marcio Personal (0001) e Ana Nutri (0005)
INSERT INTO user_professionals (student_id, professional_id) VALUES 
('550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440005'),
('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001')
ON CONFLICT DO NOTHING;
