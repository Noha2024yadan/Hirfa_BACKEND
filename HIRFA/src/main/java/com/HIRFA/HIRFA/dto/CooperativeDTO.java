package com.HIRFA.HIRFA.dto;

import jakarta.validation.constraints.Email;

public class CooperativeDTO {
    @Email
    private String email;
    private String brand;
    private String telephone;
    private String adresse;
    private String motDePasse;
    private String description;
    private String licence;
    private Boolean statutVerification;
    private Boolean confirmed;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBrand() {
        return brand;
    }

    public void setUBrand(String brand) {
        this.brand = brand;
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