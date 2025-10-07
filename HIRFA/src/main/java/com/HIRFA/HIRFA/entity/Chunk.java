package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chunk {

    @Id
    @GeneratedValue
    private UUID chunkId;

    @Column(columnDefinition = "TEXT")
    private String contenu;
}
