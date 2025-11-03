package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Cooperative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CooperativeRepository extends JpaRepository<Cooperative, UUID> {
    Optional<Cooperative> findByEmail(String email);

    Optional<Cooperative> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByBrand(String brand);

    Optional<Cooperative> findByUsernameOrEmail(String username, String email);

    List<Cooperative> findByStatutVerification(String statutVerification);

    Page<Cooperative> findByStatutVerification(String statutVerification, Pageable pageable);

    long countByStatutVerification(String statutVerification);
    
    // ===== MÉTHODES AJOUTÉES POUR LA FONCTIONNALITÉ D'INFORMATIONS CÔTÉ DESIGNER =====
    
    // Trouver toutes les coopératives actives
    List<Cooperative> findByEnabled(boolean enabled);
    
    // Rechercher par marque (ignorer la casse) et actives seulement
    List<Cooperative> findByBrandContainingIgnoreCaseAndEnabled(String brand, boolean enabled);
    
    // Rechercher par adresse (ville/région) et actives seulement
    List<Cooperative> findByAdresseContainingIgnoreCaseAndEnabled(String adresse, boolean enabled);
}
