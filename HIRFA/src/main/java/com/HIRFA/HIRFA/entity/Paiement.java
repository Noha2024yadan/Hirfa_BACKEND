package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue
    private UUID paiementId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false)
    private String methodePaiement;

    @Column(nullable = false)
    private String statutPaiement;

    @Column(nullable = false)
    private LocalDateTime datePaiement;

    private String referenceTransaction;
}
