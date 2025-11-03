package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.entity.*;
import com.HIRFA.HIRFA.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// ===== SERVICE CRUD POUR LES DESIGNS CÔTÉ DESIGNER =====
// Gère toutes les opérations CRUD des designs pour les designers

@Service
@RequiredArgsConstructor
@Transactional
public class DesignCrudService {

    private final DesignRepository designRepository;
    private final DesignerRepository designerRepository;

    /**
     * Créer un nouveau design
     * @param request Données du design à créer
     * @param designerId ID du designer qui crée le design
     * @return Le design créé
     */
    public DesignResponse createDesign(DesignCreateRequest request, UUID designerId) {
        Designer designer = designerRepository.findById(designerId)
            .orElseThrow(() -> new RuntimeException("Designer non trouvé"));

        Design design = Design.builder()
            .nomDesign(request.getNomDesign())
            .description(request.getDescription())
            .prix(request.getPrix())
            .title(request.getTitle())
            .category(request.getCategory())
            .imageUrl(request.getImageUrl())
            .designer(designer)
            .dateCreation(LocalDateTime.now())
            .statut(true) // Par défaut actif
            .isAvailable(true) // Par défaut disponible
            .isReported(false)
            .build();

        Design savedDesign = designRepository.save(design);
        return convertToDesignResponse(savedDesign);
    }

    /**
     * Mettre à jour un design existant
     * @param designId ID du design à mettre à jour
     * @param request Nouvelles données du design
     * @param designerId ID du designer (pour vérification des droits)
     * @return Le design mis à jour
     */
    public DesignResponse updateDesign(UUID designId, DesignUpdateRequest request, UUID designerId) {
        Design design = designRepository.findById(designId)
            .orElseThrow(() -> new RuntimeException("Design non trouvé"));

        // Vérifier que le design appartient bien à ce designer
        if (!design.getDesigner().getUserId().equals(designerId)) {
            throw new RuntimeException("Vous n'avez pas le droit de modifier ce design");
        }

        // Mettre à jour les champs
        if (request.getNomDesign() != null) design.setNomDesign(request.getNomDesign());
        if (request.getDescription() != null) design.setDescription(request.getDescription());
        if (request.getPrix() != null) design.setPrix(request.getPrix());
        if (request.getTitle() != null) design.setTitle(request.getTitle());
        if (request.getCategory() != null) design.setCategory(request.getCategory());
        if (request.getImageUrl() != null) design.setImageUrl(request.getImageUrl());
        if (request.getStatut() != null) design.setStatut(request.getStatut());
        if (request.getIsAvailable() != null) design.setAvailable(request.getIsAvailable());

        Design savedDesign = designRepository.save(design);
        return convertToDesignResponse(savedDesign);
    }

    /**
     * Obtenir un design par son ID
     * @param designId ID du design
     * @param designerId ID du designer (pour vérification des droits)
     * @return Le design trouvé
     */
    @Transactional(readOnly = true)
    public DesignResponse getDesign(UUID designId, UUID designerId) {
        Design design = designRepository.findById(designId)
            .orElseThrow(() -> new RuntimeException("Design non trouvé"));

        // Vérifier que le design appartient bien à ce designer
        if (!design.getDesigner().getUserId().equals(designerId)) {
            throw new RuntimeException("Vous n'avez pas le droit de voir ce design");
        }

        return convertToDesignResponse(design);
    }

