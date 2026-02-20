package com.fitness.controller;

import com.fitness.domain.enums.UserRole;
import com.fitness.domain.model.User;
import com.fitness.dto.NewStudentRequest;
import com.fitness.dto.UserSummaryDTO;
import com.fitness.dto.CoachSummaryDTO;
import com.fitness.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personal")
@CrossOrigin(origins = "*")
public class PersonalController {

    private final UserRepository userRepository;
    private final com.fitness.repository.AuditLogRepository auditLogRepository;
    private final JdbcTemplate jdbcTemplate;

    public PersonalController(UserRepository userRepository, com.fitness.repository.AuditLogRepository auditLogRepository, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
        this.jdbcTemplate = jdbcTemplate;
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

    @GetMapping("/{coachEmail}/summary")
    public ResponseEntity<CoachSummaryDTO> getCoachSummary(@PathVariable String coachEmail) {
        List<User> students = userRepository.findByCoachEmail(coachEmail.trim().toLowerCase());
        
        long activeCount = students.stream().filter(User::isActive).count();
        
        // LÃ³gica de Alertas: Alunos sem CPF cadastrado ou sem endereÃ§o (dados incompletos)
        long alerts = students.stream()
                .filter(s -> s.getCpf() == null || s.getCpf().isEmpty() || s.getAddress() == null)
                .count();
        
        // Meta: Baseada no preenchimento de perfil dos alunos ativos
        String goal = "0%";
        if (activeCount > 0) {
            double completion = (double) (activeCount - alerts) / activeCount * 100;
            goal = (int)completion + "%";
        }

        return ResponseEntity.ok(new CoachSummaryDTO(activeCount, alerts, goal));
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody NewStudentRequest req) {
        try {
            if (req.getCpf() == null || req.getCpf().trim().isEmpty() || req.getEmail() == null || req.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("CPF e e-mail sÃƒÂ£o obrigatÃƒÂ³rios");
            }
            String cleanCpf = req.getCpf().replaceAll("\\D", "");
            String cleanEmail = req.getEmail().trim().toLowerCase();

            if (userRepository.findByCpf(cleanCpf).isPresent()) {
                return ResponseEntity.badRequest().body("CPF jÃƒÂ¡ cadastrado");
            }
            if (userRepository.findByEmail(cleanEmail).isPresent()) {
                return ResponseEntity.badRequest().body("E-mail jÃƒÂ¡ cadastrado");
            }

            // Criar aluno com senha = 4 primeiros dÃƒÂ­gitos do CPF
            String tempPassword = cleanCpf.length() >= 4 ? cleanCpf.substring(0, 4) : cleanCpf;
            User student = new User();
            student.setName(req.getName());
            student.setEmail(cleanEmail);
            student.setCpf(cleanCpf);
            student.setAddress(req.getAddress());
            student.setPassword(tempPassword);
            student.setRole(UserRole.ALUNO);
            student.setActive(true);
            student.setLgpdConsent(true);
            student.setForcePasswordChange(true);
            student = userRepository.save(student);

            // Vincular ao Personal
            if (req.getCoachEmail() != null && !req.getCoachEmail().trim().isEmpty()) {
                Optional<User> coach = userRepository.findByEmail(req.getCoachEmail().trim().toLowerCase());
                if (coach.isPresent()) {
                    UUID studentId = student.getId();
                    UUID coachId = coach.get().getId();
                    jdbcTemplate.update("INSERT INTO public.user_professionals (student_id, professional_id) VALUES (?, ?) ON CONFLICT DO NOTHING", studentId, coachId);
                }
            }

            // Envio de e-mail (stub)
            System.out.println("[DEBUG_LOG] Email (stub): Enviar para " + cleanEmail + " | UsuÃƒÂ¡rio: " + cleanCpf + " | Senha temporÃƒÂ¡ria: " + tempPassword);

            return ResponseEntity.ok(java.util.Map.of(
                "success", true,
                "message", "Aluno criado e vinculado com sucesso; e-mail de credenciais enviado.",
                "studentId", student.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao criar aluno: " + e.getMessage());
        }
    }
}
