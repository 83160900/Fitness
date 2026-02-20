package com.fitness.controller;

import com.fitness.domain.model.WorkoutPlan;
import com.fitness.dto.WorkoutPlanRequest;
import com.fitness.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@CrossOrigin(origins = "*")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody WorkoutPlanRequest request) {
        try {
            WorkoutPlan plan = workoutService.createWorkoutPlan(request);
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao criar treino: " + e.getMessage());
        }
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<List<WorkoutPlan>> getByStudent(@PathVariable String email) {
        return ResponseEntity.ok(workoutService.getWorkoutsByStudent(email));
    }

    @GetMapping("/coach/{email}")
    public ResponseEntity<List<WorkoutPlan>> getByCoach(@PathVariable String email) {
        return ResponseEntity.ok(workoutService.getWorkoutsByCoach(email));
    }

    @PostMapping("/{planId}/link")
    public ResponseEntity<?> linkToStudent(@PathVariable java.util.UUID planId, @RequestParam String studentEmail) {
        try {
            return ResponseEntity.ok(workoutService.linkExistingPlanToStudent(planId, studentEmail));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao vincular treino: " + e.getMessage());
        }
    }
}
