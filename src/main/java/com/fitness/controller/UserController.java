package com.fitness.controller;

import com.fitness.domain.model.User;
import com.fitness.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String email) {
        Optional<User> opt = userRepository.findByEmail(email.trim().toLowerCase());
        return opt.<ResponseEntity<?>>map(user -> ResponseEntity.ok(Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone(),
                "address", user.getAddress(),
                "photoUrl", user.getPhotoUrl(),
                "role", user.getRole()
        ))).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> body) {
        try {
            String email = body.getOrDefault("email", "").toString().trim().toLowerCase();
            if (email.isEmpty()) return ResponseEntity.badRequest().body("Email é obrigatório");
            Optional<User> opt = userRepository.findByEmail(email);
            if (opt.isEmpty()) return ResponseEntity.status(404).body("Usuário não encontrado");

            User u = opt.get();
            if (body.containsKey("name")) u.setName(String.valueOf(body.get("name")));
            if (body.containsKey("phone")) u.setPhone(String.valueOf(body.get("phone")));
            if (body.containsKey("address")) u.setAddress(String.valueOf(body.get("address")));
            if (body.containsKey("photoUrl")) u.setPhotoUrl(String.valueOf(body.get("photoUrl")));

            userRepository.save(u);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar perfil: " + e.getMessage());
        }
    }
}
