package com.fitness.dto;

import com.fitness.domain.enums.UserRole;

public class LoginResponse {
    private String name;
    private String email;
    private UserRole role;
    private String message;

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

    public static class Builder {
        private String name;
        private String email;
        private UserRole role;
        private String message;

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

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(name, email, role, message);
        }
    }
}
