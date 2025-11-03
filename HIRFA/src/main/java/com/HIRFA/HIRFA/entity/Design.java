package com.HIRFA.HIRFA.entity;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "designs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Design {

    @Id
    @GeneratedValue
    private UUID designId;

    @Column(nullable = false)
    private String nomDesign;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private Boolean statut;

    private String title;
    private String category;
    private String imageUrl;
    private boolean isAvailable;
    private boolean isReported;
    private String reportedReason;
    @ManyToOne
    private User reportedBy;
    private LocalDateTime reportedAt;

    // ===== RELATION AVEC DESIGNER =====
    // Chaque design appartient à un designer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    private Designer designer;
}
