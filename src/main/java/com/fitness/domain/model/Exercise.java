package com.fitness.domain.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "external_id")
    private String externalId; // ID da API externa (RapidAPI)

    @Column(nullable = false)
    private String name;

    private String primaryMuscles; // separado por vírgula
    private String equipment;      // separado por vírgula

    private String imageUrl;
    private String videoUrl;

    private Instant lastSyncedAt;
    private Instant updatedAt;

    private String source = "ascendapi";

    public Exercise() {}

    public Exercise(String externalId, String name, String primaryMuscles, String equipment, String imageUrl, String videoUrl) {
        this.externalId = externalId;
        this.name = name;
        this.primaryMuscles = primaryMuscles;
        this.equipment = equipment;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.lastSyncedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() { this.updatedAt = Instant.now(); }

    @PrePersist
    public void onPersist() { this.updatedAt = Instant.now(); this.lastSyncedAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrimaryMuscles() { return primaryMuscles; }
    public void setPrimaryMuscles(String primaryMuscles) { this.primaryMuscles = primaryMuscles; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Instant getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
