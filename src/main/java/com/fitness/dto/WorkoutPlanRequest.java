package com.fitness.dto;

import java.util.List;
import java.util.UUID;

public class WorkoutPlanRequest {
    private String name;
    private String description;
    private String coachEmail;
    private String studentEmail;
    private List<WorkoutExerciseRequest> exercises;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCoachEmail() { return coachEmail; }
    public void setCoachEmail(String coachEmail) { this.coachEmail = coachEmail; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public List<WorkoutExerciseRequest> getExercises() { return exercises; }
    public void setExercises(List<WorkoutExerciseRequest> exercises) { this.exercises = exercises; }

    public static class WorkoutExerciseRequest {
        private UUID exerciseId;
        private Integer sets;
        private String reps;
        private String restTime;
        private Integer order;
        private String observations;

        // Getters and Setters
        public UUID getExerciseId() { return exerciseId; }
        public void setExerciseId(UUID exerciseId) { this.exerciseId = exerciseId; }
        public Integer getSets() { return sets; }
        public void setSets(Integer sets) { this.sets = sets; }
        public String getReps() { return reps; }
        public void setReps(String reps) { this.reps = reps; }
        public String getRestTime() { return restTime; }
        public void setRestTime(String restTime) { this.restTime = restTime; }
        public Integer getOrder() { return order; }
        public void setOrder(Integer order) { this.order = order; }
        public String getObservations() { return observations; }
        public void setObservations(String observations) { this.observations = observations; }
    }
}
