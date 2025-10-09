package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.entity.RefreshToken;
import com.HIRFA.HIRFA.entity.User.UserType;
import com.HIRFA.HIRFA.exception.EmailAlreadyExistsException;
import com.HIRFA.HIRFA.exception.UsernameAlreadyExistsException;
import com.HIRFA.HIRFA.security.JwtUtils;
import com.HIRFA.HIRFA.service.*;
import org.springframework.data.domain.Page;
import com.HIRFA.HIRFA.entity.Order;
import com.HIRFA.HIRFA.entity.ClientRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cooperatives")
@CrossOrigin(origins = "*")
public class CooperativeController {

    @Autowired
    private CooperativeService cooperativeService;

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

    @Autowired
    private DesignService designService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientRequestService clientRequestService;

    // 🔹 Register a new cooperative
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CooperativeRegistrationDto registrationDto) {
        try {
            if (!registrationDto.getMotDePasse().equals(registrationDto.getConfirmMotDePasse())) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Les mots de passe ne correspondent pas", "PASSWORDS_DO_NOT_MATCH"));
            }

            Cooperative cooperative = cooperativeService.registerCooperative(registrationDto);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registrationDto.getUsername(),
                            registrationDto.getMotDePasse()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("userType", UserType.COOPERATIVE.name());
            response.put("userId", cooperative.getUserId());
            response.put("username", cooperative.getUsername());

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
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

            // ✅ Fetch Cooperative to use UUID for refresh token
            Cooperative cooperative = cooperativeService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            // Record login attempt as success
            loginSecurityService.recordLoginAttempt(loginRequest.getUsernameOrEmail(), true, request);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("userType", UserType.COOPERATIVE.name());
            response.put("username", authentication.getName());

            // ✅ Use User for refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(cooperative);
            response.put("refreshToken", refreshToken.getToken());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            loginSecurityService.recordLoginAttempt(loginRequest.getUsernameOrEmail(), false, request);
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
            Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByToken(requestRefreshToken);
            if (refreshTokenOpt.isEmpty()) {
                throw new RuntimeException("Refresh token not found!");
            }

            RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenOpt.get());

            // ✅ Assuming RefreshToken is linked to Cooperative
            String username = refreshToken.getUser().getUsername(); // make sure RefreshToken has `user` relation

            String newJwt = jwtUtils.generateJwtToken(username);

            Map<String, String> response = new HashMap<>();
            response.put("token", newJwt);
            response.put("refreshToken", requestRefreshToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Erreur lors du rafraîchissement du token: " + e.getMessage(),
                            "TOKEN_REFRESH_FAILED"));
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

    // 🔹 Get cooperative profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Coopérative non trouvée"));

            Map<String, Object> response = new HashMap<>();
            response.put("userId", cooperative.getUserId());
            response.put("username", cooperative.getUsername());
            response.put("email", cooperative.getEmail());
            response.put("nom", cooperative.getNom());
            response.put("prenom", cooperative.getPrenom());
            response.put("telephone", cooperative.getTelephone());
            response.put("description", cooperative.getDescription());
            response.put("adresse", cooperative.getAdresse());
            response.put("licence", cooperative.getLicence());
            response.put("statutVerification", cooperative.getStatutVerification());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "PROFILE_FETCH_FAILED"));
        }
    }

    // 🔹 Admin: Verify cooperative
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{cooperativeId}/verify")
    public ResponseEntity<?> verifyCooperative(@PathVariable UUID cooperativeId, @RequestParam boolean verified) {
        try {
            Cooperative cooperative = cooperativeService.verifyCooperative(cooperativeId, verified);
            return ResponseEntity.ok(cooperative);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "VERIFICATION_FAILED"));
        }
    }

    // 🔹 Admin: List cooperatives
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllCooperatives(@RequestParam(required = false) Boolean verified) {
        try {
            List<Cooperative> cooperatives = cooperativeService.findAllCooperatives(verified);
            return ResponseEntity.ok(cooperatives);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "FETCH_COOPERATIVES_FAILED"));
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

    // ==================== DESIGN VIEWING ENDPOINTS ====================

    // 🔹 Get all available designs with pagination
    @GetMapping("/designs")
    public ResponseEntity<?> getAllDesigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.getAllAvailableDesigns(page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Designs retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching designs: " + e.getMessage(), "DESIGNS_FETCH_FAILED"));
        }
    }

    // 🔹 Get design by ID
    @GetMapping("/designs/{id}")
    public ResponseEntity<?> getDesignById(@PathVariable Long id) {
        try {
            DesignResponseDto design = designService.getDesignById(id);
            return ResponseEntity.ok(createSuccessResponse("Design retrieved successfully", design));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching design: " + e.getMessage(), "DESIGN_FETCH_FAILED"));
        }
    }

    // 🔹 Get designs by category
    @GetMapping("/designs/category/{category}")
    public ResponseEntity<?> getDesignsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.getDesignsByCategory(category, page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Designs by category retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching designs by category: " + e.getMessage(),
                            "DESIGNS_BY_CATEGORY_FAILED"));
        }
    }

    // 🔹 Search designs
    @GetMapping("/designs/search")
    public ResponseEntity<?> searchDesigns(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.searchDesigns(q, page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Design search completed successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error searching designs: " + e.getMessage(), "DESIGN_SEARCH_FAILED"));
        }
    }

    // 🔹 Get designs by price range
    @GetMapping("/designs/price-range")
    public ResponseEntity<?> getDesignsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.getDesignsByPriceRange(minPrice, maxPrice, page, size,
                    sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Designs by price range retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching designs by price range: " + e.getMessage(),
                            "DESIGNS_BY_PRICE_FAILED"));
        }
    }

    // 🔹 Get designs by category and price range
    @GetMapping("/designs/category/{category}/price-range")
    public ResponseEntity<?> getDesignsByCategoryAndPriceRange(
            @PathVariable String category,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.getDesignsByCategoryAndPriceRange(category, minPrice,
                    maxPrice, page, size, sortBy, sortDir);
            return ResponseEntity
                    .ok(createSuccessResponse("Designs by category and price range retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching designs by category and price range: " + e.getMessage(),
                            "DESIGNS_BY_CATEGORY_PRICE_FAILED"));
        }
    }

    // 🔹 Get designs by file format
    @GetMapping("/designs/file-format/{fileFormat}")
    public ResponseEntity<?> getDesignsByFileFormat(
            @PathVariable String fileFormat,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.getDesignsByFileFormat(fileFormat, page, size, sortBy,
                    sortDir);
            return ResponseEntity.ok(createSuccessResponse("Designs by file format retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching designs by file format: " + e.getMessage(),
                            "DESIGNS_BY_FILE_FORMAT_FAILED"));
        }
    }

    // 🔹 Get designs by rating range
    @GetMapping("/designs/rating-range")
    public ResponseEntity<?> getDesignsByRatingRange(
            @RequestParam Double minRating,
            @RequestParam Double maxRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.getDesignsByRatingRange(minRating, maxRating, page, size,
                    sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Designs by rating range retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching designs by rating range: " + e.getMessage(),
                            "DESIGNS_BY_RATING_FAILED"));
        }
    }

    // 🔹 Get designs by multiple criteria
    @GetMapping("/designs/filter")
    public ResponseEntity<?> getDesignsByMultipleCriteria(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String fileFormat,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.getDesignsByMultipleCriteria(category, fileFormat, minPrice,
                    maxPrice, minRating, maxRating, page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Designs filtered successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error filtering designs: " + e.getMessage(), "DESIGNS_FILTER_FAILED"));
        }
    }

    // 🔹 Get designs by designer
    @GetMapping("/designs/designer/{designerId}")
    public ResponseEntity<?> getDesignsByDesigner(
            @PathVariable UUID designerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<DesignResponseDto> designs = designService.getDesignsByDesigner(designerId, page, size, sortBy,
                    sortDir);
            return ResponseEntity.ok(createSuccessResponse("Designs by designer retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching designs by designer: " + e.getMessage(),
                            "DESIGNS_BY_DESIGNER_FAILED"));
        }
    }

    // 🔹 Get all categories
    @GetMapping("/designs/categories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<String> categories = designService.getAllCategories();
            return ResponseEntity.ok(createSuccessResponse("Categories retrieved successfully", categories));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching categories: " + e.getMessage(),
                            "CATEGORIES_FETCH_FAILED"));
        }
    }

    // 🔹 Get all file formats
    @GetMapping("/designs/file-formats")
    public ResponseEntity<?> getAllFileFormats() {
        try {
            List<String> fileFormats = designService.getAllFileFormats();
            return ResponseEntity.ok(createSuccessResponse("File formats retrieved successfully", fileFormats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching file formats: " + e.getMessage(),
                            "FILE_FORMATS_FETCH_FAILED"));
        }
    }

    // 🔹 Get featured designs
    @GetMapping("/designs/featured")
    public ResponseEntity<?> getFeaturedDesigns(@RequestParam(defaultValue = "5") int limit) {
        try {
            List<DesignResponseDto> designs = designService.getFeaturedDesigns(limit);
            return ResponseEntity.ok(createSuccessResponse("Featured designs retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching featured designs: " + e.getMessage(),
                            "FEATURED_DESIGNS_FAILED"));
        }
    }

    // 🔹 Get top-rated designs
    @GetMapping("/designs/top-rated")
    public ResponseEntity<?> getTopRatedDesigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<DesignResponseDto> designs = designService.getTopRatedDesigns(page, size);
            return ResponseEntity.ok(createSuccessResponse("Top-rated designs retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching top-rated designs: " + e.getMessage(),
                            "TOP_RATED_DESIGNS_FAILED"));
        }
    }

    // 🔹 Get most downloaded designs
    @GetMapping("/designs/most-downloaded")
    public ResponseEntity<?> getMostDownloadedDesigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<DesignResponseDto> designs = designService.getMostDownloadedDesigns(page, size);
            return ResponseEntity.ok(createSuccessResponse("Most downloaded designs retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching most downloaded designs: " + e.getMessage(),
                            "MOST_DOWNLOADED_DESIGNS_FAILED"));
        }
    }

    // 🔹 Get recent designs
    @GetMapping("/designs/recent")
    public ResponseEntity<?> getRecentDesigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<DesignResponseDto> designs = designService.getRecentDesigns(page, size);
            return ResponseEntity.ok(createSuccessResponse("Recent designs retrieved successfully", designs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching recent designs: " + e.getMessage(),
                            "RECENT_DESIGNS_FAILED"));
        }
    }

    // 🔹 Get design statistics
    @GetMapping("/designs/statistics")
    public ResponseEntity<?> getDesignStatistics() {
        try {
            DesignService.DesignStatsDto stats = designService.getDesignStatistics();
            return ResponseEntity.ok(createSuccessResponse("Design statistics retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching design statistics: " + e.getMessage(),
                            "DESIGN_STATISTICS_FAILED"));
        }
    }

    // Helper method for success response with data
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    // ==================== ORDER VIEWING ENDPOINTS ====================

    // 🔹 Get all orders for the current cooperative
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<OrderResponseDto> orders = orderService.getAllOrdersByUser(cooperative.getUserId(), page, size, sortBy,
                    sortDir);
            return ResponseEntity.ok(createSuccessResponse("Orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching orders: " + e.getMessage(), "ORDERS_FETCH_FAILED"));
        }
    }

    // 🔹 Get order by ID
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            OrderResponseDto order = orderService.getOrderByIdAndUser(id, cooperative.getUserId());
            return ResponseEntity.ok(createSuccessResponse("Order retrieved successfully", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching order: " + e.getMessage(), "ORDER_FETCH_FAILED"));
        }
    }

    // 🔹 Get order by order number
    @GetMapping("/orders/order-number/{orderNumber}")
    public ResponseEntity<?> getOrderByOrderNumber(@PathVariable String orderNumber) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            OrderResponseDto order = orderService.getOrderByOrderNumberAndUser(orderNumber, cooperative.getUserId());
            return ResponseEntity.ok(createSuccessResponse("Order retrieved successfully", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching order: " + e.getMessage(), "ORDER_FETCH_FAILED"));
        }
    }

    // 🔹 Get orders by status
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            Page<OrderResponseDto> orders = orderService.getOrdersByUserAndStatus(cooperative.getUserId(), orderStatus,
                    page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Orders by status retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching orders by status: " + e.getMessage(),
                            "ORDERS_BY_STATUS_FAILED"));
        }
    }

    // 🔹 Get orders by payment status
    @GetMapping("/orders/payment-status/{paymentStatus}")
    public ResponseEntity<?> getOrdersByPaymentStatus(
            @PathVariable String paymentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Order.PaymentStatus orderPaymentStatus = Order.PaymentStatus.valueOf(paymentStatus.toUpperCase());
            Page<OrderResponseDto> orders = orderService.getOrdersByUserAndPaymentStatus(cooperative.getUserId(),
                    orderPaymentStatus, page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Orders by payment status retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching orders by payment status: " + e.getMessage(),
                            "ORDERS_BY_PAYMENT_STATUS_FAILED"));
        }
    }

    // 🔹 Search orders by order number
    @GetMapping("/orders/search")
    public ResponseEntity<?> searchOrders(
            @RequestParam String orderNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<OrderResponseDto> orders = orderService.searchOrdersByUserAndOrderNumber(cooperative.getUserId(),
                    orderNumber, page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Order search completed successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error searching orders: " + e.getMessage(), "ORDER_SEARCH_FAILED"));
        }
    }

    // 🔹 Get recent orders (last 30 days)
    @GetMapping("/orders/recent")
    public ResponseEntity<?> getRecentOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<OrderResponseDto> orders = orderService.getRecentOrdersByUser(cooperative.getUserId(), page, size);
            return ResponseEntity.ok(createSuccessResponse("Recent orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching recent orders: " + e.getMessage(),
                            "RECENT_ORDERS_FAILED"));
        }
    }

    // 🔹 Get orders that need attention
    @GetMapping("/orders/needing-attention")
    public ResponseEntity<?> getOrdersNeedingAttention(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<OrderResponseDto> orders = orderService.getOrdersNeedingAttentionByUser(cooperative.getUserId(), page,
                    size);
            return ResponseEntity.ok(createSuccessResponse("Orders needing attention retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching orders needing attention: " + e.getMessage(),
                            "ORDERS_NEEDING_ATTENTION_FAILED"));
        }
    }

    // 🔹 Get order statistics
    @GetMapping("/orders/statistics")
    public ResponseEntity<?> getOrderStatistics() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            OrderStatisticsDto statistics = orderService.getOrderStatisticsByUser(cooperative.getUserId());
            return ResponseEntity.ok(createSuccessResponse("Order statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching order statistics: " + e.getMessage(),
                            "ORDER_STATISTICS_FAILED"));
        }
    }

    // ==================== CLIENT REQUEST MANAGEMENT ENDPOINTS ====================

    // 🔹 Get all client requests for the current cooperative
    @GetMapping("/requests")
    public ResponseEntity<?> getAllClientRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<ClientRequestResponseDto> requests = clientRequestService
                    .getAllRequestsByCooperative(cooperative.getUserId(), page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Client requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching client requests: " + e.getMessage(),
                            "REQUESTS_FETCH_FAILED"));
        }
    }

    // 🔹 Get client request by ID
    @GetMapping("/requests/{id}")
    public ResponseEntity<?> getClientRequestById(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequestResponseDto request = clientRequestService.getRequestByIdAndCooperative(id,
                    cooperative.getUserId());
            return ResponseEntity.ok(createSuccessResponse("Client request retrieved successfully", request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching client request: " + e.getMessage(),
                            "REQUEST_FETCH_FAILED"));
        }
    }

    // 🔹 Get requests by status
    @GetMapping("/requests/status/{status}")
    public ResponseEntity<?> getRequestsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequest.RequestStatus requestStatus = ClientRequest.RequestStatus.valueOf(status.toUpperCase());
            Page<ClientRequestResponseDto> requests = clientRequestService.getRequestsByCooperativeAndStatus(
                    cooperative.getUserId(), requestStatus, page, size, sortBy, sortDir);
            return ResponseEntity
                    .ok(createSuccessResponse("Client requests by status retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching requests by status: " + e.getMessage(),
                            "REQUESTS_BY_STATUS_FAILED"));
        }
    }

    // 🔹 Get requests by priority
    @GetMapping("/requests/priority/{priority}")
    public ResponseEntity<?> getRequestsByPriority(
            @PathVariable String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequest.RequestPriority requestPriority = ClientRequest.RequestPriority
                    .valueOf(priority.toUpperCase());
            Page<ClientRequestResponseDto> requests = clientRequestService.getRequestsByCooperativeAndPriority(
                    cooperative.getUserId(), requestPriority, page, size, sortBy, sortDir);
            return ResponseEntity
                    .ok(createSuccessResponse("Client requests by priority retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching requests by priority: " + e.getMessage(),
                            "REQUESTS_BY_PRIORITY_FAILED"));
        }
    }

    // 🔹 Get requests by category
    @GetMapping("/requests/category/{category}")
    public ResponseEntity<?> getRequestsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequest.RequestCategory requestCategory = ClientRequest.RequestCategory
                    .valueOf(category.toUpperCase());
            Page<ClientRequestResponseDto> requests = clientRequestService.getRequestsByCooperativeAndCategory(
                    cooperative.getUserId(), requestCategory, page, size, sortBy, sortDir);
            return ResponseEntity
                    .ok(createSuccessResponse("Client requests by category retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching requests by category: " + e.getMessage(),
                            "REQUESTS_BY_CATEGORY_FAILED"));
        }
    }

    // 🔹 Get urgent requests
    @GetMapping("/requests/urgent")
    public ResponseEntity<?> getUrgentRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<ClientRequestResponseDto> requests = clientRequestService
                    .getUrgentRequestsByCooperative(cooperative.getUserId(), page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Urgent client requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching urgent requests: " + e.getMessage(),
                            "URGENT_REQUESTS_FAILED"));
        }
    }

    // 🔹 Search requests
    @GetMapping("/requests/search")
    public ResponseEntity<?> searchRequests(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<ClientRequestResponseDto> requests = clientRequestService
                    .searchRequestsByCooperative(cooperative.getUserId(), q, page, size, sortBy, sortDir);
            return ResponseEntity.ok(createSuccessResponse("Client request search completed successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error searching requests: " + e.getMessage(), "REQUEST_SEARCH_FAILED"));
        }
    }

    // 🔹 Get recent requests (last 30 days)
    @GetMapping("/requests/recent")
    public ResponseEntity<?> getRecentRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<ClientRequestResponseDto> requests = clientRequestService
                    .getRecentRequestsByCooperative(cooperative.getUserId(), page, size);
            return ResponseEntity.ok(createSuccessResponse("Recent client requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching recent requests: " + e.getMessage(),
                            "RECENT_REQUESTS_FAILED"));
        }
    }

    // 🔹 Get requests that need attention
    @GetMapping("/requests/needing-attention")
    public ResponseEntity<?> getRequestsNeedingAttention(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<ClientRequestResponseDto> requests = clientRequestService
                    .getRequestsNeedingAttentionByCooperative(cooperative.getUserId(), page, size);
            return ResponseEntity
                    .ok(createSuccessResponse("Client requests needing attention retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching requests needing attention: " + e.getMessage(),
                            "REQUESTS_NEEDING_ATTENTION_FAILED"));
        }
    }

    // 🔹 Get overdue requests
    @GetMapping("/requests/overdue")
    public ResponseEntity<?> getOverdueRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            Page<ClientRequestResponseDto> requests = clientRequestService
                    .getOverdueRequestsByCooperative(cooperative.getUserId(), page, size);
            return ResponseEntity.ok(createSuccessResponse("Overdue client requests retrieved successfully", requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching overdue requests: " + e.getMessage(),
                            "OVERDUE_REQUESTS_FAILED"));
        }
    }

    // 🔹 Update request status
    @PutMapping("/requests/{id}/status")
    public ResponseEntity<?> updateRequestStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequest.RequestStatus requestStatus = ClientRequest.RequestStatus.valueOf(status.toUpperCase());
            ClientRequestResponseDto request = clientRequestService.updateRequestStatus(id, cooperative.getUserId(),
                    requestStatus);
            return ResponseEntity.ok(createSuccessResponse("Request status updated successfully", request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error updating request status: " + e.getMessage(),
                            "REQUEST_STATUS_UPDATE_FAILED"));
        }
    }

    // 🔹 Add cooperative response to request
    @PostMapping("/requests/{id}/respond")
    public ResponseEntity<?> addCooperativeResponse(
            @PathVariable Long id,
            @RequestParam String response,
            @RequestParam(required = false) String notes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequestResponseDto request = clientRequestService.addCooperativeResponse(id, cooperative.getUserId(),
                    response, notes);
            return ResponseEntity.ok(createSuccessResponse("Response added successfully", request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error adding response: " + e.getMessage(), "RESPONSE_ADD_FAILED"));
        }
    }

    // 🔹 Add quote to request
    @PostMapping("/requests/{id}/quote")
    public ResponseEntity<?> addQuoteToRequest(
            @PathVariable Long id,
            @RequestParam BigDecimal quotedPrice,
            @RequestParam String quotedDuration,
            @RequestParam String response,
            @RequestParam(required = false) String notes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequestResponseDto request = clientRequestService.addQuoteToRequest(id, cooperative.getUserId(),
                    quotedPrice, quotedDuration, response, notes);
            return ResponseEntity.ok(createSuccessResponse("Quote added successfully", request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error adding quote: " + e.getMessage(), "QUOTE_ADD_FAILED"));
        }
    }

    // 🔹 Accept request
    @PostMapping("/requests/{id}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequestResponseDto request = clientRequestService.acceptRequest(id, cooperative.getUserId());
            return ResponseEntity.ok(createSuccessResponse("Request accepted successfully", request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error accepting request: " + e.getMessage(), "REQUEST_ACCEPT_FAILED"));
        }
    }

    // 🔹 Complete request
    @PostMapping("/requests/{id}/complete")
    public ResponseEntity<?> completeRequest(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequestResponseDto request = clientRequestService.completeRequest(id, cooperative.getUserId());
            return ResponseEntity.ok(createSuccessResponse("Request completed successfully", request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error completing request: " + e.getMessage(),
                            "REQUEST_COMPLETE_FAILED"));
        }
    }

    // 🔹 Cancel request
    @PostMapping("/requests/{id}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequestResponseDto request = clientRequestService.cancelRequest(id, cooperative.getUserId());
            return ResponseEntity.ok(createSuccessResponse("Request cancelled successfully", request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error cancelling request: " + e.getMessage(), "REQUEST_CANCEL_FAILED"));
        }
    }

    // 🔹 Reject request
    @PostMapping("/requests/{id}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable Long id,
            @RequestParam String reason) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            ClientRequestResponseDto request = clientRequestService.rejectRequest(id, cooperative.getUserId(), reason);
            return ResponseEntity.ok(createSuccessResponse("Request rejected successfully", request));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error rejecting request: " + e.getMessage(), "REQUEST_REJECT_FAILED"));
        }
    }

    // 🔹 Get request statistics
    @GetMapping("/requests/statistics")
    public ResponseEntity<?> getRequestStatistics() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get cooperative by username to get UUID
            Cooperative cooperative = cooperativeService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cooperative not found"));

            RequestStatisticsDto statistics = clientRequestService
                    .getRequestStatisticsByCooperative(cooperative.getUserId());
            return ResponseEntity.ok(createSuccessResponse("Request statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Error fetching request statistics: " + e.getMessage(),
                            "REQUEST_STATISTICS_FAILED"));
        }
    }
}
