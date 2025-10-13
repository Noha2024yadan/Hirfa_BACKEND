package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Designer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DesignerRepository extends JpaRepository<Designer, UUID> {
    Optional<Designer> findByEmail(String email);

    Optional<Designer> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Designer> findByUsernameOrEmail(String username, String email);

    Page<Designer> findBySpecialitesContaining(String specialite, Pageable pageable);
}
