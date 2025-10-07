package com.HIRFA.HIRFA.dto;

import jakarta.validation.constraints.Email;

public class ClientDTO {
    private String nom;
    private String prenom;
    @Email
    private String email;
    private String username;
    private String telephone;
    private String adresse;
    private String motDePasse;
    private Boolean confirmed;

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

    public Boolean getLConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
/*
 * C’est une classe simple (POJO) utilisée pour transférer les données entre
 * controller ⇆ service.
 * Sécurité : tu n’exposes pas ton entité complète
 * Validation : tu peux ajouter des annotations @NotBlank, @Email, etc. sur les
 * champs DTO.
 */