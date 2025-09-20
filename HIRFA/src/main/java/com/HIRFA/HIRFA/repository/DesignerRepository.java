package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Designer;
import org.springframework.data.jpa.repository.JpaRepository;//pour utiliser javarepository qui contient les focntion findall..

import java.util.Optional;
import java.util.UUID;//parcque clientid est un uuid

public interface DesignerRepository extends JpaRepository<Designer, UUID> {
    Optional<Designer> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
