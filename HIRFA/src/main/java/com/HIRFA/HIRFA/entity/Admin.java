package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    
    public Admin() {
        this.setUserType(UserType.ADMIN);
    }
    
    // Admin-specific fields and methods can be added here
    
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
        return UserType.ADMIN;
    }
    
    @Override
    public boolean isActive() {
        return super.isActive();
    }
    
    @Override
    public void setDerniereConnexion(java.time.LocalDateTime now) {
        super.setDerniereConnexion(now);
    }
}