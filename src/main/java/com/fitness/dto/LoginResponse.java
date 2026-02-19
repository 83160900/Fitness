package com.fitness.dto;

import com.fitness.domain.enums.UserRole;

public class LoginResponse {
    private String name;
    private String email;
    private UserRole role;
    private String specialty;
    private String registrationNumber;
    private String photoUrl;
    private String message;
    private boolean forcePasswordChange;

    public LoginResponse() {}

    public LoginResponse(String name, String email, UserRole role, String message) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public static class Builder {
        private String name;
        private String email;
        private UserRole role;
        private String specialty;
        private String registrationNumber;
        private String photoUrl;
        private String message;
        private boolean forcePasswordChange;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public Builder specialty(String specialty) {
            this.specialty = specialty;
            return this;
        }

        public Builder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder forcePasswordChange(boolean forcePasswordChange) {
            this.forcePasswordChange = forcePasswordChange;
            return this;
        }

        public LoginResponse build() {
            LoginResponse response = new LoginResponse(name, email, role, message);
            response.setSpecialty(this.specialty);
            response.setRegistrationNumber(this.registrationNumber);
            response.setPhotoUrl(this.photoUrl);
            response.setForcePasswordChange(this.forcePasswordChange);
            return response;
        }
    }

    public boolean isForcePasswordChange() {
        return forcePasswordChange;
    }

    public void setForcePasswordChange(boolean forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }
}
