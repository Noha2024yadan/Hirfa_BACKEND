package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByIsReportedTrue();

    Page<Product> findByIsReportedTrue(Pageable pageable);
}
