package com.HIRFA.HIRFA.dto;

import com.HIRFA.HIRFA.entity.ClientRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClientRequestResponseDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String statusDisplayName;
    private String priority;
    private String priorityDisplayName;
    private String category;
    private String categoryDisplayName;
    private String budgetRange;
    private String estimatedDuration;
    private LocalDateTime deadline;
    private String location;
    private String contactPreference;
    private String additionalRequirements;
    private String attachments;
    private BigDecimal quotedPrice;
    private String quotedDuration;
    private String cooperativeResponse;
    private String cooperativeNotes;
    private String clientFeedback;
    private Integer rating;
    private Boolean isUrgent;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime respondedAt;
    private LocalDateTime completedAt;
    
    // Client information
    private UUID clientId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    
    // Cooperative information
    private UUID cooperativeId;
    private String cooperativeName;
    private String cooperativeEmail;
    
    // Request status flags
    private Boolean isCompleted;
    private Boolean isCancelled;
    private Boolean canBeAccepted;
    private Boolean canBeQuoted;
    private Boolean isOverdue;
    private Boolean needsAttention;

    // Constructors
    public ClientRequestResponseDto() {}

    public ClientRequestResponseDto(ClientRequest clientRequest) {
        this.id = clientRequest.getId();
        this.title = clientRequest.getTitle();
        this.description = clientRequest.getDescription();
        this.status = clientRequest.getStatus().name();
        this.statusDisplayName = clientRequest.getStatus().getDisplayName();
        this.priority = clientRequest.getPriority().name();
        this.priorityDisplayName = clientRequest.getPriority().getDisplayName();
        this.category = clientRequest.getCategory().name();
        this.categoryDisplayName = clientRequest.getCategory().getDisplayName();
        this.budgetRange = clientRequest.getBudgetRange();
        this.estimatedDuration = clientRequest.getEstimatedDuration();
        this.deadline = clientRequest.getDeadline();
        this.location = clientRequest.getLocation();
        this.contactPreference = clientRequest.getContactPreference();
        this.additionalRequirements = clientRequest.getAdditionalRequirements();
        this.attachments = clientRequest.getAttachments();
        this.quotedPrice = clientRequest.getQuotedPrice();
        this.quotedDuration = clientRequest.getQuotedDuration();
        this.cooperativeResponse = clientRequest.getCooperativeResponse();
        this.cooperativeNotes = clientRequest.getCooperativeNotes();
        this.clientFeedback = clientRequest.getClientFeedback();
        this.rating = clientRequest.getRating();
        this.isUrgent = clientRequest.getIsUrgent();
        this.isPublic = clientRequest.getIsPublic();
        this.createdAt = clientRequest.getCreatedAt();
        this.updatedAt = clientRequest.getUpdatedAt();
        this.respondedAt = clientRequest.getRespondedAt();
        this.completedAt = clientRequest.getCompletedAt();
        
        // Client information
        if (clientRequest.getClient() != null) {
            this.clientId = clientRequest.getClient().getUserId();
            this.clientName = clientRequest.getClient().getNom() + " " + clientRequest.getClient().getPrenom();
            this.clientEmail = clientRequest.getClient().getEmail();
            this.clientPhone = clientRequest.getClient().getTelephone();
        }
        
        // Cooperative information
        if (clientRequest.getCooperative() != null) {
            this.cooperativeId = clientRequest.getCooperative().getUserId();
            this.cooperativeName = clientRequest.getCooperative().getNom() + " " + clientRequest.getCooperative().getPrenom();
            this.cooperativeEmail = clientRequest.getCooperative().getEmail();
        }
        
        // Request status flags
        this.isCompleted = clientRequest.isCompleted();
        this.isCancelled = clientRequest.isCancelled();
        this.canBeAccepted = clientRequest.canBeAccepted();
        this.canBeQuoted = clientRequest.canBeQuoted();
        this.isOverdue = clientRequest.isOverdue();
        this.needsAttention = clientRequest.needsAttention();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDisplayName() {
        return statusDisplayName;
    }

    public void setStatusDisplayName(String statusDisplayName) {
        this.statusDisplayName = statusDisplayName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriorityDisplayName() {
        return priorityDisplayName;
    }

    public void setPriorityDisplayName(String priorityDisplayName) {
        this.priorityDisplayName = priorityDisplayName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryDisplayName() {
        return categoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        this.categoryDisplayName = categoryDisplayName;
    }

    public String getBudgetRange() {
        return budgetRange;
    }

    public void setBudgetRange(String budgetRange) {
        this.budgetRange = budgetRange;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactPreference() {
        return contactPreference;
    }

    public void setContactPreference(String contactPreference) {
        this.contactPreference = contactPreference;
    }

    public String getAdditionalRequirements() {
        return additionalRequirements;
    }

    public void setAdditionalRequirements(String additionalRequirements) {
        this.additionalRequirements = additionalRequirements;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public BigDecimal getQuotedPrice() {
        return quotedPrice;
    }

    public void setQuotedPrice(BigDecimal quotedPrice) {
        this.quotedPrice = quotedPrice;
    }

    public String getQuotedDuration() {
        return quotedDuration;
    }

    public void setQuotedDuration(String quotedDuration) {
        this.quotedDuration = quotedDuration;
    }

    public String getCooperativeResponse() {
        return cooperativeResponse;
    }

    public void setCooperativeResponse(String cooperativeResponse) {
        this.cooperativeResponse = cooperativeResponse;
    }

    public String getCooperativeNotes() {
        return cooperativeNotes;
    }

    public void setCooperativeNotes(String cooperativeNotes) {
        this.cooperativeNotes = cooperativeNotes;
    }

    public String getClientFeedback() {
        return clientFeedback;
    }

    public void setClientFeedback(String clientFeedback) {
        this.clientFeedback = clientFeedback;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
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

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public UUID getCooperativeId() {
        return cooperativeId;
    }

    public void setCooperativeId(UUID cooperativeId) {
        this.cooperativeId = cooperativeId;
    }

    public String getCooperativeName() {
        return cooperativeName;
    }

    public void setCooperativeName(String cooperativeName) {
        this.cooperativeName = cooperativeName;
    }

    public String getCooperativeEmail() {
        return cooperativeEmail;
    }

    public void setCooperativeEmail(String cooperativeEmail) {
        this.cooperativeEmail = cooperativeEmail;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Boolean getCanBeAccepted() {
        return canBeAccepted;
    }

    public void setCanBeAccepted(Boolean canBeAccepted) {
        this.canBeAccepted = canBeAccepted;
    }

    public Boolean getCanBeQuoted() {
        return canBeQuoted;
    }

    public void setCanBeQuoted(Boolean canBeQuoted) {
        this.canBeQuoted = canBeQuoted;
    }

    public Boolean getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }

    public Boolean getNeedsAttention() {
        return needsAttention;
    }

    public void setNeedsAttention(Boolean needsAttention) {
        this.needsAttention = needsAttention;
    }
}




