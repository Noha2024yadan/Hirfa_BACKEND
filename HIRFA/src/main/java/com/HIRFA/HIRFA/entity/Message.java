package com.HIRFA.HIRFA.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue
    private UUID messageId;

    // L'émetteur (peut être une coopérative ou un designer)
    @ManyToOne
    @JoinColumn(name = "cooperative_id", nullable = true)
    private Cooperative cooperativeSender;

    @ManyToOne
    @JoinColumn(name = "designer_id", nullable = true)
    private Designer designerSender;

    // Le destinataire
    @ManyToOne
    @JoinColumn(name = "cooperative_receiver_id", nullable = true)
    private Cooperative cooperativeReceiver;

    @ManyToOne
    @JoinColumn(name = "designer_receiver_id", nullable = true)
    private Designer designerReceiver;

    @Column(nullable = false)
    private String contenu;

    private LocalDateTime dateEnvoi = LocalDateTime.now();

    private String statut; // ENVOYE, RECU, LU
}
