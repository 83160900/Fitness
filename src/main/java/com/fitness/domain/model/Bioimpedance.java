package com.fitness.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bioimpedance")
public class Bioimpedance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String studentEmail;

    @Column(nullable = false)
    private LocalDateTime date;

    private Double weight;
    private Double imc;
    private Double fatPercent;
    private Double leanMass;
    private Double muscleMass;
    private Double visceralFat;
    private Double bodyWater;
    private Integer metabolicAge;

    private Double waist;
    private Double hip;
    private Double arm;
    private Double thigh;

    public Bioimpedance() {
        this.date = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getImc() { return imc; }
    public void setImc(Double imc) { this.imc = imc; }

    public Double getFatPercent() { return fatPercent; }
    public void setFatPercent(Double fatPercent) { this.fatPercent = fatPercent; }

    public Double getLeanMass() { return leanMass; }
    public void setLeanMass(Double leanMass) { this.leanMass = leanMass; }

    public Double getMuscleMass() { return muscleMass; }
    public void setMuscleMass(Double muscleMass) { this.muscleMass = muscleMass; }

    public Double getVisceralFat() { return visceralFat; }
    public void setVisceralFat(Double visceralFat) { this.visceralFat = visceralFat; }

    public Double getBodyWater() { return bodyWater; }
    public void setBodyWater(Double bodyWater) { this.bodyWater = bodyWater; }

    public Integer getMetabolicAge() { return metabolicAge; }
    public void setMetabolicAge(Integer metabolicAge) { this.metabolicAge = metabolicAge; }

    public Double getWaist() { return waist; }
    public void setWaist(Double waist) { this.waist = waist; }

    public Double getHip() { return hip; }
    public void setHip(Double hip) { this.hip = hip; }

    public Double getArm() { return arm; }
    public void setArm(Double arm) { this.arm = arm; }

    public Double getThigh() { return thigh; }
    public void setThigh(Double thigh) { this.thigh = thigh; }
}
