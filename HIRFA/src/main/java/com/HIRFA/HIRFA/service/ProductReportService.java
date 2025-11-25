package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.*;
import com.HIRFA.HIRFA.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * ===== SERVICE POUR LA GESTION DES SIGNALEMENTS DE PRODUITS =====
 * Gère les signalements de produits et la suppression automatique après 5
 * signalements
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductReportService {

    private final ProductReportRepository productReportRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final DesignerRepository designerRepository;
    private final CooperativeRepository cooperativeRepository;

    // Seuil de signalement pour suppression automatique
    private static final long REPORT_THRESHOLD = 5;

    /**
     * Signaler un produit
     */
    public ProductReport reportProduct(UUID productId, UUID reporterId, ReportType reportType,
            String reason, String description, String reporterType) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // 🔹 Trouver le reporter selon son type
        Object reporter = findReporterByType(reporterId, reporterType);
        if (reporter == null) {
            throw new RuntimeException("Reporter introuvable");
        }

        // 🔹 Vérifier si le reporter a déjà signalé ce produit
        if (productReportRepository.existsByProductAndReporterId(product, reporterId)) {
            throw new RuntimeException("Vous avez déjà signalé ce produit");
        }

        // 🔹 Créer le signalement
        ProductReport report = ProductReport.builder()
                .product(product)
                .reporterId(reporterId)
                .reporterType(reporterType)
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
     * Trouve un reporter selon son type (client, designer, coopérative)
     */
    private Object findReporterByType(UUID id, String type) {
        switch (type.toUpperCase()) {
            case "CLIENT":
                return clientRepository.findById(id).orElse(null);
            case "DESIGNER":
                return designerRepository.findById(id).orElse(null);
            case "COOPERATIVE":
                return cooperativeRepository.findById(id).orElse(null);
            default:
                return null;
        }
    }

    /**
     * Vérifie et supprime automatiquement un produit après trop de signalements
     */
    private void checkAndAutoDeleteProduct(Product product) {
        long reportCount = productReportRepository.countByProduct(product);

        if (reportCount >= REPORT_THRESHOLD) {
            product.setAvailable(false);
            product.setReported(true);
            product.setReportedReason("Supprimé automatiquement après " + reportCount + " signalements");
            product.setReportedAt(LocalDateTime.now());

            productRepository.save(product);

            // Marquer les signalements comme AUTO_DELETED
            List<ProductReport> reports = productReportRepository.findByProduct(product);
            reports.forEach(report -> report.setStatus(ReportStatus.AUTO_DELETED));
            productReportRepository.saveAll(reports);
        }
    }

    /**
     * Obtenir tous les signalements d'un produit
     */
    public List<ProductReport> getProductReports(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        return productReportRepository.findByProduct(product);
    }

    /**
     * Obtenir le nombre de signalements d'un produit
     */
    public long getReportCount(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        return productReportRepository.countByProduct(product);
    }

    /**
     * Trouver tous les produits au-dessus du seuil
     */
    public List<Product> getProductsAboveReportThreshold() {
        return productReportRepository.findProductsWithReportsAboveThreshold(REPORT_THRESHOLD);
    }

    /**
     * Traitement manuel d'un signalement par un admin
     */
    public void processReport(UUID reportId, ReportStatus status) {
        ProductReport report = productReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));

        report.setStatus(status);
        productReportRepository.save(report);
    }

    public ProductReport reportProduct(UUID productId, UUID reporterId, ReportType reportType, String reason,
            String description) {
        throw new UnsupportedOperationException("Unimplemented method 'reportProduct'");
    }
}
