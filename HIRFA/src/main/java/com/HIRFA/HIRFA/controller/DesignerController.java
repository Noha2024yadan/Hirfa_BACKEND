package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.entity.RefreshToken;
import com.HIRFA.HIRFA.entity.User.UserType;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import com.HIRFA.HIRFA.exception.EmailAlreadyExistsException;
import com.HIRFA.HIRFA.exception.UsernameAlreadyExistsException;
import com.HIRFA.HIRFA.security.JwtUtils;
import com.HIRFA.HIRFA.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/designers")
@CrossOrigin(origins = "*")
public class DesignerController {

    @Autowired
    private DesignerService designerService;

    @Autowired
    private DesignerRepository designerRepository;

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

    // 🔹 Register a new designer
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody DesignerRegistrationDto registrationDto) {
        try {
            if (!registrationDto.getMotDePasse().equals(registrationDto.getConfirmMotDePasse())) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Les mots de passe ne correspondent pas", "PASSWORDS_DO_NOT_MATCH"));
            }

            Designer designer = designerService.registerDesigner(registrationDto);

            // Generate JWT token
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registrationDto.getUsername(),
                            registrationDto.getMotDePasse()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("userType", UserType.DESIGNER.name());
            response.put("userId", designer.getUserId());
            response.put("username", designer.getUsername());

            return ResponseEntity.ok(response);

        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(createErrorResponse(e.getMessage(), "EMAIL_ALREADY_EXISTS"));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(createErrorResponse(e.getMessage(), "USERNAME_ALREADY_EXISTS"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Une erreur est survenue lors de l'inscription", "REGISTRATION_ERROR"));
        }
    }

    // 🔹 Login with account lock protection
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Check login attempts
            if (loginSecurityService.isAccountLocked(loginRequest.getUsernameOrEmail())) {
                return ResponseEntity.status(429)
                        .body(createErrorResponse("Trop de tentatives de connexion. Réessayez plus tard.",
                                "TOO_MANY_ATTEMPTS"));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Reset failed login attempts on success
            loginSecurityService.recordLoginAttempt(loginRequest.getUsernameOrEmail(), true, null);

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("userType", UserType.DESIGNER.name());
            response.put("username", authentication.getName());

            // Add refresh token
            // Fix: get User entity from username before creating refresh token
            Designer designer = designerRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Designer not found"));
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(designer);
            response.put("refreshToken", refreshToken.getToken());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Record failed login attempt
            loginSecurityService.recordLoginAttempt(loginRequest.getUsernameOrEmail(), false, null);
            return ResponseEntity.status(401)
                    .body(createErrorResponse("Nom d'utilisateur ou mot de passe incorrect", "BAD_CREDENTIALS"));
        }
    }

    // 🔹 Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                // Remove "Bearer " prefix
                token = token.substring(7);

            }
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(createSuccessResponse("Déconnexion réussie"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Erreur lors de la déconnexion", "LOGOUT_FAILED"));
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
                    .map(user -> {
                        String token = jwtUtils.generateJwtToken(user.getUsername());
                        Map<String, String> response = new HashMap<>();
                        response.put("token", token);
                        response.put("refreshToken", requestRefreshToken);
                        return ResponseEntity.ok(response);
                    })
                    .orElseThrow(() -> new RuntimeException("Refresh token not found!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error refreshing token: " + e.getMessage(), "TOKEN_REFRESH_FAILED"));
        }
    }

    // 🔹 Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordResetRequestDto request) {
        try {
            passwordResetService.initiatePasswordReset(request.getEmail());
            return ResponseEntity.ok(createSuccessResponse("Email de réinitialisation envoyé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "PASSWORD_RESET_REQUEST_FAILED"));
        }
    }

    // 🔹 Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetConfirmDto request) {
        try {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Les mots de passe ne correspondent pas", "PASSWORDS_DO_NOT_MATCH"));
            }
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(createSuccessResponse("Mot de passe réinitialisé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "PASSWORD_RESET_FAILED"));
        }
    }

    // 🔹 Update designer profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody DesignerUpdateDto updateDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Designer updatedDesigner = designerService.updateDesignerProfile(currentUsername, updateDto);
            return ResponseEntity.ok(updatedDesigner);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "PROFILE_UPDATE_FAILED"));
        }
    }

    // 🔹 Get designer profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            final Designer designer = designerService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Designer non trouvé"));

            // Create a safe response without sensitive data
            Map<String, Object> response = new HashMap<>();
            response.put("userId", designer.getUserId());
            response.put("username", designer.getUsername());
            response.put("email", designer.getEmail());
            response.put("nom", designer.getNom());
            response.put("prenom", designer.getPrenom());
            response.put("telephone", designer.getTelephone());
            response.put("portfolio", designer.getPortfolio());
            response.put("specialites", designer.getSpecialites());
            response.put("tarifs", designer.getTarifs());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "PROFILE_FETCH_FAILED"));
        }
    }

    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createErrorResponse(String message, String errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("errorCode", errorCode);
        return response;
    }
}
