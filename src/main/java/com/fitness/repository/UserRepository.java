package com.fitness.repository;

import com.fitness.domain.model.User;
import com.fitness.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    List<User> findByCoachEmail(String coachEmail);
}
