package com.HIRFA.HIRFA.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;

public class DesignerBasicDTO {
    private String nom;
    private String prenom;
    @Email
    private String email;
    private String username;
    private String telephone;
    private String portfolio;
    private String specialites;
    private BigDecimal tarifs;

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
