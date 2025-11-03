package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.CooperativeRegistrationDto;
import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.services.CooperativeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cooperative")
public class CooperativeController {

    private final CooperativeService cooperativeService;

    public CooperativeController(CooperativeService cooperativeService) {
        this.cooperativeService = cooperativeService;
    }

    // Register using DTO
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CooperativeRegistrationDto dto) {
        try {
            Cooperative cooperative = convertDtoToEntity(dto);
            cooperativeService.registerCooperative(cooperative);
            return ResponseEntity.ok("Inscription réussie! Vérifiez votre email pour confirmer votre compte.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'inscription: " + e.getMessage());
        }
    }

    // Convert DTO to Entity
    private Cooperative convertDtoToEntity(CooperativeRegistrationDto dto) {
        return Cooperative.builder()
                .email(dto.getEmail())
                .brand(dto.getBrand())
                .telephone(dto.getTelephone())
                .motDePasse(dto.getMotDePasse())
                .description(dto.getDescription())
                .adresse(dto.getAdresse())
                .licence(dto.getLicence())
                .build();
    }

    // Confirm email
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String code) {
        boolean confirmed = cooperativeService.confirmEmail(code);
        if (confirmed) {
            return ResponseEntity.ok("Compte confirmé avec succès !");
        } else {
            return ResponseEntity.badRequest().body("Code invalide ");
        }
    }

    
}
