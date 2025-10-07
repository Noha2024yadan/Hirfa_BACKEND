package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
