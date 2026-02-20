package com.fitness.service;

import com.fitness.domain.model.Exercise;
import com.fitness.domain.model.User;
import com.fitness.domain.model.WorkoutExercise;
import com.fitness.domain.model.WorkoutPlan;
import com.fitness.dto.WorkoutPlanRequest;
import com.fitness.repository.ExerciseRepository;
import com.fitness.repository.UserRepository;
import com.fitness.repository.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutService(WorkoutPlanRepository workoutPlanRepository, UserRepository userRepository, ExerciseRepository exerciseRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public WorkoutPlan createWorkoutPlan(WorkoutPlanRequest request) {
        User coach = userRepository.findByEmail(request.getCoachEmail().trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Coach not found: " + request.getCoachEmail()));
        
        User student = null;
        if (request.getStudentEmail() != null && !request.getStudentEmail().isBlank()) {
            student = userRepository.findByEmail(request.getStudentEmail().trim().toLowerCase())
                    .orElseThrow(() -> new RuntimeException("Student not found: " + request.getStudentEmail()));
        }

        WorkoutPlan plan = new WorkoutPlan();
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setCoach(coach);
        plan.setStudent(student);
        plan.setItems(new ArrayList<>());
        plan.setCreatedAt(java.time.Instant.now());
        plan.setActive(true);

        for (WorkoutPlanRequest.WorkoutExerciseRequest itemReq : request.getExercises()) {
            Exercise exercise = exerciseRepository.findById(itemReq.getExerciseId())
                    .orElseThrow(() -> new RuntimeException("Exercise not found: " + itemReq.getExerciseId()));
            
            WorkoutExercise item = new WorkoutExercise();
            item.setWorkoutPlan(plan);
            item.setExercise(exercise);
            item.setSets(itemReq.getSets());
            item.setReps(itemReq.getReps());
            item.setRestTime(itemReq.getRestTime());
            item.setExerciseOrder(itemReq.getOrder());
            item.setObservations(itemReq.getObservations());
            plan.getItems().add(item);
        }

        return workoutPlanRepository.save(plan);
    }

    @Transactional
    public WorkoutPlan linkExistingPlanToStudent(java.util.UUID planId, String studentEmail) {
        WorkoutPlan sourcePlan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        
        User student = userRepository.findByEmail(studentEmail.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        WorkoutPlan newPlan = new WorkoutPlan();
        newPlan.setName(sourcePlan.getName());
        newPlan.setDescription(sourcePlan.getDescription());
        newPlan.setCoach(sourcePlan.getCoach());
        newPlan.setStudent(student);
        newPlan.setCreatedAt(java.time.Instant.now());
        newPlan.setActive(true);
        newPlan.setItems(new ArrayList<>());

        for (WorkoutExercise sourceItem : sourcePlan.getItems()) {
            WorkoutExercise newItem = new WorkoutExercise();
            newItem.setWorkoutPlan(newPlan);
            newItem.setExercise(sourceItem.getExercise());
            newItem.setSets(sourceItem.getSets());
            newItem.setReps(sourceItem.getReps());
            newItem.setRestTime(sourceItem.getRestTime());
            newItem.setExerciseOrder(sourceItem.getExerciseOrder());
            newItem.setObservations(sourceItem.getObservations());
            newPlan.getItems().add(newItem);
        }

        return workoutPlanRepository.save(newPlan);
    }

    public List<WorkoutPlan> getWorkoutsByStudent(String email) {
        return workoutPlanRepository.findByStudentEmailAndActiveTrue(email.trim().toLowerCase());
    }

    public List<WorkoutPlan> getWorkoutsByCoach(String email) {
        return workoutPlanRepository.findByCoachEmailAndActiveTrue(email.trim().toLowerCase());
    }
}
