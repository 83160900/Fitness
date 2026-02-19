package com.fitness.repository;

import com.fitness.domain.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByEntityIdOrderByTimestampDesc(UUID entityId);
}
