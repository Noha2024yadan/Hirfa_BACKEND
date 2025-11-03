package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.service.AutoModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// ===== CONTRÔLEUR POUR LA MODÉRATION AUTOMATIQUE =====
// API pour les administrateurs pour gérer la modération automatique

@RestController
@RequestMapping("/api/admin/moderation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ModerationController {

    private final AutoModerationService autoModerationService;

    /**
     * Déclencher manuellement la modération
     * POST /api/admin/moderation/trigger
     */
    @PostMapping("/trigger")
    public ResponseEntity<?> triggerModeration() {
        try {
            autoModerationService.triggerManualModeration();
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Modération déclenchée avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les statistiques de modération
     * GET /api/admin/moderation/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getModerationStats() {
        try {
            AutoModerationService.ModerationStats stats = autoModerationService.getModerationStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}