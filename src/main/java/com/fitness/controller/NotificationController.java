package com.fitness.controller;

import com.fitness.domain.model.Notification;
import com.fitness.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationRepository repository;

    public NotificationController(NotificationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(
            @RequestParam String email,
            @RequestParam(required = false) String status) {
        
        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(repository.findByRecipientEmailAndStatusOrderByCreatedAtDesc(email, status));
        }
        return ResponseEntity.ok(repository.findByRecipientEmailOrderByCreatedAtDesc(email));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable UUID id) {
        return repository.findById(id).map(notification -> {
            notification.setStatus("LIDA");
            notification.setReadAt(LocalDateTime.now());
            repository.save(notification);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<?> archive(@PathVariable UUID id) {
        return repository.findById(id).map(notification -> {
            notification.setStatus("ARQUIVADA");
            repository.save(notification);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
