package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    private UUID productId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private BigDecimal price;

    @Column(nullable = false)
    private int stockQuantity;

    private String category;

    private BigDecimal poids;

    private String dimensions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean statut;

    private int signalements;

    private boolean isReported;

    private String reportedReason;

    @ManyToOne
    private User reportedBy;

    private LocalDateTime reportedAt;

    private boolean isAvailable;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Default
    private List<PanierItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Default
    private List<Image> images = new ArrayList<>();
}
