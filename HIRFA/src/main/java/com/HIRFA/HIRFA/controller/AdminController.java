package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.entity.*;

import com.HIRFA.HIRFA.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DesignerService designerService;

    @Autowired
    private CooperativeService cooperativeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DesignService designService;

    // 🔹 Get all users with pagination and filtering
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreation,desc") String[] sort,
            @RequestParam(required = false) UserType userType,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) String search) {

        try {
            Pageable pageable = PageRequest.of(page, size, parseSort(sort));
            Page<User> usersPage = userService.findAllUsers(userType, enabled, search, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("users", usersPage.getContent());
            response.put("currentPage", usersPage.getNumber());
            response.put("totalItems", usersPage.getTotalElements());
            response.put("totalPages", usersPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch users: " + e.getMessage(), "FETCH_USERS_FAILED"));
        }
    }

    // 🔹 Get user by ID
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        try {
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "USER_NOT_FOUND"));
        }
    }

    // 🔹 Update user status (enable/disable)
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable UUID userId,
            @RequestParam boolean enabled) {
        try {
            User user = userService.updateUserStatus(userId, enabled);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to update user status: " + e.getMessage(),
                            "UPDATE_STATUS_FAILED"));
        }
    }

    // 🔹 Delete user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(createSuccessResponse("User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to delete user: " + e.getMessage(), "DELETE_USER_FAILED"));
        }
    }

    // 🔹 Get system statistics
    @GetMapping("/statistics")
    public ResponseEntity<?> getSystemStatistics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            Map<String, Object> stats = new HashMap<>();

            // User statistics
            stats.put("totalUsers", userService.countAllUsers());
            stats.put("totalClients", userService.countUsersByType(UserType.CLIENT));
            stats.put("totalDesigners", userService.countUsersByType(UserType.DESIGNER));
            stats.put("totalCooperatives", userService.countUsersByType(UserType.COOPERATIVE));

            // Active users
            stats.put("activeUsersToday", userService.countActiveUsersToday());
            stats.put("newUsersThisWeek", userService.countNewUsersThisWeek());

            // Verification stats
            stats.put("verifiedCooperatives", cooperativeService.countByStatutVerification("VERIFIED"));
            stats.put("pendingCooperatives", cooperativeService.countByStatutVerification("PENDING"));

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch statistics: " + e.getMessage(), "STATS_FETCH_FAILED"));
        }
    }

    // 🔹 Get recent activities
    @GetMapping("/activities")
    public ResponseEntity<?> getRecentActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
            return ResponseEntity.ok(userService.getRecentActivities(pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch activities: " + e.getMessage(),
                            "ACTIVITIES_FETCH_FAILED"));
        }
    }

    // 🔹 Search users
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(userService.searchUsers(query, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Search failed: " + e.getMessage(), "SEARCH_FAILED"));
        }
    }

    // 🔹 Verify/Unverify Cooperative
    @PutMapping("/cooperatives/{cooperativeId}/verify")
    public ResponseEntity<?> verifyCooperative(
            @PathVariable UUID cooperativeId,
            @RequestParam boolean verified) {
        try {
            Cooperative cooperative = cooperativeService.verifyCooperative(cooperativeId, verified);
            return ResponseEntity.ok(cooperative);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to update verification status: " + e.getMessage(),
                            "VERIFICATION_UPDATE_FAILED"));
        }
    }

    // 🔹 Get all cooperatives with optional filtering
    @GetMapping("/cooperatives")
    public ResponseEntity<?> getAllCooperatives(
            @RequestParam(required = false) Boolean verified,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
            return ResponseEntity.ok(cooperativeService.findAllCooperatives(verified, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch cooperatives: " + e.getMessage(),
                            "FETCH_COOPERATIVES_FAILED"));
        }
    }

    // 🔹 Get all designers with optional filtering
    @GetMapping("/designers")
    public ResponseEntity<?> getAllDesigners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String specialite) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
            return ResponseEntity.ok(designerService.findAllDesigners(specialite, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch designers: " + e.getMessage(),
                            "FETCH_DESIGNERS_FAILED"));
        }
    }

    // 🔹 Get all clients with pagination
    @GetMapping("/clients")
    public ResponseEntity<?> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
            return ResponseEntity.ok(clientService.findAllClients(pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch clients: " + e.getMessage(), "FETCH_CLIENTS_FAILED"));
        }
    }

    // 🔹 Get reported products
    @GetMapping("/products/reported")
    public ResponseEntity<?> getReportedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportedAt"));
            Page<Product> productsPage = productService.getReportedProducts(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("products", productsPage.getContent());
            response.put("currentPage", productsPage.getNumber());
            response.put("totalItems", productsPage.getTotalElements());
            response.put("totalPages", productsPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch reported products: " + e.getMessage(),
                            "FETCH_REPORTED_PRODUCTS_FAILED"));
        }
    }

    // 🔹 Approve reported product
    @PutMapping("/products/{productId}/approve")
    public ResponseEntity<?> approveProduct(@PathVariable UUID productId) {
        try {
            Product product = productService.approveProduct(productId);
            if (product != null) {
                return ResponseEntity.ok(createSuccessResponse("Product approved successfully"));
            } else {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Product not found", "PRODUCT_NOT_FOUND"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to approve product: " + e.getMessage(),
                            "APPROVE_PRODUCT_FAILED"));
        }
    }

    // 🔹 Reject reported product
    @PutMapping("/products/{productId}/reject")
    public ResponseEntity<?> rejectProduct(@PathVariable UUID productId) {
        try {
            Product product = productService.rejectProduct(productId);
            if (product != null) {
                return ResponseEntity.ok(createSuccessResponse("Product rejected successfully"));
            } else {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Product not found", "PRODUCT_NOT_FOUND"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to reject product: " + e.getMessage(), "REJECT_PRODUCT_FAILED"));
        }
    }

    // 🔹 Get reported designs
    @GetMapping("/designs/reported")
    public ResponseEntity<?> getReportedDesigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportedAt"));
            Page<Design> designsPage = designService.getReportedDesigns(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("designs", designsPage.getContent());
            response.put("currentPage", designsPage.getNumber());
            response.put("totalItems", designsPage.getTotalElements());
            response.put("totalPages", designsPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch reported designs: " + e.getMessage(),
                            "FETCH_REPORTED_DESIGNS_FAILED"));
        }
    }

    // 🔹 Approve reported design
    @PutMapping("/designs/{designId}/approve")
    public ResponseEntity<?> approveDesign(@PathVariable UUID designId) {
        try {
            Design design = designService.approveDesign(designId);
            if (design != null) {
                return ResponseEntity.ok(createSuccessResponse("Design approved successfully"));
            } else {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Design not found", "DESIGN_NOT_FOUND"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to approve design: " + e.getMessage(),
                            "APPROVE_DESIGN_FAILED"));
        }
    }

    // 🔹 Reject reported design
    @PutMapping("/designs/{designId}/reject")
    public ResponseEntity<?> rejectDesign(@PathVariable UUID designId) {
        try {
            Design design = designService.rejectDesign(designId);
            if (design != null) {
                return ResponseEntity.ok(createSuccessResponse("Design rejected successfully"));
            } else {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Design not found", "DESIGN_NOT_FOUND"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to reject design: " + e.getMessage(), "REJECT_DESIGN_FAILED"));
        }
    }

    // Helper method to parse sort parameters
    private Sort parseSort(String[] sort) {
        if (sort.length >= 2) {
            return Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        } else if (sort.length == 1) {
            return Sort.by(sort[0]).descending();
        }
        return Sort.by("dateCreation").descending();
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
