package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_requests")
public class ClientRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Request title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Request description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Request status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @NotNull(message = "Request priority is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private RequestPriority priority;

    @NotNull(message = "Request category is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private RequestCategory category;

    @Column(name = "budget_range")
    private String budgetRange; // e.g., "100-500", "500-1000", "1000+"

    @Column(name = "estimated_duration")
    private String estimatedDuration; // e.g., "1-2 weeks", "1 month", "2-3 months"

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "location")
    private String location;

    @Column(name = "contact_preference")
    private String contactPreference; // e.g., "Email", "Phone", "In-person"

    @Column(name = "additional_requirements", length = 1000)
    private String additionalRequirements;

    @Column(name = "attachments")
    private String attachments; // JSON string of file URLs

    @Column(name = "quoted_price", precision = 10, scale = 2)
    private BigDecimal quotedPrice;

    @Column(name = "quoted_duration")
    private String quotedDuration;

    @Column(name = "cooperative_response", length = 1000)
    private String cooperativeResponse;

    @Column(name = "cooperative_notes", length = 1000)
    private String cooperativeNotes;

    @Column(name = "client_feedback", length = 1000)
    private String clientFeedback;

    @Column(name = "rating")
    private Integer rating; // 1-5 stars

    @Column(name = "is_urgent", nullable = false)
    private Boolean isUrgent = false;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Foreign key to Client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Foreign key to Cooperative
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id", nullable = false)
    private Cooperative cooperative;

    // Request Status Enum
    public enum RequestStatus {
        PENDING("Pending"),
        REVIEWED("Reviewed"),
        QUOTED("Quoted"),
        ACCEPTED("Accepted"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled"),
        REJECTED("Rejected"),
        EXPIRED("Expired");

        private final String displayName;

        RequestStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Request Priority Enum
    public enum RequestPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        URGENT("Urgent");

        private final String displayName;

        RequestPriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Request Category Enum
    public enum RequestCategory {
        DESIGN("Design"),
        DEVELOPMENT("Development"),
        CONSULTATION("Consultation"),
        MAINTENANCE("Maintenance"),
        TRAINING("Training"),
        SUPPORT("Support"),
        OTHER("Other");

        private final String displayName;

        RequestCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public ClientRequest() {
    }

    public ClientRequest(String title, String description, RequestStatus status, RequestPriority priority,
            RequestCategory category, Client client, Cooperative cooperative) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.category = category;
        this.client = client;
        this.cooperative = cooperative;
    }

    // JPA Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestPriority getPriority() {
        return priority;
    }

    public void setPriority(RequestPriority priority) {
        this.priority = priority;
    }

    public RequestCategory getCategory() {
        return category;
    }

    public void setCategory(RequestCategory category) {
        this.category = category;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Cooperative getCooperative() {
        return cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
    }

    // Helper methods
    public boolean isCompleted() {
        return status == RequestStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return status == RequestStatus.CANCELLED || status == RequestStatus.REJECTED || status == RequestStatus.EXPIRED;
    }

    public boolean canBeAccepted() {
        return status == RequestStatus.QUOTED;
    }

    public boolean canBeQuoted() {
        return status == RequestStatus.PENDING || status == RequestStatus.REVIEWED;
    }

    public boolean isOverdue() {
        return deadline != null && LocalDateTime.now().isAfter(deadline) && !isCompleted() && !isCancelled();
    }

    public boolean needsAttention() {
        return status == RequestStatus.PENDING || status == RequestStatus.REVIEWED || isOverdue();
    }
}
