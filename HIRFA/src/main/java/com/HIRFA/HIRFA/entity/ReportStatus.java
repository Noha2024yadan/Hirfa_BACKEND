package com.HIRFA.HIRFA.entity;

// ===== ENUM POUR LE STATUT DES SIGNALEMENTS =====
// Définit l'état d'un signalement dans le système

public enum ReportStatus {
    PENDING("En attente"),
    REVIEWED("Examiné"),
    APPROVED("Approuvé"),
    REJECTED("Rejeté"),
    AUTO_DELETED("Supprimé automatiquement");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
