package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.service.DesignCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

// ===== CONTRÔLEUR CRUD POUR LES DESIGNS CÔTÉ DESIGNER =====
// API REST pour que les designers gèrent leurs designs (Create, Read, Update, Delete)

@RestController
@RequestMapping("/api/designer/designs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DesignCrudController {

    private final DesignCrudService designCrudService;

    /**
     * Créer un nouveau design
     * POST /api/designer/designs
     */
    @PostMapping
    public ResponseEntity<?> createDesign(
            @RequestBody DesignCreateRequest request,
            @RequestParam UUID designerId) {
        try {
            DesignResponse design = designCrudService.createDesign(request, designerId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Design créé avec succès",
                "design", design
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Mettre à jour un design existant
     * PUT /api/designer/designs/{designId}
     */
    @PutMapping("/{designId}")
    public ResponseEntity<?> updateDesign(
            @PathVariable UUID designId,
            @RequestBody DesignUpdateRequest request,
            @RequestParam UUID designerId) {
        try {
            DesignResponse design = designCrudService.updateDesign(designId, request, designerId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Design mis à jour avec succès",
                "design", design
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir un design par son ID
     * GET /api/designer/designs/{designId}
     */
    @GetMapping("/{designId}")
    public ResponseEntity<?> getDesign(
            @PathVariable UUID designId,
            @RequestParam UUID designerId) {
        try {
            DesignResponse design = designCrudService.getDesign(designId, designerId);
            return ResponseEntity.ok(design);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir tous les designs d'un designer avec pagination
     * GET /api/designer/designs
     */
    @GetMapping
    public ResponseEntity<?> getDesigns(
            @RequestParam UUID designerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreation") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<DesignResponse> designs = designCrudService.getDesignsByDesigner(designerId, pageable);
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "designs", designs.getContent(),
                "currentPage", designs.getNumber(),
                "totalPages", designs.getTotalPages(),
                "totalElements", designs.getTotalElements(),
                "hasNext", designs.hasNext(),
                "hasPrevious", designs.hasPrevious()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les designs actifs seulement
     * GET /api/designer/designs/active
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveDesigns(
            @RequestParam UUID designerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreation") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<DesignResponse> designs = designCrudService.getActiveDesigns(designerId, pageable);
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "designs", designs.getContent(),
                "currentPage", designs.getNumber(),
                "totalPages", designs.getTotalPages(),
                "totalElements", designs.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Rechercher des designs
     * GET /api/designer/designs/search
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchDesigns(
            @RequestParam UUID designerId,
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreation") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<DesignResponse> designs = designCrudService.searchDesigns(designerId, searchTerm, pageable);
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "designs", designs.getContent(),
                "searchTerm", searchTerm,
                "currentPage", designs.getNumber(),
                "totalPages", designs.getTotalPages(),
                "totalElements", designs.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Supprimer un design (soft delete)
     * DELETE /api/designer/designs/{designId}
     */
    @DeleteMapping("/{designId}")
    public ResponseEntity<?> deleteDesign(
            @PathVariable UUID designId,
            @RequestParam UUID designerId) {
        try {
            designCrudService.deleteDesign(designId, designerId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Design supprimé avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les statistiques des designs d'un designer
     * GET /api/designer/designs/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getDesignStats(@RequestParam UUID designerId) {
        try {
            DesignCrudService.DesignStats stats = designCrudService.getDesignStats(designerId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "stats", stats
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}