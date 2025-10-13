package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    List<Image> findByProduct_ProductId(UUID productId);

    List<Image> findByDesign_DesignId(UUID designId);
}
