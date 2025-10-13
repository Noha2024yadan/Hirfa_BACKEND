package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.Product;
import com.HIRFA.HIRFA.entity.User;
import com.HIRFA.HIRFA.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    // Save or update product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Delete product
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    // Get reported products
    public List<Product> getReportedProducts() {
        return productRepository.findByIsReportedTrue();
    }

    // Get reported products with pagination
    public Page<Product> getReportedProducts(Pageable pageable) {
        return productRepository.findByIsReportedTrue(pageable);
    }

    // Report a product
    public Product reportProduct(UUID productId, String reason, User reportedBy) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setReported(true);
            product.setReportedReason(reason);
            product.setReportedBy(reportedBy);
            product.setReportedAt(LocalDateTime.now());
            return productRepository.save(product);
        }
        return null;
    }

    // Approve reported product (unreport)
    public Product approveProduct(UUID productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setReported(false);
            product.setReportedReason(null);
            product.setReportedBy(null);
            product.setReportedAt(null);
            return productRepository.save(product);
        }
        return null;
    }

    // Reject reported product (disable)
    public Product rejectProduct(UUID productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setAvailable(false);
            product.setReported(false);
            product.setReportedReason(null);
            product.setReportedBy(null);
            product.setReportedAt(null);
            return productRepository.save(product);
        }
        return null;
    }
}
