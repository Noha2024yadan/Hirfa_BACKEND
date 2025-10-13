package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User {

    @Id
    @GeneratedValue
    private UUID userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String nom;
    private String prenom;
    private String telephone;

    @Column(nullable = false)
    private String motDePasse;

    @CreationTimestamp
    private LocalDateTime dateCreation;

    private LocalDateTime derniereConnexion;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(nullable = false)
    private boolean enabled = true;

    // Constructors
    public User() {
    }

    // Spring Security methods
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userType.name()));
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return enabled;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Custom method
    public boolean isActive() {
        return enabled;
    }
}
