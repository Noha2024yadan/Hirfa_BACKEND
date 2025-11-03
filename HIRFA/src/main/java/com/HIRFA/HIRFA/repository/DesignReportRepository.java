package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.DesignReport;
import com.HIRFA.HIRFA.entity.Design;
import com.HIRFA.HIRFA.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

// ===== REPOSITORY POUR LES SIGNALEMENTS DE DESIGNS =====
// Gère les opérations de base de données pour les signalements de designs

@Repository
public interface DesignReportRepository extends JpaRepository<DesignReport, UUID> {
    
    // Trouver tous les signalements d'un design spécifique
    List<DesignReport> findByDesign(Design design);
    
    // Trouver tous les signalements d'un utilisateur spécifique
    List<DesignReport> findByReporter(User reporter);
    
    // Compter le nombre de signalements pour un design
    long countByDesign(Design design);
    
    // Vérifier si un utilisateur a déjà signalé un design
    boolean existsByDesignAndReporter(Design design, User reporter);
    
    // Trouver les designs avec plus de X signalements
    @Query("SELECT dr.design FROM DesignReport dr GROUP BY dr.design HAVING COUNT(dr) >= :threshold")
    List<Design> findDesignsWithReportsAboveThreshold(@Param("threshold") long threshold);
    
    // Compter les signalements par statut pour un design
    @Query("SELECT COUNT(dr) FROM DesignReport dr WHERE dr.design = :design AND dr.status = :status")
    long countByDesignAndStatus(@Param("design") Design design, @Param("status") com.HIRFA.HIRFA.entity.ReportStatus status);
}