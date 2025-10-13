package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue
    private UUID sessionId;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;
}
