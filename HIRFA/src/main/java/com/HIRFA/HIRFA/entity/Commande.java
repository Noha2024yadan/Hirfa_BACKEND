package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue
    private UUID commandeId;

    @Column(nullable = false)
    private LocalDateTime dateCommande;

    @Column(nullable = false)
    private String statut;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    private String adresseLivraison;

    private String notesClient;

    private LocalDateTime dateLivraison;
}
