package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

// ===== ENTITÉ DE SIGNALEMENT POUR LES DESIGNS =====
// Cette entité permet aux utilisateurs de signaler des designs inappropriés
// Si un design reçoit plus de 5 signalements, il sera automatiquement supprimé

@Entity
@Table(name = "design_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignReport {

    @Id
    @GeneratedValue
    private UUID reportId;

    // Le design signalé
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "design_id", nullable = false)
    private Design design;

    // L'utilisateur qui a signalé le design
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    // Raison du signalement
    @Column(nullable = false)
    private String reason;

    // Description détaillée (optionnelle)
    @Column(length = 1000)
    private String description;

    // Type de signalement
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    // Date et heure du signalement
    @Column(nullable = false)
    private LocalDateTime reportedAt;

    // Statut du signalement (en attente, traité, rejeté)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;

    @PrePersist
    protected void onCreate() {
        this.reportedAt = LocalDateTime.now();
    }
}