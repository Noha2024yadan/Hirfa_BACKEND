package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue
    private UUID id;

    private String nom;
    private String prenom;
    private String email;

    @Column(unique = true)
    private String username;

    private String telephone;
    private String motDePasse;
    private String adresse;
    private LocalDateTime dateCreation;
    private boolean statut;

    // ===== Constructors =====
    public Client() {
    }

    public Client(String nom, String prenom, String email, String username, String telephone, String motDePasse,
            String adresse, boolean statut) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.username = username;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
        this.adresse = adresse;
        this.dateCreation = LocalDateTime.now();
        this.statut = statut;
    }

    // ===== Getters & Setters =====
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    private boolean enabled;

    private LocalDateTime derniereConnexion;
    private UserType userType;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setClientId(UUID clientId) {
        throw new UnsupportedOperationException("Unimplemented method 'setClientId'");
    }

}
