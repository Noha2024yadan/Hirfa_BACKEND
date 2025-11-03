package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.DesignReportRequest;
import com.HIRFA.HIRFA.entity.DesignReport;
import com.HIRFA.HIRFA.entity.ReportStatus;
import com.HIRFA.HIRFA.service.DesignReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// ===== CONTRÔLEUR POUR LES SIGNALEMENTS DE DESIGNS =====
// API REST pour gérer les signalements de designs

@RestController
@RequestMapping("/api/design-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DesignReportController {

    private final DesignReportService designReportService;

    /**
     * Signaler un design
     * POST /api/design-reports
     */
    @PostMapping
    public ResponseEntity<?> reportDesign(@RequestBody DesignReportRequest request) {
        try {
            DesignReport report = designReportService.reportDesign(
                request.getDesignId(),
                request.getReporterId(),
                request.getReportType(),
                request.getReason(),
                request.getDescription()
            );
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Design signalé avec succès",
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
     * Obtenir tous les signalements d'un design
     * GET /api/design-reports/design/{designId}
     */
    @GetMapping("/design/{designId}")
    public ResponseEntity<List<DesignReport>> getDesignReports(@PathVariable UUID designId) {
        List<DesignReport> reports = designReportService.getDesignReports(designId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Obtenir le nombre de signalements d'un design
     * GET /api/design-reports/design/{designId}/count
     */
    @GetMapping("/design/{designId}/count")
    public ResponseEntity<?> getReportCount(@PathVariable UUID designId) {
        long count = designReportService.getReportCount(designId);
        return ResponseEntity.ok().body(Map.of(
            "designId", designId,
            "reportCount", count
        ));
    }

    /**
     * Traiter un signalement (admin seulement)
     * PUT /api/design-reports/{reportId}/process
     */
    @PutMapping("/{reportId}/process")
    public ResponseEntity<?> processReport(@PathVariable UUID reportId, 
                                         @RequestParam ReportStatus status) {
        try {
            designReportService.processReport(reportId, status);
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
     * Obtenir les designs avec trop de signalements (admin seulement)
     * GET /api/design-reports/flagged-designs
     */
    @GetMapping("/flagged-designs")
    public ResponseEntity<?> getFlaggedDesigns() {
        var designs = designReportService.getDesignsAboveReportThreshold();
        return ResponseEntity.ok(designs);
    }
}