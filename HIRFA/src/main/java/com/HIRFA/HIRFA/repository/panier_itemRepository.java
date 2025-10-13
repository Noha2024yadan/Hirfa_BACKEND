package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Panier;
import com.HIRFA.HIRFA.entity.Product;
import com.HIRFA.HIRFA.entity.PanierItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface panier_itemRepository extends JpaRepository<PanierItem, UUID> {
    Optional<PanierItem> findByPanierAndProduct(Panier panier, Product product);

}
