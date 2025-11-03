package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.*;
import com.HIRFA.HIRFA.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// ===== SERVICE POUR LA GESTION DES SIGNALEMENTS DE DESIGNS =====
// Gère les signalements de designs et la suppression automatique après 5 signalements

@Service
@RequiredArgsConstructor
@Transactional
public class DesignReportService {

    private final DesignReportRepository designReportRepository;
    private final DesignRepository designRepository; // Assume this exists
    private final UserRepository userRepository; // Assume this exists
    
    // Seuil de signalement pour suppression automatique
    private static final long REPORT_THRESHOLD = 5;

    /**
     * Signaler un design
     * @param designId ID du design à signaler
     * @param reporterId ID de l'utilisateur qui signale
     * @param reportType Type de signalement
     * @param reason Raison du signalement
     * @param description Description détaillée (optionnelle)
     * @return Le signalement créé
     */
    public DesignReport reportDesign(UUID designId, UUID reporterId, ReportType reportType, 
                                   String reason, String description) {
        
        Design design = designRepository.findById(designId)
            .orElseThrow(() -> new RuntimeException("Design non trouvé"));
        
        User reporter = userRepository.findById(reporterId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si l'utilisateur a déjà signalé ce design
        if (designReportRepository.existsByDesignAndReporter(design, reporter)) {
            throw new RuntimeException("Vous avez déjà signalé ce design");
        }
        
        // Créer le signalement
        DesignReport report = DesignReport.builder()
            .design(design)
            .reporter(reporter)
            .reportType(reportType)
            .reason(reason)
            .description(description)
            .reportedAt(LocalDateTime.now())
            .status(ReportStatus.PENDING)
            .build();
        
        DesignReport savedReport = designReportRepository.save(report);
        
        // Vérifier si le design doit être supprimé automatiquement
        checkAndAutoDeleteDesign(design);
        
        return savedReport;
    }

    /**
     * Vérifier et supprimer automatiquement un design s'il a trop de signalements
     * @param design Le design à vérifier
     */
    private void checkAndAutoDeleteDesign(Design design) {
        long reportCount = designReportRepository.countByDesign(design);
        
        if (reportCount >= REPORT_THRESHOLD) {
            // Marquer le design comme indisponible et signalé
            design.setAvailable(false);
            design.setReported(true);
            design.setReportedReason("Supprimé automatiquement après " + reportCount + " signalements");
            design.setReportedAt(LocalDateTime.now());
            
            designRepository.save(design);
            
            // Optionnel: Mettre à jour tous les signalements comme approuvés
            List<DesignReport> reports = designReportRepository.findByDesign(design);
            reports.forEach(report -> {
                report.setStatus(ReportStatus.AUTO_DELETED);
            });
            designReportRepository.saveAll(reports);
        }
    }

    /**
     * Obtenir tous les signalements d'un design
     * @param designId ID du design
     * @return Liste des signalements
     */
    public List<DesignReport> getDesignReports(UUID designId) {
        Design design = designRepository.findById(designId)
            .orElseThrow(() -> new RuntimeException("Design non trouvé"));
        
        return designReportRepository.findByDesign(design);
    }

    /**
     * Obtenir le nombre de signalements d'un design
     * @param designId ID du design
     * @return Nombre de signalements
     */
    public long getReportCount(UUID designId) {
        Design design = designRepository.findById(designId)
            .orElseThrow(() -> new RuntimeException("Design non trouvé"));
        
        return designReportRepository.countByDesign(design);
    }

    /**
     * Trouver tous les designs qui ont dépassé le seuil de signalement
     * @return Liste des designs à supprimer
     */
    public List<Design> getDesignsAboveReportThreshold() {
        return designReportRepository.findDesignsWithReportsAboveThreshold(REPORT_THRESHOLD);
    }

    /**
     * Traitement manuel d'un signalement par un admin
     * @param reportId ID du signalement
     * @param status Nouveau statut
     */
    public void processReport(UUID reportId, ReportStatus status) {
        DesignReport report = designReportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        
        report.setStatus(status);
        designReportRepository.save(report);
    }
}