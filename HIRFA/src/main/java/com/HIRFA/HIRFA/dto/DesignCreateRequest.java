package com.HIRFA.HIRFA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// ===== DTO POUR CRÉER UN DESIGN (CÔTÉ DESIGNER) =====
// Utilisé pour recevoir les données de création de design depuis le frontend

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignCreateRequest {
    private String nomDesign;
    private String description;
    private BigDecimal prix;
    private String title;
    private String category;
    private String imageUrl; // URL de l'image du design
}