package com.HIRFA.HIRFA.entity;

import java.util.UUID;

public interface UserDetails {
    UUID getUserId();
    String getUsername();
    String getEmail();
    String getMotDePasse();
    UserType getUserType();
    boolean isActive();
    void setDerniereConnexion(java.time.LocalDateTime now);
}
