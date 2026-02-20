package com.fitness.controller;

import com.fitness.domain.model.User;
import com.fitness.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentRelationsController {

    private final UserRepository userRepository;

    public StudentRelationsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{email}/professionals")
    public ResponseEntity<?> getProfessionals(@PathVariable String email) {
        Optional<User> opt = userRepository.findByEmail(email.trim().toLowerCase());
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        User student = opt.get();
        List<Map<String, String>> pros;
        if (student.getProfessionals() == null) {
            pros = List.of();
        } else {
            pros = student.getProfessionals().stream()
                .map(p -> Map.of(
                        "name", p.getName() != null ? p.getName() : "",
                        "email", p.getEmail() != null ? p.getEmail() : "",
                        "phone", p.getPhone() != null ? p.getPhone() : "",
                        "photoUrl", p.getPhotoUrl() != null ? p.getPhotoUrl() : ""
                )).collect(Collectors.toList());
        }
        return ResponseEntity.ok(pros);
    }
}
