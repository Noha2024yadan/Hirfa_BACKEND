package com.HIRFA.HIRFA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// ===== DTO POUR LA RÉPONSE PRODUIT (CÔTÉ COOPÉRATIVE) =====
// Utilisé pour retourner les données de produit vers le frontend

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String category;
    private BigDecimal poids;
    private String dimensions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean statut;
    private boolean isAvailable;
    private boolean isReported;
    private String reportedReason;
    private int signalements;
    private List<String> imageUrls; // URLs des images
    private String cooperativeName; // Nom de la coopérative
}