package com.HIRFA.HIRFA.forgetpassword;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PasswordResetService passwordResetService;

    public AuthController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        passwordResetService.createPasswordResetTokenAndSendEmail(email);
        return ResponseEntity.ok(Map.of("message", "Si cet email existe, un lien a été envoyé."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        boolean ok = passwordResetService.resetPassword(token, newPassword);
        if (ok)
            return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès."));
        return ResponseEntity.badRequest().body(Map.of("message", "Token invalide ou expiré."));
    }
}
