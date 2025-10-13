package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class ResetPasswordToken {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private LocalDateTime expiryDate;
}
