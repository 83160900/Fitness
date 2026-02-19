-- Limpa e insere o usuario admin na tabela users
INSERT INTO users (id, email, password, name, role, active)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'admin', 'admin', 'Administrador', 'ADMIN', true)
ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password, name = EXCLUDED.name, role = EXCLUDED.role, active = EXCLUDED.active;

-- Inserir um Personal de teste
INSERT INTO users (id, email, password, name, role, active, specialty, formation, experience)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'personal@teste.com', '1234', 'Marcio Personal', 'PERSONAL', true, 'Musculacao e Hipertrofia', 'Educacao Fisica - USP', '10 anos de experiencia em academias')
ON CONFLICT (email) DO UPDATE SET name = EXCLUDED.name, specialty = EXCLUDED.specialty;

-- Inserir Alunos vinculados ao Marcio Personal
INSERT INTO users (id, email, password, name, role, active, coach_id, photo_url)
VALUES 
('550e8400-e29b-41d4-a716-446655440002', 'aluno1@teste.com', '1234', 'Joao Silva', 'ALUNO', true, '550e8400-e29b-41d4-a716-446655440001', 'https://i.pravatar.cc/150?u=aluno1'),
('550e8400-e29b-41d4-a716-446655440003', 'aluno2@teste.com', '1234', 'Maria Souza', 'ALUNO', true, '550e8400-e29b-41d4-a716-446655440001', 'https://i.pravatar.cc/150?u=aluno2'),
('550e8400-e29b-41d4-a716-446655440004', 'aluno3@teste.com', '1234', 'Carlos Oliveira', 'ALUNO', true, '550e8400-e29b-41d4-a716-446655440001', 'https://i.pravatar.cc/150?u=aluno3')
ON CONFLICT (email) DO NOTHING;
