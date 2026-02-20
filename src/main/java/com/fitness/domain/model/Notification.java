package com.fitness.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String type; // RESERVA, CONFIRMACAO, CANCELAMENTO

    private String senderEmail;

    @Column(nullable = false)
    private String recipientEmail;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    private UUID slotId;

    @Column(nullable = false)
    private String status; // PENDENTE, LIDA, ARQUIVADA

    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    private boolean sentToWhatsApp;
    private String whatsappError;

    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UUID getSlotId() { return slotId; }
    public void setSlotId(UUID slotId) { this.slotId = slotId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public boolean isSentToWhatsApp() { return sentToWhatsApp; }
    public void setSentToWhatsApp(boolean sentToWhatsApp) { this.sentToWhatsApp = sentToWhatsApp; }

    public String getWhatsappError() { return whatsappError; }
    public void setWhatsappError(String whatsappError) { this.whatsappError = whatsappError; }
}
