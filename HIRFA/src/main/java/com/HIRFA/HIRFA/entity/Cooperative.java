package com.HIRFA.HIRFA.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table
public class Cooperative {
    @Id
    @GeneratedValue
    public UUID cooperativeId;

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
    private String description;
    private Boolean statut;
    private String adresse;
    private String licence;
    private Boolean statutVerification;

    public UUID getCooperativeId() {
        return cooperativeId;
    }

    public void setCooperativeId(UUID cooperativeId) {
        this.cooperativeId = cooperativeId;
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