    /**
     * Obtenir tous les designs d'un designer avec pagination
     * @param designerId ID du designer
     * @param pageable Paramètres de pagination
     * @return Page de designs
     */
    @Transactional(readOnly = true)
    public Page<DesignResponse> getDesignsByDesigner(UUID designerId, Pageable pageable) {
        Designer designer = designerRepository.findById(designerId)
            .orElseThrow(() -> new RuntimeException("Designer non trouvé"));

        Page<Design> designsPage = designRepository.findByDesigner(designer, pageable);
        
        List<DesignResponse> designResponses = designsPage.getContent().stream()
            .map(this::convertToDesignResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(designResponses, pageable, designsPage.getTotalElements());
    }

    /**
     * Supprimer un design (soft delete)
     * @param designId ID du design à supprimer
     * @param designerId ID du designer (pour vérification des droits)
     */
    public void deleteDesign(UUID designId, UUID designerId) {
        Design design = designRepository.findById(designId)
            .orElseThrow(() -> new RuntimeException("Design non trouvé"));

        // Vérifier que le design appartient bien à ce designer
        if (!design.getDesigner().getUserId().equals(designerId)) {
            throw new RuntimeException("Vous n'avez pas le droit de supprimer ce design");
        }

        // Soft delete : marquer comme indisponible au lieu de supprimer
        design.setAvailable(false);
        design.setStatut(false);
        
        designRepository.save(design);
    }

    /**
     * Rechercher des designs par nom ou catégorie
     * @param designerId ID du designer
     * @param searchTerm Terme de recherche
     * @param pageable Paramètres de pagination
     * @return Page de designs correspondants
     */
    @Transactional(readOnly = true)
    public Page<DesignResponse> searchDesigns(UUID designerId, String searchTerm, Pageable pageable) {
        Designer designer = designerRepository.findById(designerId)
            .orElseThrow(() -> new RuntimeException("Designer non trouvé"));

        Page<Design> designsPage = designRepository
            .findByDesignerAndNomDesignContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                designer, searchTerm, searchTerm, pageable);
        
        List<DesignResponse> designResponses = designsPage.getContent().stream()
            .map(this::convertToDesignResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(designResponses, pageable, designsPage.getTotalElements());
    }

    /**
     * Obtenir les designs actifs seulement
     * @param designerId ID du designer
     * @param pageable Paramètres de pagination
     * @return Page de designs actifs
     */
    @Transactional(readOnly = true)
    public Page<DesignResponse> getActiveDesigns(UUID designerId, Pageable pageable) {
        Designer designer = designerRepository.findById(designerId)
            .orElseThrow(() -> new RuntimeException("Designer non trouvé"));

        Page<Design> designsPage = designRepository.findByDesignerAndStatutTrue(designer, pageable);
        
        List<DesignResponse> designResponses = designsPage.getContent().stream()
            .map(this::convertToDesignResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(designResponses, pageable, designsPage.getTotalElements());
    }

    /**
     * Obtenir les statistiques des designs d'un designer
     * @param designerId ID du designer
     * @return Statistiques des designs
     */
    @Transactional(readOnly = true)
    public DesignStats getDesignStats(UUID designerId) {
        Designer designer = designerRepository.findById(designerId)
            .orElseThrow(() -> new RuntimeException("Designer non trouvé"));

        long totalDesigns = designRepository.countByDesigner(designer);
        long activeDesigns = designRepository.countByDesignerAndStatutTrue(designer);

        return DesignStats.builder()
            .totalDesigns(totalDesigns)
            .activeDesigns(activeDesigns)
            .inactiveDesigns(totalDesigns - activeDesigns)
            .build();
    }

    // ===== MÉTHODES UTILITAIRES PRIVÉES =====

    private DesignResponse convertToDesignResponse(Design design) {
        return DesignResponse.builder()
            .designId(design.getDesignId())
            .nomDesign(design.getNomDesign())
            .description(design.getDescription())
            .prix(design.getPrix())
            .dateCreation(design.getDateCreation())
            .statut(design.getStatut())
            .title(design.getTitle())
            .category(design.getCategory())
            .imageUrl(design.getImageUrl())
            .isAvailable(design.isAvailable())
            .isReported(design.isReported())
            .reportedReason(design.getReportedReason())
            .reportedAt(design.getReportedAt())
            .designerName(design.getDesigner().getNom() + " " + design.getDesigner().getPrenom())
            .designerUsername(design.getDesigner().getUsername())
            .build();
    }

    // Classe interne pour les statistiques des designs
    @lombok.Data
    @lombok.Builder
    public static class DesignStats {
        private long totalDesigns;
        private long activeDesigns;
        private long inactiveDesigns;
    }
}