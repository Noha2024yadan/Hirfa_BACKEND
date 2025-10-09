package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CooperativeRepository extends JpaRepository<Cooperative, UUID> {

    Optional<Cooperative> findByUsernameOrEmail(String username, String email);

    Optional<Cooperative> findByUsername(String username);

    Optional<Cooperative> findByEmail(String email);

    // For filtering in findAllCooperatives
    java.util.List<Cooperative> findByStatutVerification(String statutVerification);
}
