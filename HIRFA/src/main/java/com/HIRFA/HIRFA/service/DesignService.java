package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.Design;
import com.HIRFA.HIRFA.repository.DesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DesignService {

    @Autowired
    private DesignRepository designRepository;

    // Get all designs
    public List<Design> getAllDesigns() {
        return designRepository.findAll();
    }

    // Get design by ID
    public Optional<Design> getDesignById(UUID id) {
        return designRepository.findById(id);
    }

    // Save or update design
    public Design saveDesign(Design design) {
        return designRepository.save(design);
    }

    // Delete design
    public void deleteDesign(UUID id) {
        designRepository.deleteById(id);
    }

    // Get reported designs
    public List<Design> getReportedDesigns() {
        return designRepository.findByIsReportedTrue();
    }

    // Get reported designs with pagination
    public Page<Design> getReportedDesigns(Pageable pageable) {
        return designRepository.findByIsReportedTrue(pageable);
    }

    // Report a design (without User entity)
    public Design reportDesign(UUID designId, String reason, String reporterInfo) {
        Optional<Design> designOpt = designRepository.findById(designId);
        if (designOpt.isPresent()) {
            Design design = designOpt.get();
            design.setReported(true);
            design.setReportedReason(reason);
            design.setReportedBy(reporterInfo); // store reporter name/email instead
            design.setReportedAt(LocalDateTime.now());
            return designRepository.save(design);
        }
        return null;
    }

    // Approve reported design (unreport)
    public Design approveDesign(UUID designId) {
        return designRepository.findById(designId).map(design -> {
            design.setReported(false);
            design.setReportedReason(null);
            design.setReportedBy(null);
            design.setReportedAt(null);
            return designRepository.save(design);
        }).orElse(null);
    }

    // Reject reported design (disable)
    public Design rejectDesign(UUID designId) {
        return designRepository.findById(designId).map(design -> {
            design.setAvailable(false);
            design.setReported(false);
            design.setReportedReason(null);
            design.setReportedBy(null);
            design.setReportedAt(null);
            return designRepository.save(design);
        }).orElse(null);
    }

    public long countAllDesigns() {
        return designRepository.count();
    }
}
