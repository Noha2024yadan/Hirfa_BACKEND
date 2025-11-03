package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.ProductReport;
import com.HIRFA.HIRFA.entity.Product;
import com.HIRFA.HIRFA.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

// ===== REPOSITORY POUR LES SIGNALEMENTS DE PRODUITS =====
// Gère les opérations de base de données pour les signalements de produits

@Repository
public interface ProductReportRepository extends JpaRepository<ProductReport, UUID> {
    
    // Trouver tous les signalements d'un produit spécifique
    List<ProductReport> findByProduct(Product product);
    
    // Trouver tous les signalements d'un utilisateur spécifique
    List<ProductReport> findByReporter(User reporter);
    
    // Compter le nombre de signalements pour un produit
    long countByProduct(Product product);
    
    // Vérifier si un utilisateur a déjà signalé un produit
    boolean existsByProductAndReporter(Product product, User reporter);
    
    // Trouver les produits avec plus de X signalements
    @Query("SELECT pr.product FROM ProductReport pr GROUP BY pr.product HAVING COUNT(pr) >= :threshold")
    List<Product> findProductsWithReportsAboveThreshold(@Param("threshold") long threshold);
    
    // Compter les signalements par statut pour un produit
    @Query("SELECT COUNT(pr) FROM ProductReport pr WHERE pr.product = :product AND pr.status = :status")
    long countByProductAndStatus(@Param("product") Product product, @Param("status") com.HIRFA.HIRFA.entity.ReportStatus status);
}