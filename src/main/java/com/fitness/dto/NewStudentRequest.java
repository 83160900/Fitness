package com.fitness.dto;

public class NewStudentRequest {
    private String name;
    private String cpf;
    private String address;
    private String email;
    private String coachEmail;

    public NewStudentRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCoachEmail() { return coachEmail; }
    public void setCoachEmail(String coachEmail) { this.coachEmail = coachEmail; }
}
