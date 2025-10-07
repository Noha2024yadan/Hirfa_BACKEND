package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue
    private UUID docId;

    @Column(nullable = false)
    private String nom;

    private String type;

    private String taille;
}
