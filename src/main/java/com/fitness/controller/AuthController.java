package com.fitness.controller;

import com.fitness.dto.LoginRequest;
import com.fitness.dto.LoginResponse;
import com.fitness.dto.RegisterRequest;
import com.fitness.domain.model.User;
import com.fitness.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
            String password = request.getPassword() != null ? request.getPassword().trim() : "";
            
            System.out.println("[DEBUG_LOG] Login: Tentativa para '" + email + "'");
            
            return userRepository.findByEmail(email)
                    .map(user -> {
                        try {
                            String storedPassword = user.getPassword() != null ? user.getPassword().trim() : "";
                            System.out.println("[DEBUG_LOG] Login: Usuario '" + email + "' encontrado no banco.");
                            System.out.println("[DEBUG_LOG] Login: Role=" + user.getRole() + ", Active=" + user.isActive());
                            
                            if (storedPassword.equals(password)) {
                                System.out.println("[DEBUG_LOG] Login: Senha correta para " + email);
                                return ResponseEntity.ok(LoginResponse.builder()
                                        .name(user.getName())
                                        .email(user.getEmail())
                                        .role(user.getRole())
                                        .specialty(user.getSpecialty())
                                        .registrationNumber(user.getRegistrationNumber())
                                        .photoUrl(user.getPhotoUrl())
                                        .message("Login realizado com sucesso!")
                                        .build());
                            } else {
                                System.out.println("[DEBUG_LOG] Login: Senha incorreta para '" + email + "'.");
                                return ResponseEntity.status(401).body("Credenciais inválidas!");
                            }
                        } catch (Exception e) {
                            System.err.println("[DEBUG_LOG] Login: Erro interno ao processar usuario - " + e.getMessage());
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    })
                    .orElseGet(() -> {
                        System.out.println("[DEBUG_LOG] Login: Usuario '" + email + "' NAO encontrado no banco.");
                        return ResponseEntity.status(401).body("Credenciais inválidas!");
                    });
        } catch (Exception e) {
            System.err.println("[DEBUG_LOG] Login: ERRO CRITICO - " + e.getClass().getName() + " : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro Interno no Servidor: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("[DEBUG_LOG] Cadastro: Início da tentativa para '" + request.getEmail() + "'");
        
        try {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body("E-mail é obrigatório");
            }

            String cleanEmail = request.getEmail().trim().toLowerCase();
            System.out.println("[DEBUG_LOG] Cadastro: Verificando existência de '" + cleanEmail + "'");
            
            if (userRepository.findByEmail(cleanEmail).isPresent()) {
                System.out.println("[DEBUG_LOG] Cadastro: E-mail '" + cleanEmail + "' já existe.");
                return ResponseEntity.badRequest().body("E-mail já cadastrado!");
            }

            System.out.println("[DEBUG_LOG] Cadastro: Criando objeto User...");
            User user = new User();
            user.setName(request.getName());
            user.setEmail(cleanEmail);
            user.setPassword(request.getPassword());
            user.setRole(request.getRole() != null ? request.getRole() : com.fitness.domain.enums.UserRole.ALUNO);
            user.setSpecialty(request.getSpecialty());
            user.setRegistrationNumber(request.getRegistrationNumber());
            user.setFormation(request.getFormation());
            user.setExperience(request.getExperience());
            user.setActive(true);
            user.setLgpdConsent(true);

            System.out.println("[DEBUG_LOG] Cadastro: Chamando userRepository.save()...");
            userRepository.save(user);
            System.out.println("[DEBUG_LOG] Cadastro: Usuário '" + cleanEmail + "' salvo com sucesso no banco!");

            return ResponseEntity.ok("Usuário registrado com sucesso!");
        } catch (org.springframework.dao.DataAccessException e) {
            System.err.println("[DEBUG_LOG] Cadastro: ERRO DE BANCO - " + e.getMessage());
            return ResponseEntity.status(500).body("Erro de Banco de Dados: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            System.err.println("[DEBUG_LOG] Cadastro: ERRO DESCONHECIDO - " + e.getClass().getName() + " : " + e.getMessage());
            e.printStackTrace();
            String msg = e.getMessage();
            if (e.getCause() != null) msg += " | Causa: " + e.getCause().getMessage();
            return ResponseEntity.status(500).body("Erro Interno: " + msg);
        }
    }
}
