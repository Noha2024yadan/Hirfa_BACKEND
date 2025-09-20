package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class Designer {
    @Id
    @GeneratedValue
    public UUID designerId;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String telephone;
    private String motDePasse;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereConnexion;
    private String portfolio;
    private String specialites;
    private Boolean statut;
    private BigDecimal tarifs;

    public UUID getDesignerId() {
        return designerId;
    }

    public void setDesignerId(UUID designerId) {
        this.designerId = designerId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

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
}
