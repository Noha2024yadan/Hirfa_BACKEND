package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.*;
import com.HIRFA.HIRFA.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DesignReportService {

    private final DesignReportRepository designReportRepository;
    private final DesignRepository designRepository;
    private final ClientRepository clientRepository;
    private final DesignerRepository designerRepository;
    private final CooperativeRepository cooperativeRepository;

    private static final long REPORT_THRESHOLD = 5;

    public DesignReport reportDesign(UUID designId, UUID reporterId, ReportType reportType,
            String reason, String description) {

        Design design = designRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design non trouvé"));

        Object reporter = findUserById(reporterId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (designReportRepository.existsByDesignAndReporter(design, reporter)) {
            throw new RuntimeException("Vous avez déjà signalé ce design");
        }

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

        checkAndAutoDeleteDesign(design);

        return savedReport;
    }

    private Optional<Object> findUserById(UUID id) {
        return clientRepository.findById(id)
                .map(u -> (Object) u)
                .or(() -> designerRepository.findById(id).map(u -> (Object) u))
                .or(() -> cooperativeRepository.findById(id).map(u -> (Object) u));
    }

    private void checkAndAutoDeleteDesign(Design design) {
        long reportCount = designReportRepository.countByDesign(design);

        if (reportCount >= REPORT_THRESHOLD) {
            design.setAvailable(false);
            design.setReported(true);
            design.setReportedReason("Supprimé automatiquement après " + reportCount + " signalements");
            design.setReportedAt(LocalDateTime.now());

            designRepository.save(design);

            List<DesignReport> reports = designReportRepository.findByDesign(design);
            reports.forEach(report -> report.setStatus(ReportStatus.AUTO_DELETED));
            designReportRepository.saveAll(reports);
        }
    }

    public List<DesignReport> getDesignReports(UUID designId) {
        Design design = designRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design non trouvé"));
        return designReportRepository.findByDesign(design);
    }

    public long getReportCount(UUID designId) {
        Design design = designRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design non trouvé"));
        return designReportRepository.countByDesign(design);
    }

    public List<Design> getDesignsAboveReportThreshold() {
        return designReportRepository.findDesignsWithReportsAboveThreshold(REPORT_THRESHOLD);
    }

    public void processReport(UUID reportId, ReportStatus status) {
        DesignReport report = designReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        report.setStatus(status);
        designReportRepository.save(report);
    }
}
