package com.HIRFA.HIRFA.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cooperatives")
@Data               // génère automatiquement getters, setters, toString, equals, hashCode
@NoArgsConstructor  // génère un constructeur vide
@AllArgsConstructor // génère un constructeur avec tous les attributs
@Builder            // permet d'utiliser le pattern Builder pour créer des objets facilement
public class Cooperative {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID cooperativeId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String brand;

    private String telephone;
    private String motDePasse;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereConnexion;
    private String description;
    private Boolean confirmed;
    private String adresse;
    private String licence;
    private Boolean statutVerification;


    private String confirmationCode;
}
