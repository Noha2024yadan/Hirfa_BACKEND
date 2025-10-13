package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "panier_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PanierItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // ✅ Modern Hibernate/JPA 3+ UUID generation
    @Column(name = "panier_item_id", updatable = false, nullable = false)
    private UUID panierItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panier_id", nullable = false)
    @JsonBackReference
    private Panier panier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantite;

    @Column(name = "prix_unitaire", precision = 10, scale = 2)
    private BigDecimal prixUnitaire;
}
