package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "designers")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Designer extends User {
    private String portfolio;
    private String specialites;
    private BigDecimal tarifs;
    private Boolean confirmed;

    public Designer() {
        this.setUserType(UserType.DESIGNER);
    }

    // Override UserDetails interface methods
    @Override
    public UUID getUserId() {
        return super.getUserId();
    }

    @Override
    public String getMotDePasse() {
        return super.getMotDePasse();
    }

    @Override
    public UserType getUserType() {
        return UserType.DESIGNER;
    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }

    @Override
    public void setDerniereConnexion(LocalDateTime now) {
        super.setDerniereConnexion(now);
    }
}
