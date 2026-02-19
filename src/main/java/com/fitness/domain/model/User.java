package com.fitness.domain.model;

import com.fitness.domain.enums.UserRole;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String specialty; // Especialidade (Ex: Nutrição Esportiva, Quiropraxia Clínica)
    private String registrationNumber; // CRM, CRN, CREF, etc.
    
    @Column(columnDefinition = "TEXT")
    private String formation; // Formação/Graduação
    
    @Column(columnDefinition = "TEXT")
    private String experience; // Experiência profissional

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_professionals",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "professional_id")
    )
    private List<User> professionals; // Lista de profissionais que atendem este aluno (Personal, Nutro, etc.)

    @Column(nullable = false)
    private boolean lgpdConsent = false; // Consentimento LGPD

    private String photoUrl; // URL da foto do perfil

    private boolean active = true;

    public User() {}

    public User(UUID id, String email, String password, String name, UserRole role, boolean active) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<User> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<User> professionals) {
        this.professionals = professionals;
    }

    public boolean isLgpdConsent() {
        return lgpdConsent;
    }

    public void setLgpdConsent(boolean lgpdConsent) {
        this.lgpdConsent = lgpdConsent;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
