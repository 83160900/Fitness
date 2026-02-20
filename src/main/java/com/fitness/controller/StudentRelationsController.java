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
        List<Map<String, Object>> pros = student.getProfessionals() == null ? List.of() : student.getProfessionals().stream()
                .map(p -> Map.of(
                        "name", p.getName(),
                        "email", p.getEmail(),
                        "phone", p.getPhone(),
                        "photoUrl", p.getPhotoUrl()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(pros);
    }
}
