package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.entity.RefreshToken;
import com.HIRFA.HIRFA.security.JwtUtils;
import com.HIRFA.HIRFA.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private LoginSecurityService loginSecurityService;

    @Autowired
    private PasswordResetService passwordResetService;

    // 🔹 Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody ClientRegistrationDto registrationDto) {
        try {
            if (!registrationDto.getMotDePasse().equals(registrationDto.getConfirmMotDePasse())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(null, "Les mots de passe ne correspondent pas",
                                "PASSWORDS_DO_NOT_MATCH"));
            }
            Client client = clientService.registerClient(registrationDto);
            return ResponseEntity.ok(new ApiResponse<>(client));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, e.getMessage(), "REGISTRATION_FAILED"));
        }
    }

    // 🔹 Login with account lock protection
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody ClientLoginDto loginDto, HttpServletRequest request) {
        try {
            // Check if account is locked
            if (loginSecurityService.isAccountLocked(loginDto.getUsernameOrEmail())) {
                loginSecurityService.recordLoginAttempt(loginDto.getUsernameOrEmail(), false, request);
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(null, "Compte bloqué. Réessayez dans 15 minutes.", "ACCOUNT_LOCKED"));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getMotDePasse()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication.getName());

            // Record successful login attempt
            loginSecurityService.recordLoginAttempt(loginDto.getUsernameOrEmail(), true, request);

            return ResponseEntity.ok(new ApiResponse<>(new JwtResponseDto(jwt)));
        } catch (Exception e) {
            // Record failed login attempt
            loginSecurityService.recordLoginAttempt(loginDto.getUsernameOrEmail(), false, request);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Identifiants invalides", "INVALID_CREDENTIALS"));
        }
    }

    // 🔹 Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix
            token = token.substring(7);

            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(new ApiResponse<>(null, "Déconnexion réussie", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Erreur lors de la déconnexion", "LOGOUT_FAILED"));
        }
    }

    // 🔹 Refresh Token
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
        String requestRefreshToken = request.getRefreshToken();

        try {
            return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(client -> {
                        String token = jwtUtils.generateJwtToken(client.getUsername());
                        return ResponseEntity.ok(new TokenRefreshResponseDto(token, requestRefreshToken));
                    })
                    .orElseThrow(() -> new RuntimeException("Refresh token not found!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, "Error refreshing token: " + e.getMessage(), "TOKEN_REFRESH_FAILED"));
        }
    }

    // 🔹 Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordResetRequestDto request) {
        try {
            passwordResetService.initiatePasswordReset(request.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(null, "Email de réinitialisation envoyé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, e.getMessage(), "PASSWORD_RESET_REQUEST_FAILED"));
        }
    }

    // 🔹 Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetConfirmDto request) {
        try {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(null, "Les mots de passe ne correspondent pas",
                                "PASSWORDS_DO_NOT_MATCH"));
            }
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse<>(null, "Mot de passe réinitialisé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, e.getMessage(), "PASSWORD_RESET_FAILED"));
        }
    }
}
