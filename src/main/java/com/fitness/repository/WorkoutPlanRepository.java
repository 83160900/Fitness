package com.fitness.repository;

import com.fitness.domain.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {
    @Query("SELECT w FROM WorkoutPlan w WHERE w.student.email = :studentEmail AND w.active = true")
    List<WorkoutPlan> findByStudentEmailAndActiveTrue(@Param("studentEmail") String studentEmail);

    @Query("SELECT w FROM WorkoutPlan w WHERE w.coach.email = :coachEmail AND w.active = true")
    List<WorkoutPlan> findByCoachEmailAndActiveTrue(@Param("coachEmail") String coachEmail);
}
