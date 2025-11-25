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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

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

    // 🔹 Get all cooperatives with optional filtering
    @GetMapping("/cooperatives")
    public ResponseEntity<?> getAllCooperatives(
            @RequestParam(required = false) Boolean verified,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
            Page<Cooperative> cooperativesPage = cooperativeService.findAllCooperatives(verified, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("cooperatives", cooperativesPage.getContent());
            response.put("currentPage", cooperativesPage.getNumber());
            response.put("totalItems", cooperativesPage.getTotalElements());
            response.put("totalPages", cooperativesPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch cooperatives: " + e.getMessage(),
                            "FETCH_COOPERATIVES_FAILED"));
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

    // 🔹 Get all designers with optional filtering
    @GetMapping("/designers")
    public ResponseEntity<?> getAllDesigners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String specialite) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreation"));
            Page<Designer> designersPage = designerService.findAllDesigners(specialite, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("designers", designersPage.getContent());
            response.put("currentPage", designersPage.getNumber());
            response.put("totalItems", designersPage.getTotalElements());
            response.put("totalPages", designersPage.getTotalPages());

            return ResponseEntity.ok(response);
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
            Page<Client> clientsPage = clientService.findAllClients(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("clients", clientsPage.getContent());
            response.put("currentPage", clientsPage.getNumber());
            response.put("totalItems", clientsPage.getTotalElements());
            response.put("totalPages", clientsPage.getTotalPages());

            return ResponseEntity.ok(response);
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

    // 🔹 Get system statistics (based on other entities)
    @GetMapping("/statistics")
    public ResponseEntity<?> getSystemStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            stats.put("totalClients", clientService.countAllClients());
            stats.put("totalDesigners", designerService.countAllDesigners());
            stats.put("totalCooperatives", cooperativeService.countAllCooperatives());
            stats.put("totalProducts", productService.countAllProducts());
            stats.put("totalDesigns", designService.countAllDesigns());

            stats.put("verifiedCooperatives", cooperativeService.countByStatutVerification("VERIFIED"));
            stats.put("pendingCooperatives", cooperativeService.countByStatutVerification("PENDING"));

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Failed to fetch statistics: " + e.getMessage(), "STATS_FETCH_FAILED"));
        }
    }

    // Helper methods
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
