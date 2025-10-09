package com.HIRFA.HIRFA.dto;

import jakarta.validation.constraints.NotBlank;

public class CooperativeRegistrationDto extends BaseUserRegistrationDto {

    private String description;

    @NotBlank(message = "Address is required")
    private String adresse;

    @NotBlank(message = "Licence number is required")
    private String licence;

    // Getters and Setters
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

    public Object getConfirmMotDePasse() {

        throw new UnsupportedOperationException("Unimplemented method 'getConfirmMotDePasse'");
    }
}
