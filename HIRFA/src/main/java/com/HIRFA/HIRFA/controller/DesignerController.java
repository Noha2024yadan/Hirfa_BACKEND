package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.entity.RefreshToken;
import com.HIRFA.HIRFA.entity.UserType;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import com.HIRFA.HIRFA.exception.EmailAlreadyExistsException;
import com.HIRFA.HIRFA.exception.UsernameAlreadyExistsException;
import com.HIRFA.HIRFA.security.JwtUtils;
import com.HIRFA.HIRFA.service.*;
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

    // 🔹 Register a new designer
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody DesignerRegistrationDto registrationDto) {
        try {
            if (!registrationDto.getMotDePasse().equals(registrationDto.getConfirmMotDePasse())) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Les mots de passe ne correspondent pas", "PASSWORDS_DO_NOT_MATCH"));
            }

            Designer designer = designerService.registerDesigner(registrationDto);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registrationDto.getUsername(),
                            registrationDto.getMotDePasse()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("userType", UserType.DESIGNER.name());
            response.put("userId", designer.getId());
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

    // 🔹 Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
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

            loginSecurityService.recordLoginAttempt(loginRequest.getUsernameOrEmail(), true, null);

            Designer designer = designerRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Designer not found"));

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(designer);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("refreshToken", refreshToken.getToken());
            response.put("userType", UserType.DESIGNER.name());
            response.put("username", designer.getUsername());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            loginSecurityService.recordLoginAttempt(loginRequest.getUsernameOrEmail(), false, null);
            return ResponseEntity.status(401)
                    .body(createErrorResponse("Nom d'utilisateur ou mot de passe incorrect", "BAD_CREDENTIALS"));
        }
    }

    // 🔹 Refresh Token
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
        String requestRefreshToken = request.getRefreshToken();

        try {
            return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getDesigner)
                    .map(designer -> {
                        String token = jwtUtils.generateJwtToken(designer.getUsername());
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

    // 🔹 Get designer profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Designer designer = designerService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Designer non trouvé"));

            Map<String, Object> response = new HashMap<>();
            response.put("id", designer.getId());
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

    private Map<String, Object> createErrorResponse(String message, String errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("errorCode", errorCode);
        return response;
    }

}
