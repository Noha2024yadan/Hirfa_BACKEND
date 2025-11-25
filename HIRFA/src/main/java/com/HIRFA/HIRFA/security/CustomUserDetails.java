package com.HIRFA.HIRFA.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean enabled;

    // ✅ Constructor matching your service
    public CustomUserDetails(UUID id, String username, String email, String password, String role, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can change logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can change logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can change logic if needed
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Object getUserType() {
        throw new UnsupportedOperationException("Unimplemented method 'getUserType'");
    }
}
