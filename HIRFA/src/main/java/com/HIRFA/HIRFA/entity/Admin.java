package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admins")
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID adminId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType = UserType.ADMIN;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "derniere_connexion")
    private LocalDateTime derniereConnexion;

    public Admin() {
        this.userType = UserType.ADMIN;
        this.active = true;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", derniereConnexion=" + derniereConnexion +
                '}';
    }
}
