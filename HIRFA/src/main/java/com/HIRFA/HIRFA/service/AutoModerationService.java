package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.Design;
import com.HIRFA.HIRFA.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// ===== SERVICE DE SUPPRESSION AUTOMATIQUE =====
// Service qui s'exécute périodiquement pour supprimer automatiquement
// les produits et designs ayant reçu plus de 5 signalements

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AutoModerationService {

    private final ProductReportService productReportService;
    private final DesignReportService designReportService;

    /**
     * Tâche automatique qui s'exécute toutes les heures pour vérifier
     * et supprimer les produits/designs avec trop de signalements
     */
    @Scheduled(fixedRate = 3600000) // Toutes les heures (3600000 ms)
    public void autoModerateContent() {
        log.info("Démarrage de la modération automatique à {}", LocalDateTime.now());
        
        try {
            // Modérer les produits
            moderateProducts();
            
            // Modérer les designs
            moderateDesigns();
            
            log.info("Modération automatique terminée avec succès à {}", LocalDateTime.now());
            
        } catch (Exception e) {
            log.error("Erreur lors de la modération automatique: {}", e.getMessage(), e);
        }
    }

    /**
     * Modérer les produits signalés
     */
    private void moderateProducts() {
        List<Product> flaggedProducts = productReportService.getProductsAboveReportThreshold();
        
        log.info("Nombre de produits à modérer: {}", flaggedProducts.size());
        
        for (Product product : flaggedProducts) {
            try {
                // Marquer le produit comme indisponible s'il ne l'est pas déjà
                if (product.isAvailable()) {
                    product.setAvailable(false);
                    product.setReported(true);
                    product.setReportedReason("Supprimé automatiquement par le système de modération");
                    product.setReportedAt(LocalDateTime.now());
                    
                    log.warn("Produit {} ({}) supprimé automatiquement pour signalements excessifs", 
                             product.getProductId(), product.getName());
                }
            } catch (Exception e) {
                log.error("Erreur lors de la suppression du produit {}: {}", 
                         product.getProductId(), e.getMessage());
            }
        }
    }

    /**
     * Modérer les designs signalés
     */
    private void moderateDesigns() {
        List<Design> flaggedDesigns = designReportService.getDesignsAboveReportThreshold();
        
        log.info("Nombre de designs à modérer: {}", flaggedDesigns.size());
        
        for (Design design : flaggedDesigns) {
            try {
                // Marquer le design comme indisponible s'il ne l'est pas déjà
                if (design.isAvailable()) {
                    design.setAvailable(false);
                    design.setReported(true);
                    design.setReportedReason("Supprimé automatiquement par le système de modération");
                    design.setReportedAt(LocalDateTime.now());
                    
                    log.warn("Design {} ({}) supprimé automatiquement pour signalements excessifs", 
                             design.getDesignId(), design.getNomDesign());
                }
            } catch (Exception e) {
                log.error("Erreur lors de la suppression du design {}: {}", 
                         design.getDesignId(), e.getMessage());
            }
        }
    }

    /**
     * Méthode pour déclencher manuellement la modération (pour les admins)
     */
    public void triggerManualModeration() {
        log.info("Modération manuelle déclenchée à {}", LocalDateTime.now());
        autoModerateContent();
    }

    /**
     * Obtenir des statistiques sur la modération
     */
    public ModerationStats getModerationStats() {
        List<Product> flaggedProducts = productReportService.getProductsAboveReportThreshold();
        List<Design> flaggedDesigns = designReportService.getDesignsAboveReportThreshold();
        
        return ModerationStats.builder()
            .flaggedProductsCount(flaggedProducts.size())
            .flaggedDesignsCount(flaggedDesigns.size())
            .lastModerationRun(LocalDateTime.now())
            .build();
    }

    // Classe interne pour les statistiques de modération
    @lombok.Data
    @lombok.Builder
    public static class ModerationStats {
        private int flaggedProductsCount;
        private int flaggedDesignsCount;
        private LocalDateTime lastModerationRun;
    }
}