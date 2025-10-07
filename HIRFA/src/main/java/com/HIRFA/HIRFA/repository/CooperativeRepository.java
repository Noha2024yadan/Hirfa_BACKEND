package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CooperativeRepository extends JpaRepository<Cooperative, UUID> {
    Optional<Cooperative> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByBrand(String brand);
}
