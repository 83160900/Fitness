package com.fitness.repository;

import com.fitness.domain.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {
    List<WorkoutPlan> findByStudentEmailAndActiveTrue(String studentEmail);
    List<WorkoutPlan> findByCoachEmailAndActiveTrue(String coachEmail);
}
