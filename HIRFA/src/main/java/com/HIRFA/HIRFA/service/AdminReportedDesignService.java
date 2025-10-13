package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.ReportedDesignAdminView;
import com.HIRFA.HIRFA.entity.Design;
import com.HIRFA.HIRFA.entity.Image;
import com.HIRFA.HIRFA.entity.ReportStatus;
import com.HIRFA.HIRFA.entity.ReportedDesign;
import com.HIRFA.HIRFA.repository.DesignRepository;
import com.HIRFA.HIRFA.repository.ImageRepository;
import com.HIRFA.HIRFA.repository.ReportedDesignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReportedDesignService {

    private final ReportedDesignRepository reportedDesignRepository;
    private final ImageRepository imageRepository;
    private final DesignRepository designRepository;

    public List<ReportedDesignAdminView> getAllReportedDesigns() {
        return reportedDesignRepository.findAll().stream().map(report -> {
            Design d = report.getDesign();
            List<String> images = imageRepository.findByDesign_DesignId(d.getDesignId())
                    .stream().map(Image::getUrl).collect(Collectors.toList());

            return ReportedDesignAdminView.builder()
                    .reportId(report.getReportId())
                    .reason(report.getReason())
                    .status(report.getStatus().name())
                    .reportDate(report.getReportDate())
                    .designId(d.getDesignId())
                    .nomDesign(d.getNomDesign())
                    .description(d.getDescription())
                    .prix(d.getPrix().doubleValue())
                    .imageUrls(images)
                    .build();
        }).collect(Collectors.toList());
    }

    public void deleteDesign(UUID designId) {
        Design design = designRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design introuvable"));
        designRepository.delete(design);
    }

    public ReportedDesign ignoreReport(UUID reportId) {
        ReportedDesign report = reportedDesignRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report introuvable"));
        report.setStatus(ReportStatus.REJECTED);
        return reportedDesignRepository.save(report);
    }
}
