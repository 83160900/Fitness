package com.fitness.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String action; // Ex: VIEW_STUDENT_DATA, UPDATE_TRAINING
    private String entityType; // User, Training, Bioimpedance
    private UUID entityId;
    private String performedBy; // Email do profissional
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Column(columnDefinition = "TEXT")
    private String details;

    public AuditLog() {}

    public AuditLog(String action, String entityType, UUID entityId, String performedBy, String details) {
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.performedBy = performedBy;
        this.details = details;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public String getAction() { return action; }
    public String getEntityType() { return entityType; }
    public UUID getEntityId() { return entityId; }
    public String getPerformedBy() { return performedBy; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDetails() { return details; }
}
