package com.HIRFA.HIRFA.dto;

import java.math.BigDecimal;

public class DesignerUpdateDto {
    private String email;
    private String nom;
    private String prenom;
    private String telephone;
    private String portfolio;
    private String specialites;
    private BigDecimal tarifs;

    // Getters / Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
