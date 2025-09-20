package com.HIRFA.HIRFA.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table
public class Cooperative {
    @Id
    @GeneratedValue
    public UUID cooperativeId;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String brand;

    private String telephone;
    private String motDePasse;
    @CreationTimestamp
    private LocalDateTime dateCreation;
    private LocalDateTime derniereConnexion;
    private String description;
    private Boolean confirmed;
    private String adresse;
    private String licence;
    private Boolean statutVerification;

    public UUID getCooperativeId() {
        return cooperativeId;
    }

    public void setCooperativeId(UUID cooperativeId) {
        this.cooperativeId = cooperativeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Boolean getLConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public Boolean getStatutVerification() {
        return statutVerification;
    }

    public void setStatutVerification(Boolean statutVerification) {
        this.statutVerification = statutVerification;
    }
}
