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
            System.out.println("[DEBUG_LOG] Recebendo requisição de novo treino: " + request.getName());
            System.out.println("[DEBUG_LOG] Coach: " + request.getCoachEmail() + ", Student: " + request.getStudentEmail());
            WorkoutPlan plan = workoutService.createWorkoutPlan(request);
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            System.err.println("[DEBUG_LOG] Erro ao criar treino: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao criar treino: " + e.getMessage());
        }
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<List<WorkoutPlan>> getByStudent(@PathVariable String email) {
        System.out.println("[DEBUG_LOG] Buscando treinos para o aluno: " + email);
        List<WorkoutPlan> workouts = workoutService.getWorkoutsByStudent(email);
        System.out.println("[DEBUG_LOG] Encontrados " + workouts.size() + " treinos ativos para o aluno.");
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/coach/{email}")
    public ResponseEntity<List<WorkoutPlan>> getByCoach(@PathVariable String email) {
        System.out.println("[DEBUG_LOG] Buscando treinos (Biblioteca) para o coach: " + email);
        List<WorkoutPlan> workouts = workoutService.getWorkoutsByCoach(email);
        System.out.println("[DEBUG_LOG] Encontrados " + workouts.size() + " treinos na biblioteca.");
        return ResponseEntity.ok(workouts);
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
