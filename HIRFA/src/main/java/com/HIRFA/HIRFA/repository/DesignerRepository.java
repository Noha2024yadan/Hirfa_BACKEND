package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Designer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DesignerRepository extends JpaRepository<Designer, UUID> {
    // Authentication related methods
    Optional<Designer> findByUsernameOrEmail(String username, String email);

    Optional<Designer> findByEmail(String email);

    Optional<Designer> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    // Business logic methods
    boolean existsByPortfolio(String portfolio);

    // Search designers by name, email, or portfolio
    @Query("SELECT d FROM Designer d WHERE " +
            "(:search IS NULL OR " +
            "LOWER(d.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.prenom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.portfolio) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Designer> searchDesigners(@Param("search") String search, Pageable pageable);
}
