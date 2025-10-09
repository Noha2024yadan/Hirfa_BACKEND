package com.HIRFA.HIRFA.dto;

public class JwtResponseDto {
    private String token;
    private String type = "Bearer";

    public JwtResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}