package com.HIRFA.HIRFA.dto;

import jakarta.validation.constraints.NotBlank;

public class DesignerRegistrationDto extends BaseUserRegistrationDto {

    @NotBlank(message = "Portfolio URL is required")
    private String portfolio;

    private String specialites;

    // Getters and Setters
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

    public Object getConfirmMotDePasse() {
        throw new UnsupportedOperationException("Unimplemented method 'getConfirmMotDePasse'");
    }
}
