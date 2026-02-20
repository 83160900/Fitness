package com.fitness.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private Integer sets;
    private String reps;
    private String restTime;
    private Integer exerciseOrder;
    private String observations;

    public WorkoutExercise() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public WorkoutPlan getWorkoutPlan() { return workoutPlan; }
    public void setWorkoutPlan(WorkoutPlan workoutPlan) { this.workoutPlan = workoutPlan; }
    public Exercise getExercise() { return exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }
    public String getReps() { return reps; }
    public void setReps(String reps) { this.reps = reps; }
    public String getRestTime() { return restTime; }
    public void setRestTime(String restTime) { this.restTime = restTime; }
    public Integer getExerciseOrder() { return exerciseOrder; }
    public void setExerciseOrder(Integer exerciseOrder) { this.exerciseOrder = exerciseOrder; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
