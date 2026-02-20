package com.fitness.repository;

import com.fitness.domain.model.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<ScheduleSlot, UUID> {
    List<ScheduleSlot> findByPersonalEmailAndStartTimeBetweenOrderByStartTimeAsc(String email, LocalDateTime start, LocalDateTime end);
    Optional<ScheduleSlot> findByPersonalEmailAndStartTime(String email, LocalDateTime startTime);
    List<ScheduleSlot> findByStudentEmailOrderByStartTimeDesc(String email);
}
