package com.fitness.repository;

import com.fitness.domain.model.Bioimpedance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BioimpedanceRepository extends JpaRepository<Bioimpedance, UUID> {
    List<Bioimpedance> findByStudentEmailOrderByDateDesc(String studentEmail);
}
