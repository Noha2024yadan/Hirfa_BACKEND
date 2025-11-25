package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cooperatives")
@Data
public class Cooperative {

    @Id
    @GeneratedValue
    private UUID id;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String username;
    private String motDePasse;
    private boolean active = true;

    private String name;
    private String description;
    private String address;
    private String statutVerification;

    public UUID getUserId() {
        return this.id;
    }

    private boolean enabled;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereConnexion;
    private UserType userType;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getBrand() {
        throw new UnsupportedOperationException("Unimplemented method 'getBrand'");
    }

}
