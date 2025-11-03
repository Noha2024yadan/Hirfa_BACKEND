package com.HIRFA.HIRFA.entity;

// ===== ENUM POUR LES TYPES DE SIGNALEMENT =====
// Définit les différents types de signalements possibles pour les produits et designs

public enum ReportType {
    INAPPROPRIATE_CONTENT("Contenu inapproprié"),
    FAKE_PRODUCT("Produit factice"),
    COPYRIGHT_VIOLATION("Violation de droits d'auteur"),
    SPAM("Spam"),
    MISLEADING_INFORMATION("Informations trompeuses"),
    OFFENSIVE_CONTENT("Contenu offensant"),
    OTHER("Autre");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}