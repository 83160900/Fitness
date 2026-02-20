package com.fitness.controller;

import com.fitness.domain.model.User;
import com.fitness.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    @PostMapping("/upload-photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo vazio");
        }

        try {
            // Cria diretório se não existir
            String uploadDir = "uploads/profiles";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Nome único para o arquivo
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Retorna a URL (ajustar conforme domínio)
            // No Railway, pode ser necessário servir esses arquivos estáticos ou usar um bucket S3
            // Por enquanto, retornamos o caminho relativo que será servido pelo Spring se configurado
            String fileUrl = "/api/users/photo/" + fileName;
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/photo/{fileName}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String fileName) {
        try {
            Path path = Paths.get("uploads/profiles/" + fileName);
            byte[] image = Files.readAllBytes(path);
            String contentType = Files.probeContentType(path);
            return ResponseEntity.ok()
                    .header("Content-Type", contentType != null ? contentType : "image/jpeg")
                    .body(image);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
