package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.ReportedDesignAdminView;
import com.HIRFA.HIRFA.entity.ReportedDesign;
import com.HIRFA.HIRFA.service.AdminReportedDesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/reported-designs")
@RequiredArgsConstructor
public class AdminReportedDesignController {

    private final AdminReportedDesignService adminReportedDesignService;

    @GetMapping
    public ResponseEntity<List<ReportedDesignAdminView>> getAllReportedDesigns() {
        return ResponseEntity.ok(adminReportedDesignService.getAllReportedDesigns());
    }

    @DeleteMapping("/{designId}")
    public ResponseEntity<Void> deleteDesign(@PathVariable UUID designId) {
        adminReportedDesignService.deleteDesign(designId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/ignore/{reportId}")
    public ResponseEntity<ReportedDesign> ignoreReport(@PathVariable UUID reportId) {
        return ResponseEntity.ok(adminReportedDesignService.ignoreReport(reportId));
    }
}
