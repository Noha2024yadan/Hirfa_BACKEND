package com.HIRFA.HIRFA.dto;

import com.HIRFA.HIRFA.entity.Design;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class DesignResponseDto {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private Boolean isAvailable;
    private Boolean isFeatured;
    private Double rating;
    private Integer reviewCount;
    private Integer downloadCount;
    private String tags;
    private String fileFormat;
    private Long fileSize;
    private String dimensions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Designer information
    private UUID designerId;
    private String designerName;
    private String designerUsername;
    private String designerPortfolio;

    // Constructors
    public DesignResponseDto() {
    }

    public DesignResponseDto(Design design) {
        this.id = design.getDesignId();
        this.title = design.getTitle() != null ? design.getTitle() : design.getNomDesign();
        this.description = design.getDescription();
        this.price = design.getPrix();
        this.category = design.getCategory();
        this.imageUrl = design.getImageUrl();
        this.isAvailable = design.isAvailable();
        this.createdAt = design.getDateCreation();
        // Other fields not in Design entity, set to null or default
        this.isFeatured = null;
        this.rating = null;
        this.reviewCount = null;
        this.downloadCount = null;
        this.tags = null;
        this.fileFormat = null;
        this.fileSize = null;
        this.dimensions = null;
        this.updatedAt = null;

        // Designer information - assuming Design has a designer field, but it doesn't,
        // so skip or add later
        this.designerId = null;
        this.designerName = null;
        this.designerUsername = null;
        this.designerPortfolio = null;
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesignerId() {
        return designerId;
    }

    public void setDesignerId(UUID designerId) {
        this.designerId = designerId;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getDesignerUsername() {
        return designerUsername;
    }

    public void setDesignerUsername(String designerUsername) {
        this.designerUsername = designerUsername;
    }

    public String getDesignerPortfolio() {
        return designerPortfolio;
    }

    public void setDesignerPortfolio(String designerPortfolio) {
        this.designerPortfolio = designerPortfolio;
    }
}
