package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.DesignResponseDto;
import com.HIRFA.HIRFA.entity.Design;
import com.HIRFA.HIRFA.exception.ResourceNotFoundException;
import com.HIRFA.HIRFA.repository.DesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DesignService {

    @Autowired
    private DesignRepository designRepository;

    // Get all available designs with pagination
    public Page<DesignResponseDto> getAllAvailableDesigns(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.findByIsAvailableTrue(pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get designs by category with pagination
    public Page<DesignResponseDto> getDesignsByCategory(String category, int page, int size, String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.findByCategoryAndIsAvailableTrue(category, pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Search designs with pagination
    public Page<DesignResponseDto> searchDesigns(String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.searchAvailableDesignsByAllFields(search, pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get designs by price range with pagination
    public Page<DesignResponseDto> getDesignsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get designs by category and price range with pagination
    public Page<DesignResponseDto> getDesignsByCategoryAndPriceRange(String category, BigDecimal minPrice,
            BigDecimal maxPrice, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.findByCategoryAndPriceRange(category, minPrice, maxPrice, pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get designs by file format with pagination
    public Page<DesignResponseDto> getDesignsByFileFormat(String fileFormat, int page, int size, String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.findByFileFormatAndIsAvailableTrue(fileFormat, pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get designs by rating range with pagination
    public Page<DesignResponseDto> getDesignsByRatingRange(Double minRating, Double maxRating, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.findByRatingRange(minRating, maxRating, pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get designs by multiple criteria with pagination
    public Page<DesignResponseDto> getDesignsByMultipleCriteria(String category, String fileFormat, BigDecimal minPrice,
            BigDecimal maxPrice,
            Double minRating, Double maxRating, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.findByMultipleCriteria(category, fileFormat, minPrice, maxPrice,
                minRating, maxRating, pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get design by ID
    public DesignResponseDto getDesignById(Long id) {
        Design design = designRepository.findByIdAndIsAvailableTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Design not found with id: " + id));
        return new DesignResponseDto(design);
    }

    // Get designs by designer ID with pagination
    public Page<DesignResponseDto> getDesignsByDesigner(UUID designerId, int page, int size, String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Design> designs = designRepository.findByDesignerUserIdAndIsAvailableTrue(designerId, pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get all categories
    public List<String> getAllCategories() {
        return designRepository.findDistinctCategories();
    }

    // Get all file formats
    public List<String> getAllFileFormats() {
        return designRepository.findDistinctFileFormats();
    }

    // Get featured designs
    public List<DesignResponseDto> getFeaturedDesigns(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        Page<Design> designs = designRepository.findByIsFeaturedTrueAndIsAvailableTrue(pageable);
        return designs.getContent().stream()
                .map(DesignResponseDto::new)
                .collect(Collectors.toList());
    }

    // Get top-rated designs
    public Page<DesignResponseDto> getTopRatedDesigns(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Design> designs = designRepository.findTopRatedDesigns(pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get most downloaded designs
    public Page<DesignResponseDto> getMostDownloadedDesigns(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Design> designs = designRepository.findMostDownloadedDesigns(pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get recent designs
    public Page<DesignResponseDto> getRecentDesigns(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Design> designs = designRepository.findRecentDesigns(pageable);
        return designs.map(DesignResponseDto::new);
    }

    // Get design statistics
    public DesignStatsDto getDesignStatistics() {
        long totalDesigns = designRepository.countByIsAvailableTrue();
        long featuredDesigns = designRepository.countByIsFeaturedTrueAndIsAvailableTrue();
        List<String> categories = designRepository.findDistinctCategories();
        List<String> fileFormats = designRepository.findDistinctFileFormats();

        return new DesignStatsDto(totalDesigns, featuredDesigns, categories.size(), fileFormats.size(), categories,
                fileFormats);
    }

    // Inner class for design statistics
    public static class DesignStatsDto {
        private long totalDesigns;
        private long featuredDesigns;
        private int categoryCount;
        private int fileFormatCount;
        private List<String> categories;
        private List<String> fileFormats;

        public DesignStatsDto(long totalDesigns, long featuredDesigns, int categoryCount, int fileFormatCount,
                List<String> categories, List<String> fileFormats) {
            this.totalDesigns = totalDesigns;
            this.featuredDesigns = featuredDesigns;
            this.categoryCount = categoryCount;
            this.fileFormatCount = fileFormatCount;
            this.categories = categories;
            this.fileFormats = fileFormats;
        }

        // Getters
        public long getTotalDesigns() {
            return totalDesigns;
        }

        public long getFeaturedDesigns() {
            return featuredDesigns;
        }

        public int getCategoryCount() {
            return categoryCount;
        }

        public int getFileFormatCount() {
            return fileFormatCount;
        }

        public List<String> getCategories() {
            return categories;
        }

        public List<String> getFileFormats() {
            return fileFormats;
        }
    }
}
