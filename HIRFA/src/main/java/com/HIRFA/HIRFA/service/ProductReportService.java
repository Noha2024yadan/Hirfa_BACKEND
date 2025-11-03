package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.*;
import com.HIRFA.HIRFA.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// ===== SERVICE POUR LA GESTION DES SIGNALEMENTS DE PRODUITS =====
// Gère les signalements de produits et la suppression automatique après 5 signalements

@Service
@RequiredArgsConstructor
@Transactional
public class ProductReportService {

    private final ProductReportRepository productReportRepository;
    private final ProductRepository productRepository; // Assume this exists
    private final UserRepository userRepository; // Assume this exists
    
    // Seuil de signalement pour suppression automatique
    private static final long REPORT_THRESHOLD = 5;

    /**
     * Signaler un produit
     * @param productId ID du produit à signaler
     * @param reporterId ID de l'utilisateur qui signale
     * @param reportType Type de signalement
     * @param reason Raison du signalement
     * @param description Description détaillée (optionnelle)
     * @return Le signalement créé
     */
    public ProductReport reportProduct(UUID productId, UUID reporterId, ReportType reportType, 
                                     String reason, String description) {
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        
        User reporter = userRepository.findById(reporterId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si l'utilisateur a déjà signalé ce produit
        if (productReportRepository.existsByProductAndReporter(product, reporter)) {
            throw new RuntimeException("Vous avez déjà signalé ce produit");
        }
        
        // Créer le signalement
        ProductReport report = ProductReport.builder()
            .product(product)
            .reporter(reporter)
            .reportType(reportType)
            .reason(reason)
            .description(description)
            .reportedAt(LocalDateTime.now())
            .status(ReportStatus.PENDING)
            .build();
        
        ProductReport savedReport = productReportRepository.save(report);
        
        // Vérifier si le produit doit être supprimé automatiquement
        checkAndAutoDeleteProduct(product);
        
        return savedReport;
    }

    /**
     * Vérifier et supprimer automatiquement un produit s'il a trop de signalements
     * @param product Le produit à vérifier
     */
    private void checkAndAutoDeleteProduct(Product product) {
        long reportCount = productReportRepository.countByProduct(product);
        
        if (reportCount >= REPORT_THRESHOLD) {
            // Marquer le produit comme indisponible et signalé
            product.setAvailable(false);
            product.setReported(true);
            product.setReportedReason("Supprimé automatiquement après " + reportCount + " signalements");
            product.setReportedAt(LocalDateTime.now());
            
            productRepository.save(product);
            
            // Optionnel: Mettre à jour tous les signalements comme approuvés
            List<ProductReport> reports = productReportRepository.findByProduct(product);
            reports.forEach(report -> {
                report.setStatus(ReportStatus.AUTO_DELETED);
            });
            productReportRepository.saveAll(reports);
        }
    }

    /**
     * Obtenir tous les signalements d'un produit
     * @param productId ID du produit
     * @return Liste des signalements
     */
    public List<ProductReport> getProductReports(UUID productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        
        return productReportRepository.findByProduct(product);
    }

    /**
     * Obtenir le nombre de signalements d'un produit
     * @param productId ID du produit
     * @return Nombre de signalements
     */
    public long getReportCount(UUID productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        
        return productReportRepository.countByProduct(product);
    }

    /**
     * Trouver tous les produits qui ont dépassé le seuil de signalement
     * @return Liste des produits à supprimer
     */
    public List<Product> getProductsAboveReportThreshold() {
        return productReportRepository.findProductsWithReportsAboveThreshold(REPORT_THRESHOLD);
    }

    /**
     * Traitement manuel d'un signalement par un admin
     * @param reportId ID du signalement
     * @param status Nouveau statut
     */
    public void processReport(UUID reportId, ReportStatus status) {
        ProductReport report = productReportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        
        report.setStatus(status);
        productReportRepository.save(report);
    }
}