package com.HIRFA.HIRFA.dto;

import com.HIRFA.HIRFA.entity.ReportType;
import lombok.Data;

import java.util.UUID;

// ===== DTO POUR LES REQUÊTES DE SIGNALEMENT DE PRODUITS =====
// Utilisé pour recevoir les données de signalement depuis le frontend

@Data
public class ProductReportRequest {
    private UUID productId;
    private UUID reporterId;
    private ReportType reportType;
    private String reason;
    private String description; // optionnel
}