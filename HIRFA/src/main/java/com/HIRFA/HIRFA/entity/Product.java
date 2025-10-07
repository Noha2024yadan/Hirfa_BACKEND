package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String nomProduit;

    @Column(length = 1000)
    private String description;

    private BigDecimal prix;

    @Column(nullable = false)
    private int quantiteStock;

    private String categorie;

    private BigDecimal poids;

    private String dimensions;

    private LocalDateTime dateCreation;

    private Boolean statut;

    private int signalements;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<PanierItem> items;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

}