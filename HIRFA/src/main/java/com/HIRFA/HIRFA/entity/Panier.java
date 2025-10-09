package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "paniers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Panier {

    @Id
    @GeneratedValue
    private UUID panierId;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;
    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PanierItem> items = new ArrayList<>();

    public BigDecimal calculerTotal() {
        return items.stream()
                .map(item -> item.getPrixUnitaire().multiply(BigDecimal.valueOf(item.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @PrePersist
    public void prePersist() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDateTime.now();  // Initialiser date_creation
        }
        if (this.dateModification == null) {
            this.dateModification = LocalDateTime.now();  // Initialiser date_modification
        }
    }
}
