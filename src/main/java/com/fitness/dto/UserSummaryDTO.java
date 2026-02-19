package com.fitness.dto;

import com.fitness.domain.enums.UserRole;
import java.util.UUID;

public class UserSummaryDTO {
    private UUID id;
    private String name;
    private String email;
    private UserRole role;
    private String photoUrl;
    private String specialty;

    public UserSummaryDTO(UUID id, String name, String email, UserRole role, String photoUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.photoUrl = photoUrl;
    }

    public UserSummaryDTO(UUID id, String name, String email, UserRole role, String photoUrl, String specialty) {
        this(id, name, email, role, photoUrl);
        this.specialty = specialty;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
