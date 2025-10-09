package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "designers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Designer extends User {
    private String portfolio;
    private String specialites;
    private BigDecimal tarifs;
    
    public Designer() {
        this.setUserType(UserType.DESIGNER);
    }
    
    // Designer-specific fields
    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getSpecialites() {
        return specialites;
    }

    public void setSpecialites(String specialites) {
        this.specialites = specialites;
    }

    public BigDecimal getTarifs() {
        return tarifs;
    }

    public void setTarifs(BigDecimal tarifs) {
        this.tarifs = tarifs;
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
