package com.fitness.controller;

import com.fitness.domain.model.ScheduleSlot;
import com.fitness.repository.ScheduleRepository;
import com.fitness.dto.ScheduleRequest;
import com.fitness.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleRepository repository;
    private final NotificationService notificationService;

    public ScheduleController(ScheduleRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    @GetMapping("/personal/{email}")
    public ResponseEntity<List<ScheduleSlot>> getPersonalSchedule(@PathVariable String email, @RequestParam String start, @RequestParam String end) {
        return ResponseEntity.ok(repository.findByPersonalEmailAndStartTimeBetweenOrderByStartTimeAsc(
                email, LocalDateTime.parse(start), LocalDateTime.parse(end)));
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSlot(@RequestBody ScheduleRequest req) {
        String personalEmail = req.getPersonalEmail();
        String studentEmail = req.getStudentEmail();
        LocalDateTime startTime = req.getStartTime();
        String recurrence = req.getRecurrence();

        synchronized (this) {
            Optional<ScheduleSlot> existing = repository.findByPersonalEmailAndStartTime(personalEmail, startTime);
            if (existing.isPresent() && !existing.get().getStatus().equals("CANCELADO")) {
                return ResponseEntity.badRequest().body("HorÃ¡rio jÃ¡ ocupado ou reservado.");
            }

            ScheduleSlot slot = existing.orElse(new ScheduleSlot());
            slot.setPersonalEmail(personalEmail);
            slot.setStudentEmail(studentEmail);
            slot.setStartTime(startTime);
            slot.setStatus("RESERVADO");
            slot.setRecurrence(recurrence != null ? recurrence : "NENHUMA");
            slot.setLockedUntil(LocalDateTime.now().plusMinutes(5));
            
            ScheduleSlot saved = repository.save(slot);
            notificationService.notifyReservationCreated(saved);
            return ResponseEntity.ok(saved);
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmSlot(@RequestBody ScheduleRequest req) {
        if (req.getSlotId() == null) return ResponseEntity.badRequest().body("SlotId obrigatÃ³rio");
        
        Optional<ScheduleSlot> opt = repository.findById(req.getSlotId());
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ScheduleSlot slot = opt.get();
        if (req.isConfirm()) {
            slot.setStatus("CONFIRMADO");
            ScheduleSlot saved = repository.save(slot);
            notificationService.notifyReservationConfirmed(saved);
            return ResponseEntity.ok(saved);
        } else {
            slot.setStatus("CANCELADO");
            slot.setRejectionReason(req.getReason());
            ScheduleSlot saved = repository.save(slot);
            notificationService.notifyReservationRejected(saved);
            return ResponseEntity.ok(saved);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelSlot(@RequestBody ScheduleRequest req) {
        if (req.getSlotId() == null) return ResponseEntity.badRequest().body("SlotId obrigatÃ³rio");
        
        Optional<ScheduleSlot> opt = repository.findById(req.getSlotId());
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ScheduleSlot slot = opt.get();
        if (slot.getStartTime().isBefore(LocalDateTime.now().plusDays(1))) {
            return ResponseEntity.badRequest().body("Cancelamento permitido apenas com 24h de antecedÃªncia.");
        }

        slot.setStatus("CANCELADO");
        return ResponseEntity.ok(repository.save(slot));
    }
}
