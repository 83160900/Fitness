package com.fitness.controller;

import com.fitness.domain.model.User;
import com.fitness.dto.LoginRequest;
import com.fitness.dto.LoginResponse;
import com.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .map(user -> ResponseEntity.ok(LoginResponse.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .message("Login realizado com sucesso!")
                        .build()))
                .orElse(ResponseEntity.status(401).body("Credenciais inválidas!"));
    }
}
