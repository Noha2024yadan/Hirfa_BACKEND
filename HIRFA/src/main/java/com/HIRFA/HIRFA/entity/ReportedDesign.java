package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reported_designs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportedDesign {

    @Id
    @GeneratedValue
    private UUID reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "design_id", nullable = false)
    private Design design;

    @Column(nullable = false)
    private String reporterType;

    @Column(nullable = false)
    private UUID reporterId;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDateTime reportDate;
}
