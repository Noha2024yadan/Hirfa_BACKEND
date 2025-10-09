package com.HIRFA.HIRFA.dto;

import java.util.List;
import java.util.Map;

public class RequestStatisticsDto {
    private Long totalRequests;
    private Long pendingRequests;
    private Long completedRequests;
    private Long cancelledRequests;
    private Long overdueRequests;
    private Long urgentRequests;
    private Map<String, Long> statusCounts;
    private Map<String, Long> priorityCounts;
    private Map<String, Long> categoryCounts;
    private List<RequestStatusSummary> requestStatusSummary;
    private List<RequestPrioritySummary> requestPrioritySummary;
    private List<RequestCategorySummary> requestCategorySummary;

    // Constructors
    public RequestStatisticsDto() {}

    public RequestStatisticsDto(Long totalRequests, Long pendingRequests, Long completedRequests, Long cancelledRequests,
                               Long overdueRequests, Long urgentRequests,
                               Map<String, Long> statusCounts, Map<String, Long> priorityCounts, Map<String, Long> categoryCounts) {
        this.totalRequests = totalRequests;
        this.pendingRequests = pendingRequests;
        this.completedRequests = completedRequests;
        this.cancelledRequests = cancelledRequests;
        this.overdueRequests = overdueRequests;
        this.urgentRequests = urgentRequests;
        this.statusCounts = statusCounts;
        this.priorityCounts = priorityCounts;
        this.categoryCounts = categoryCounts;
    }

    // Getters and Setters
    public Long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public Long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(Long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public Long getCompletedRequests() {
        return completedRequests;
    }

    public void setCompletedRequests(Long completedRequests) {
        this.completedRequests = completedRequests;
    }

    public Long getCancelledRequests() {
        return cancelledRequests;
    }

    public void setCancelledRequests(Long cancelledRequests) {
        this.cancelledRequests = cancelledRequests;
    }

    public Long getOverdueRequests() {
        return overdueRequests;
    }

    public void setOverdueRequests(Long overdueRequests) {
        this.overdueRequests = overdueRequests;
    }

    public Long getUrgentRequests() {
        return urgentRequests;
    }

    public void setUrgentRequests(Long urgentRequests) {
        this.urgentRequests = urgentRequests;
    }

    public Map<String, Long> getStatusCounts() {
        return statusCounts;
    }

    public void setStatusCounts(Map<String, Long> statusCounts) {
        this.statusCounts = statusCounts;
    }

    public Map<String, Long> getPriorityCounts() {
        return priorityCounts;
    }

    public void setPriorityCounts(Map<String, Long> priorityCounts) {
        this.priorityCounts = priorityCounts;
    }

    public Map<String, Long> getCategoryCounts() {
        return categoryCounts;
    }

    public void setCategoryCounts(Map<String, Long> categoryCounts) {
        this.categoryCounts = categoryCounts;
    }

    public List<RequestStatusSummary> getRequestStatusSummary() {
        return requestStatusSummary;
    }

    public void setRequestStatusSummary(List<RequestStatusSummary> requestStatusSummary) {
        this.requestStatusSummary = requestStatusSummary;
    }

    public List<RequestPrioritySummary> getRequestPrioritySummary() {
        return requestPrioritySummary;
    }

    public void setRequestPrioritySummary(List<RequestPrioritySummary> requestPrioritySummary) {
        this.requestPrioritySummary = requestPrioritySummary;
    }

    public List<RequestCategorySummary> getRequestCategorySummary() {
        return requestCategorySummary;
    }

    public void setRequestCategorySummary(List<RequestCategorySummary> requestCategorySummary) {
        this.requestCategorySummary = requestCategorySummary;
    }

    // Inner classes for summary data
    public static class RequestStatusSummary {
        private String status;
        private String displayName;
        private Long count;

        public RequestStatusSummary() {}

        public RequestStatusSummary(String status, String displayName, Long count) {
            this.status = status;
            this.displayName = displayName;
            this.count = count;
        }

        // Getters and Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public static class RequestPrioritySummary {
        private String priority;
        private String displayName;
        private Long count;

        public RequestPrioritySummary() {}

        public RequestPrioritySummary(String priority, String displayName, Long count) {
            this.priority = priority;
            this.displayName = displayName;
            this.count = count;
        }

        // Getters and Setters
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public static class RequestCategorySummary {
        private String category;
        private String displayName;
        private Long count;

        public RequestCategorySummary() {}

        public RequestCategorySummary(String category, String displayName, Long count) {
            this.category = category;
            this.displayName = displayName;
            this.count = count;
        }

        // Getters and Setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }
}




