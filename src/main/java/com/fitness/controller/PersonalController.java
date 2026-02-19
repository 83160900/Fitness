package com.fitness.controller;

import com.fitness.domain.model.User;
import com.fitness.dto.UserSummaryDTO;
import com.fitness.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personal")
@CrossOrigin(origins = "*")
public class PersonalController {

    private final UserRepository userRepository;

    public PersonalController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{coachEmail}/students")
    public ResponseEntity<List<UserSummaryDTO>> getMyStudents(@PathVariable String coachEmail) {
        List<User> students = userRepository.findByCoachEmail(coachEmail);
        List<UserSummaryDTO> dtos = students.stream()
                .map(s -> new UserSummaryDTO(s.getId(), s.getName(), s.getEmail(), s.getRole(), s.getPhotoUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
