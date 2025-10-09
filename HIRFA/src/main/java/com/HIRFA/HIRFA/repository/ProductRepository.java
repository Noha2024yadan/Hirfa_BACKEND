package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find all reported products
    List<Product> findByIsReportedTrue();

    // Find reported products with pagination
    Page<Product> findByIsReportedTrue(Pageable pageable);
}
