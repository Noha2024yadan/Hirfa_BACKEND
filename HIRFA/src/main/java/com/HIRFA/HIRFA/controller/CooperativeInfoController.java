package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.CooperativeBasicInfoDto;
import com.HIRFA.HIRFA.service.CooperativeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// ===== CONTRÔLEUR POUR LES INFORMATIONS DE COOPÉRATIVES CÔTÉ DESIGNER =====
// API REST pour permettre aux designers de consulter les informations des coopératives

@RestController
@RequestMapping("/api/designer/cooperatives")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CooperativeInfoController {

    private final CooperativeInfoService cooperativeInfoService;

    /**
     * Obtenir les informations de base d'une coopérative spécifique
     * GET /api/designer/cooperatives/{cooperativeId}
     */
    @GetMapping("/{cooperativeId}")
    public ResponseEntity<?> getCooperativeBasicInfo(@PathVariable UUID cooperativeId) {
        try {
            CooperativeBasicInfoDto info = cooperativeInfoService.getCooperativeBasicInfo(cooperativeId);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir toutes les coopératives actives
     * GET /api/designer/cooperatives
     */
    @GetMapping
    public ResponseEntity<List<CooperativeBasicInfoDto>> getAllActiveCooperatives() {
        List<CooperativeBasicInfoDto> cooperatives = cooperativeInfoService.getAllActiveCooperatives();
        return ResponseEntity.ok(cooperatives);
    }

    /**
     * Rechercher des coopératives par nom de marque
     * GET /api/designer/cooperatives/search?brand={brand}
     */
    @GetMapping("/search")
    public ResponseEntity<List<CooperativeBasicInfoDto>> searchCooperativesByBrand(
            @RequestParam String brand) {
        List<CooperativeBasicInfoDto> cooperatives = cooperativeInfoService.searchCooperativesByBrand(brand);
        return ResponseEntity.ok(cooperatives);
    }

    /**
     * Obtenir les coopératives par ville/région
     * GET /api/designer/cooperatives/location?city={city}
     */
    @GetMapping("/location")
    public ResponseEntity<List<CooperativeBasicInfoDto>> getCooperativesByLocation(
            @RequestParam String city) {
        List<CooperativeBasicInfoDto> cooperatives = cooperativeInfoService.getCooperativesByLocation(city);
        return ResponseEntity.ok(cooperatives);
    }

    /**
     * Obtenir un résumé des statistiques des coopératives
     * GET /api/designer/cooperatives/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getCooperativesStats() {
        try {
            List<CooperativeBasicInfoDto> allCooperatives = cooperativeInfoService.getAllActiveCooperatives();
            
            Map<String, Object> stats = Map.of(
                "totalActiveCooperatives", allCooperatives.size(),
                "averageProductsPerCooperative", allCooperatives.stream()
                    .mapToLong(CooperativeBasicInfoDto::getTotalProducts)
                    .average()
                    .orElse(0.0),
                "cooperativesWithProducts", allCooperatives.stream()
                    .filter(coop -> coop.getTotalProducts() > 0)
                    .count()
            );
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}