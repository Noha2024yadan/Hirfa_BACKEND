package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Design;
import com.HIRFA.HIRFA.entity.Designer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DesignRepository extends JpaRepository<Design, UUID> {
    List<Design> findByIsReportedTrue();

    Page<Design> findByIsReportedTrue(Pageable pageable);
    
    // ===== MÉTHODES AJOUTÉES POUR CRUD DESIGNER =====
    
    // Trouver tous les designs d'un designer avec pagination
    Page<Design> findByDesigner(Designer designer, Pageable pageable);
    
    // Trouver les designs actifs d'un designer
    Page<Design> findByDesignerAndStatutTrue(Designer designer, Pageable pageable);
    
    // Recherche dans les designs d'un designer
    Page<Design> findByDesignerAndNomDesignContainingIgnoreCaseOrCategoryContainingIgnoreCase(
        Designer designer, String nomDesign, String category, Pageable pageable);
    
    // Compter les designs d'un designer
    long countByDesigner(Designer designer);
    
    // Compter les designs actifs d'un designer
    long countByDesignerAndStatutTrue(Designer designer);
}
