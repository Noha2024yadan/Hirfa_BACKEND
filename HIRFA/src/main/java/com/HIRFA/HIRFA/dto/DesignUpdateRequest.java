package com.HIRFA.HIRFA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// ===== DTO POUR METTRE À JOUR UN DESIGN (CÔTÉ DESIGNER) =====
// Utilisé pour recevoir les données de modification de design depuis le frontend

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignUpdateRequest {
    private String nomDesign;
    private String description;
    private BigDecimal prix;
    private String title;
    private String category;
    private String imageUrl;
    private Boolean statut; // Actif/Inactif
    private Boolean isAvailable; // Disponible/Non disponible
}