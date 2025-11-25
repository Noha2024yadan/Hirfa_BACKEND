package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.HIRFA.HIRFA.entity.Designer;

@Entity
@Table(name = "designers")
@Data
@NoArgsConstructor
public class Designer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    private String username;
    private String email;
    private String motDePasse;
    private String nom;
    private String prenom;
    private String telephone;

    private LocalDateTime derniereConnexion;
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.DESIGNER;

    // Designer-specific fields
    private String portfolio;
    private String specialites;
    private BigDecimal tarifs;
    private Boolean confirmed = false;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Utility methods
    public boolean isActive() {
        return active;
    }

    public void setDerniereConnexion(LocalDateTime now) {
        this.derniereConnexion = now;
    }

    private boolean enabled;
    private LocalDateTime dateCreation;

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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

}
