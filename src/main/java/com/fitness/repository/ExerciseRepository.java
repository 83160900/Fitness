package com.fitness.repository;

import com.fitness.domain.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    Optional<Exercise> findByExternalId(String externalId);

    @Query("select e from Exercise e where (:q is null or lower(e.name) like lower(concat('%', cast(:q as string), '%'))) " +
            "and (:muscle is null or lower(e.primaryMuscles) like lower(concat('%', cast(:muscle as string), '%'))) " +
            "and (:equipment is null or lower(e.equipment) like lower(concat('%', cast(:equipment as string), '%'))) ")
    List<Exercise> search(@Param("q") String q,
                          @Param("muscle") String muscle,
                          @Param("equipment") String equipment);

    @Query("select max(e.lastSyncedAt) from Exercise e")
    Optional<java.time.Instant> findLastSyncTimestamp();

    Optional<Exercise> findByNameIgnoreCase(String name);
}
