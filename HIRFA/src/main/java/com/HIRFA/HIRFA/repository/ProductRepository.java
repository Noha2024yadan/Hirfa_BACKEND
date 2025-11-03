package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Product;
import com.HIRFA.HIRFA.entity.Cooperative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByIsReportedTrue();

    Page<Product> findByIsReportedTrue(Pageable pageable);
    
    // ===== MÉTHODES AJOUTÉES POUR CRUD COOPÉRATIVE =====
    
    // Trouver tous les produits d'une coopérative avec pagination
    Page<Product> findByCooperative(Cooperative cooperative, Pageable pageable);
    
    // Trouver les produits actifs d'une coopérative
    Page<Product> findByCooperativeAndStatutTrue(Cooperative cooperative, Pageable pageable);
    
    // Recherche dans les produits d'une coopérative
    Page<Product> findByCooperativeAndNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
        Cooperative cooperative, String name, String category, Pageable pageable);
    
    // Compter les produits d'une coopérative
    long countByCooperative(Cooperative cooperative);
    
    // Compter les produits actifs d'une coopérative
    long countByCooperativeAndStatutTrue(Cooperative cooperative);
}
