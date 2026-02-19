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
    private final com.fitness.repository.AuditLogRepository auditLogRepository;

    public PersonalController(UserRepository userRepository, com.fitness.repository.AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/{coachEmail}/students")
    public ResponseEntity<List<UserSummaryDTO>> getMyStudents(@PathVariable String coachEmail) {
        List<User> students = userRepository.findByCoachEmail(coachEmail);
        
        // Trilha de auditoria
        auditLogRepository.save(new com.fitness.domain.model.AuditLog(
            "VIEW_STUDENT_LIST", "User", null, coachEmail, "Acesso a lista de alunos vinculados"
        ));

        List<UserSummaryDTO> dtos = students.stream()
                .map(s -> new UserSummaryDTO(s.getId(), s.getName(), s.getEmail(), s.getRole(), s.getPhotoUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
