package com.HIRFA.HIRFA.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.HIRFA.HIRFA.service.PasswordResetService;
import com.HIRFA.HIRFA.service.UserType;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> body) {
        passwordResetService.createPasswordResetTokenAndSendEmail(body.get("email"));
        return ResponseEntity.ok("Email envoyé si le compte existe.");
    }

    @PostMapping("/client/reset-password")
    public ResponseEntity<String> resetClient(@RequestBody Map<String, String> body) {
        boolean ok = passwordResetService.resetPasswordByRole(body.get("token"), body.get("newPassword"),
                UserType.CLIENT);
        return ok ? ResponseEntity.ok("Mot de passe client changé")
                : ResponseEntity.badRequest().body("Token invalide ou expiré");
    }

    @PostMapping("/designer/reset-password")
    public ResponseEntity<String> resetDesigner(@RequestBody Map<String, String> body) {
        boolean ok = passwordResetService.resetPasswordByRole(body.get("token"), body.get("newPassword"),
                UserType.DESIGNER);
        return ok ? ResponseEntity.ok("Mot de passe designer changé")
                : ResponseEntity.badRequest().body("Token invalide ou expiré");
    }

    @PostMapping("/cooperative/reset-password")
    public ResponseEntity<String> resetCoop(@RequestBody Map<String, String> body) {
        boolean ok = passwordResetService.resetPasswordByRole(body.get("token"), body.get("newPassword"),
                UserType.COOPERATIVE);
        return ok ? ResponseEntity.ok("Mot de passe cooperative changé")
                : ResponseEntity.badRequest().body("Token invalide ou expiré");
    }
}
