package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Panier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PanierRepository extends JpaRepository<Panier, UUID> {
    Optional<Panier> findByClient_ClientId(UUID clientId);

}
