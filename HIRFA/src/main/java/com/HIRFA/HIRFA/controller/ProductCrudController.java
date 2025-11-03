package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.service.ProductCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

// ===== CONTRÔLEUR CRUD POUR LES PRODUITS CÔTÉ COOPÉRATIVE =====
// API REST pour que les coopératives gèrent leurs produits (Create, Read, Update, Delete)

@RestController
@RequestMapping("/api/cooperative/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductCrudController {

    private final ProductCrudService productCrudService;

    /**
     * Créer un nouveau produit
     * POST /api/cooperative/products
     */
    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestBody ProductCreateRequest request,
            @RequestParam UUID cooperativeId) {
        try {
            ProductResponse product = productCrudService.createProduct(request, cooperativeId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Produit créé avec succès",
                "product", product
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Mettre à jour un produit existant
     * PUT /api/cooperative/products/{productId}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductUpdateRequest request,
            @RequestParam UUID cooperativeId) {
        try {
            ProductResponse product = productCrudService.updateProduct(productId, request, cooperativeId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Produit mis à jour avec succès",
                "product", product
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir un produit par son ID
     * GET /api/cooperative/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(
            @PathVariable UUID productId,
            @RequestParam UUID cooperativeId) {
        try {
            ProductResponse product = productCrudService.getProduct(productId, cooperativeId);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir tous les produits d'une coopérative avec pagination
     * GET /api/cooperative/products
     */
    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam UUID cooperativeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<ProductResponse> products = productCrudService.getProductsByCooperative(cooperativeId, pageable);
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "products", products.getContent(),
                "currentPage", products.getNumber(),
                "totalPages", products.getTotalPages(),
                "totalElements", products.getTotalElements(),
                "hasNext", products.hasNext(),
                "hasPrevious", products.hasPrevious()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Rechercher des produits
     * GET /api/cooperative/products/search
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam UUID cooperativeId,
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<ProductResponse> products = productCrudService.searchProducts(cooperativeId, searchTerm, pageable);
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "products", products.getContent(),
                "searchTerm", searchTerm,
                "currentPage", products.getNumber(),
                "totalPages", products.getTotalPages(),
                "totalElements", products.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Supprimer un produit (soft delete)
     * DELETE /api/cooperative/products/{productId}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable UUID productId,
            @RequestParam UUID cooperativeId) {
        try {
            productCrudService.deleteProduct(productId, cooperativeId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Produit supprimé avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les statistiques des produits d'une coopérative
     * GET /api/cooperative/products/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getProductStats(@RequestParam UUID cooperativeId) {
        try {
            // Cette méthode peut être ajoutée au service si nécessaire
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Statistiques des produits (à implémenter)"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}