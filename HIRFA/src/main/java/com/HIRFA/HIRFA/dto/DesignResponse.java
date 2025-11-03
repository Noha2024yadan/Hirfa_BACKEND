package com.HIRFA.HIRFA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// ===== DTO POUR LA RÉPONSE DESIGN (CÔTÉ DESIGNER) =====
// Utilisé pour retourner les données de design vers le frontend

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignResponse {
    private UUID designId;
    private String nomDesign;
    private String description;
    private BigDecimal prix;
    private LocalDateTime dateCreation;
    private Boolean statut;
    private String title;
    private String category;
    private String imageUrl;
    private boolean isAvailable;
    private boolean isReported;
    private String reportedReason;
    private LocalDateTime reportedAt;
    private String designerName; // Nom du designer
    private String designerUsername; // Username du designer
}