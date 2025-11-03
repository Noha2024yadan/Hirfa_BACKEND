package com.HIRFA.HIRFA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// ===== DTO POUR LES INFORMATIONS DE BASE D'UNE COOPÉRATIVE =====
// Utilisé pour afficher les informations essentielles côté designer

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CooperativeBasicInfoDto {
    private UUID cooperativeId;
    private String brand;
    private String description;
    private String adresse;
    private String email;
    private String telephone;
    private String statutVerification;
    private boolean isActive;
    
    // Informations statistiques
    private long totalProducts;
    private long activeProducts;
    private String memberSince; // Date d'inscription formatée
}