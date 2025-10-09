package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Design;
import com.HIRFA.HIRFA.entity.Designer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {

       // Find all available designs
       List<Design> findByIsAvailableTrue();

       // Find designs by category
       List<Design> findByCategoryAndIsAvailableTrue(String category);

       // Find designs by category with pagination
       Page<Design> findByCategoryAndIsAvailableTrue(String category, Pageable pageable);

       // Find all available designs with pagination
       Page<Design> findByIsAvailableTrue(Pageable pageable);

       // Find featured designs
       Page<Design> findByIsFeaturedTrueAndIsAvailableTrue(Pageable pageable);

       // Find designs by designer
       Page<Design> findByDesignerAndIsAvailableTrue(Designer designer, Pageable pageable);

       // Find designs by designer ID
       Page<Design> findByDesignerUserIdAndIsAvailableTrue(UUID designerId, Pageable pageable);

       // Search designs by title, description, or tags
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true AND " +
                     "(LOWER(d.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(d.tags) LIKE LOWER(CONCAT('%', :search, '%')))")
       Page<Design> searchAvailableDesigns(@Param("search") String search, Pageable pageable);

       // Search designs by title, description, tags, or category
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true AND " +
                     "(LOWER(d.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(d.tags) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(d.category) LIKE LOWER(CONCAT('%', :search, '%')))")
       Page<Design> searchAvailableDesignsByAllFields(@Param("search") String search, Pageable pageable);

       // Find designs by price range
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true AND d.price BETWEEN :minPrice AND :maxPrice")
       Page<Design> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice,
                     Pageable pageable);

       // Find designs by category and price range
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true AND d.category = :category AND d.price BETWEEN :minPrice AND :maxPrice")
       Page<Design> findByCategoryAndPriceRange(@Param("category") String category,
                     @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

       // Find designs by file format
       Page<Design> findByFileFormatAndIsAvailableTrue(String fileFormat, Pageable pageable);

       // Find designs by rating range
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true AND d.rating BETWEEN :minRating AND :maxRating")
       Page<Design> findByRatingRange(@Param("minRating") Double minRating, @Param("maxRating") Double maxRating,
                     Pageable pageable);

       // Find designs by multiple criteria
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true AND " +
                     "(:category IS NULL OR d.category = :category) AND " +
                     "(:fileFormat IS NULL OR d.fileFormat = :fileFormat) AND " +
                     "(:minPrice IS NULL OR d.price >= :minPrice) AND " +
                     "(:maxPrice IS NULL OR d.price <= :maxPrice) AND " +
                     "(:minRating IS NULL OR d.rating >= :minRating) AND " +
                     "(:maxRating IS NULL OR d.rating <= :maxRating)")
       Page<Design> findByMultipleCriteria(@Param("category") String category,
                     @Param("fileFormat") String fileFormat,
                     @Param("minPrice") BigDecimal minPrice,
                     @Param("maxPrice") BigDecimal maxPrice,
                     @Param("minRating") Double minRating,
                     @Param("maxRating") Double maxRating,
                     Pageable pageable);

       // Get all unique categories
       @Query("SELECT DISTINCT d.category FROM Design d WHERE d.isAvailable = true ORDER BY d.category")
       List<String> findDistinctCategories();

       // Get all unique file formats
       @Query("SELECT DISTINCT d.fileFormat FROM Design d WHERE d.isAvailable = true AND d.fileFormat IS NOT NULL ORDER BY d.fileFormat")
       List<String> findDistinctFileFormats();

       // Find design by ID and availability
       Optional<Design> findByIdAndIsAvailableTrue(Long id);

       // Find top-rated designs
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true ORDER BY d.rating DESC, d.reviewCount DESC")
       Page<Design> findTopRatedDesigns(Pageable pageable);

       // Find most downloaded designs
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true ORDER BY d.downloadCount DESC")
       Page<Design> findMostDownloadedDesigns(Pageable pageable);

       // Find recent designs
       @Query("SELECT d FROM Design d WHERE d.isAvailable = true ORDER BY d.createdAt DESC")
       Page<Design> findRecentDesigns(Pageable pageable);

       // Count designs by designer
       Long countByDesignerAndIsAvailableTrue(Designer designer);

       // Count designs by category
       Long countByCategoryAndIsAvailableTrue(String category);

       // ✅ Added methods to fix DesignService errors
       Long countByIsAvailableTrue();

       Long countByIsFeaturedTrueAndIsAvailableTrue();
}
