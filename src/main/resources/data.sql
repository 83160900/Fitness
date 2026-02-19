-- Inserir usuÃ¡rios Mocked (Credenciais de acesso imediato)
-- Personal: personal@teste.com / senha: password123
INSERT INTO public.users (id, email, password, name, role, active, lgpd_consent)
SELECT 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'personal@teste.com', 'password123', 'Professor Teste', 'PERSONAL', true, true
WHERE NOT EXISTS (SELECT 1 FROM public.users WHERE email = 'personal@teste.com');

-- Aluno: aluno@teste.com / senha: password123
INSERT INTO public.users (id, email, password, name, role, active, lgpd_consent)
SELECT 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'aluno@teste.com', 'password123', 'Aluno Teste', 'ALUNO', true, true
WHERE NOT EXISTS (SELECT 1 FROM public.users WHERE email = 'aluno@teste.com');
