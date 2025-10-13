package com.HIRFA.HIRFA.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ New style for Spring Security 6.1+
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // ✅ Allow all requests (no authentication)
                );

        return http.build();
    }
}
