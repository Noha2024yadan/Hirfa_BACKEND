package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder

public class Designer {
    @Id
    @GeneratedValue
    public UUID designerId;

    private String nom;
    private String prenom;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(unique = true)
    private String username;

    private String telephone;
    private String motDePasse;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereConnexion;
    private String portfolio;
    private String specialites;
    private Boolean statut;
    private BigDecimal tarifs;

   

   
}
