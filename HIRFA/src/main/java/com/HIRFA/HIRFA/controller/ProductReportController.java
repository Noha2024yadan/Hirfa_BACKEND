package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.ProductReportRequest;
import com.HIRFA.HIRFA.entity.ProductReport;
import com.HIRFA.HIRFA.entity.ReportStatus;
import com.HIRFA.HIRFA.service.ProductReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// ===== CONTRÔLEUR POUR LES SIGNALEMENTS DE PRODUITS =====
// API REST pour gérer les signalements de produits

@RestController
@RequestMapping("/api/product-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductReportController {

    private final ProductReportService productReportService;

    /**
     * Signaler un produit
     * POST /api/product-reports
     */
    @PostMapping
    public ResponseEntity<?> reportProduct(@RequestBody ProductReportRequest request) {
        try {
            ProductReport report = productReportService.reportProduct(
                request.getProductId(),
                request.getReporterId(),
                request.getReportType(),
                request.getReason(),
                request.getDescription()
            );
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Produit signalé avec succès",
                "reportId", report.getReportId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir tous les signalements d'un produit
     * GET /api/product-reports/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReport>> getProductReports(@PathVariable UUID productId) {
        List<ProductReport> reports = productReportService.getProductReports(productId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Obtenir le nombre de signalements d'un produit
     * GET /api/product-reports/product/{productId}/count
     */
    @GetMapping("/product/{productId}/count")
    public ResponseEntity<?> getReportCount(@PathVariable UUID productId) {
        long count = productReportService.getReportCount(productId);
        return ResponseEntity.ok().body(Map.of(
            "productId", productId,
            "reportCount", count
        ));
    }

    /**
     * Traiter un signalement (admin seulement)
     * PUT /api/product-reports/{reportId}/process
     */
    @PutMapping("/{reportId}/process")
    public ResponseEntity<?> processReport(@PathVariable UUID reportId, 
                                         @RequestParam ReportStatus status) {
        try {
            productReportService.processReport(reportId, status);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Signalement traité avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les produits avec trop de signalements (admin seulement)
     * GET /api/product-reports/flagged-products
     */
    @GetMapping("/flagged-products")
    public ResponseEntity<?> getFlaggedProducts() {
        var products = productReportService.getProductsAboveReportThreshold();
        return ResponseEntity.ok(products);
    }
}