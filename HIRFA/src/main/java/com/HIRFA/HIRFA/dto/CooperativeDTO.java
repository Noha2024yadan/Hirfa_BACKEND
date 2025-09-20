package com.HIRFA.HIRFA.dto;

import jakarta.validation.constraints.Email;

public class CooperativeDTO {
    private String nom;
    private String prenom;
    @Email
    private String email;
    private String username;
    private String telephone;
    private String adresse;
    private String motDePasse;
    private String description;
    private String licence;
    private Boolean statutVerification;

    // Getters & setters
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
/*
 * C’est une classe simple (POJO) utilisée pour transférer les données entre
 * controller ⇆ service.
 * Sécurité : tu n’exposes pas ton entité complète
 * Validation : tu peux ajouter des annotations @NotBlank, @Email, etc. sur les
 * champs DTO.
 */