package com.fitness.dto;

import com.fitness.domain.enums.UserRole;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private String specialty;
    private String registrationNumber;
    private String formation;
    private String experience;

    public RegisterRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getFormation() { return formation; }
    public void setFormation(String formation) { this.formation = formation; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
}
