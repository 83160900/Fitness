package com.fitness.repository;

import com.fitness.domain.model.User;
import com.fitness.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    
    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u JOIN u.professionals p WHERE p.email = :coachEmail")
    List<User> findByCoachEmail(String coachEmail);
}
