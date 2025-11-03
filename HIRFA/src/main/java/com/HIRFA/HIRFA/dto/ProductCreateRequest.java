package com.HIRFA.HIRFA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// ===== DTO POUR CRÉER UN PRODUIT (CÔTÉ COOPÉRATIVE) =====
// Utilisé pour recevoir les données de création de produit depuis le frontend

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String category;
    private BigDecimal poids;
    private String dimensions;
    private List<String> imageUrls; // URLs des images du produit
}