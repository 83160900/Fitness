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
        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";
        
        System.out.println("[DEBUG_LOG] Login: Tentativa para '" + email + "'");
        
        return userRepository.findByEmail(email)
                .map(user -> {
                    String storedPassword = user.getPassword() != null ? user.getPassword().trim() : "";
                    System.out.println("[DEBUG_LOG] Login: Usuario encontrado. Role: " + user.getRole());
                    
                    if (storedPassword.equals(password)) {
                        System.out.println("[DEBUG_LOG] Login: Senha correta!");
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
                        System.out.println("[DEBUG_LOG] Login: Senha incorreta. Enviada: '" + password + "', Banco: '" + storedPassword + "'");
                        return ResponseEntity.status(401).body("Credenciais inválidas!");
                    }
                })
                .orElseGet(() -> {
                    System.out.println("[DEBUG_LOG] Login: Usuario nao encontrado para '" + email + "'");
                    return ResponseEntity.status(401).body("Credenciais inválidas!");
                });
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setSpecialty(request.getSpecialty());
        user.setRegistrationNumber(request.getRegistrationNumber());
        user.setFormation(request.getFormation());
        user.setExperience(request.getExperience());

        userRepository.save(user);

        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }
}
