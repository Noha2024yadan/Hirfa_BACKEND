package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.entity.*;
import com.HIRFA.HIRFA.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// ===== SERVICE CRUD POUR LES PRODUITS CÔTÉ COOPÉRATIVE =====
// Gère toutes les opérations CRUD des produits pour les coopératives

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCrudService {

    private final ProductRepository productRepository;
    private final CooperativeRepository cooperativeRepository;

    /**
     * Créer un nouveau produit
     * @param request Données du produit à créer
     * @param cooperativeId ID de la coopérative qui crée le produit
     * @return Le produit créé
     */
    public ProductResponse createProduct(ProductCreateRequest request, UUID cooperativeId) {
        Cooperative cooperative = cooperativeRepository.findById(cooperativeId)
            .orElseThrow(() -> new RuntimeException("Coopérative non trouvée"));

        Product product = Product.builder()
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .stockQuantity(request.getStockQuantity())
            .category(request.getCategory())
            .poids(request.getPoids())
            .dimensions(request.getDimensions())
            .cooperative(cooperative)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .statut(true) // Par défaut actif
            .isAvailable(true) // Par défaut disponible
            .isReported(false)
            .signalements(0)
            .build();

        Product savedProduct = productRepository.save(product);

        // Gérer les images si elles sont fournies
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            createProductImages(savedProduct, request.getImageUrls());
        }

        return convertToProductResponse(savedProduct);
    }

    /**
     * Mettre à jour un produit existant
     * @param productId ID du produit à mettre à jour
     * @param request Nouvelles données du produit
     * @param cooperativeId ID de la coopérative (pour vérification des droits)
     * @return Le produit mis à jour
     */
    public ProductResponse updateProduct(UUID productId, ProductUpdateRequest request, UUID cooperativeId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Vérifier que le produit appartient bien à cette coopérative
        if (!product.getCooperative().getUserId().equals(cooperativeId)) {
            throw new RuntimeException("Vous n'avez pas le droit de modifier ce produit");
        }

        // Mettre à jour les champs
        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getStockQuantity() >= 0) product.setStockQuantity(request.getStockQuantity());
        if (request.getCategory() != null) product.setCategory(request.getCategory());
        if (request.getPoids() != null) product.setPoids(request.getPoids());
        if (request.getDimensions() != null) product.setDimensions(request.getDimensions());
        if (request.getStatut() != null) product.setStatut(request.getStatut());
        if (request.getIsAvailable() != null) product.setAvailable(request.getIsAvailable());

        product.setUpdatedAt(LocalDateTime.now());

        // Gérer les images si elles sont fournies
        if (request.getImageUrls() != null) {
            updateProductImages(product, request.getImageUrls());
        }

        Product savedProduct = productRepository.save(product);
        return convertToProductResponse(savedProduct);
    }

    /**
     * Obtenir un produit par son ID
     * @param productId ID du produit
     * @param cooperativeId ID de la coopérative (pour vérification des droits)
     * @return Le produit trouvé
     */
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID productId, UUID cooperativeId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Vérifier que le produit appartient bien à cette coopérative
        if (!product.getCooperative().getUserId().equals(cooperativeId)) {
            throw new RuntimeException("Vous n'avez pas le droit de voir ce produit");
        }

        return convertToProductResponse(product);
    }

    /**
     * Obtenir tous les produits d'une coopérative avec pagination
     * @param cooperativeId ID de la coopérative
     * @param pageable Paramètres de pagination
     * @return Page de produits
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCooperative(UUID cooperativeId, Pageable pageable) {
        Cooperative cooperative = cooperativeRepository.findById(cooperativeId)
            .orElseThrow(() -> new RuntimeException("Coopérative non trouvée"));

        Page<Product> productsPage = productRepository.findByCooperative(cooperative, pageable);
        
        List<ProductResponse> productResponses = productsPage.getContent().stream()
            .map(this::convertToProductResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(productResponses, pageable, productsPage.getTotalElements());
    }

    /**
     * Supprimer un produit (soft delete)
     * @param productId ID du produit à supprimer
     * @param cooperativeId ID de la coopérative (pour vérification des droits)
     */
    public void deleteProduct(UUID productId, UUID cooperativeId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Vérifier que le produit appartient bien à cette coopérative
        if (!product.getCooperative().getUserId().equals(cooperativeId)) {
            throw new RuntimeException("Vous n'avez pas le droit de supprimer ce produit");
        }

        // Soft delete : marquer comme indisponible au lieu de supprimer
        product.setAvailable(false);
        product.setStatut(false);
        product.setUpdatedAt(LocalDateTime.now());
        
        productRepository.save(product);
    }

    /**
     * Rechercher des produits par nom ou catégorie
     * @param cooperativeId ID de la coopérative
     * @param searchTerm Terme de recherche
     * @param pageable Paramètres de pagination
     * @return Page de produits correspondants
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(UUID cooperativeId, String searchTerm, Pageable pageable) {
        Cooperative cooperative = cooperativeRepository.findById(cooperativeId)
            .orElseThrow(() -> new RuntimeException("Coopérative non trouvée"));

        Page<Product> productsPage = productRepository
            .findByCooperativeAndNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                cooperative, searchTerm, searchTerm, pageable);
        
        List<ProductResponse> productResponses = productsPage.getContent().stream()
            .map(this::convertToProductResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(productResponses, pageable, productsPage.getTotalElements());
    }

    // ===== MÉTHODES UTILITAIRES PRIVÉES =====

    private ProductResponse convertToProductResponse(Product product) {
        List<String> imageUrls = product.getImages() != null ? 
            product.getImages().stream()
                .map(Image::getUrl) // L'entité Image a un champ 'url'
                .collect(Collectors.toList()) : 
            List.of();

        return ProductResponse.builder()
            .productId(product.getProductId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stockQuantity(product.getStockQuantity())
            .category(product.getCategory())
            .poids(product.getPoids())
            .dimensions(product.getDimensions())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .statut(product.getStatut())
            .isAvailable(product.isAvailable())
            .isReported(product.isReported())
            .reportedReason(product.getReportedReason())
            .signalements(product.getSignalements())
            .imageUrls(imageUrls)
            .cooperativeName(product.getCooperative().getBrand())
            .build();
    }

    private void createProductImages(Product product, List<String> imageUrls) {
        // TODO: Implémenter la création des images
        // Cette méthode sera ajustée selon la structure de l'entité Image
    }

    private void updateProductImages(Product product, List<String> imageUrls) {
        // TODO: Implémenter la mise à jour des images
        // Cette méthode sera ajustée selon la structure de l'entité Image
    }
}