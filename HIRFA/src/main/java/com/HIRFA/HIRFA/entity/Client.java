package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends User {
    private String adresse;

    @Column(nullable = false)
    private boolean emailVerified = false;

    private UUID clientId;

    @Column(nullable = false)
    private boolean statut = true;

    public Client() {
        this.setUserType(UserType.CLIENT);
    }
}
