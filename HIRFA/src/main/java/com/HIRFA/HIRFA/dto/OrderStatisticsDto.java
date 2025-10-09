package com.HIRFA.HIRFA.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderStatisticsDto {
    private Long totalOrders;
    private Long pendingOrders;
    private Long completedOrders;
    private Long cancelledOrders;
    private BigDecimal totalAmount;
    private BigDecimal averageOrderValue;
    private Map<String, Long> statusCounts;
    private Map<String, Long> paymentStatusCounts;
    private List<OrderStatusSummary> orderStatusSummary;
    private List<PaymentStatusSummary> paymentStatusSummary;

    // Constructors
    public OrderStatisticsDto() {}

    public OrderStatisticsDto(Long totalOrders, Long pendingOrders, Long completedOrders, Long cancelledOrders,
                             BigDecimal totalAmount, BigDecimal averageOrderValue,
                             Map<String, Long> statusCounts, Map<String, Long> paymentStatusCounts) {
        this.totalOrders = totalOrders;
        this.pendingOrders = pendingOrders;
        this.completedOrders = completedOrders;
        this.cancelledOrders = cancelledOrders;
        this.totalAmount = totalAmount;
        this.averageOrderValue = averageOrderValue;
        this.statusCounts = statusCounts;
        this.paymentStatusCounts = paymentStatusCounts;
    }

    // Getters and Setters
    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Long getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(Long cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public Map<String, Long> getStatusCounts() {
        return statusCounts;
    }

    public void setStatusCounts(Map<String, Long> statusCounts) {
        this.statusCounts = statusCounts;
    }

    public Map<String, Long> getPaymentStatusCounts() {
        return paymentStatusCounts;
    }

    public void setPaymentStatusCounts(Map<String, Long> paymentStatusCounts) {
        this.paymentStatusCounts = paymentStatusCounts;
    }

    public List<OrderStatusSummary> getOrderStatusSummary() {
        return orderStatusSummary;
    }

    public void setOrderStatusSummary(List<OrderStatusSummary> orderStatusSummary) {
        this.orderStatusSummary = orderStatusSummary;
    }

    public List<PaymentStatusSummary> getPaymentStatusSummary() {
        return paymentStatusSummary;
    }

    public void setPaymentStatusSummary(List<PaymentStatusSummary> paymentStatusSummary) {
        this.paymentStatusSummary = paymentStatusSummary;
    }

    // Inner classes for summary data
    public static class OrderStatusSummary {
        private String status;
        private String displayName;
        private Long count;
        private BigDecimal totalAmount;

        public OrderStatusSummary() {}

        public OrderStatusSummary(String status, String displayName, Long count, BigDecimal totalAmount) {
            this.status = status;
            this.displayName = displayName;
            this.count = count;
            this.totalAmount = totalAmount;
        }

        // Getters and Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    }

    public static class PaymentStatusSummary {
        private String status;
        private String displayName;
        private Long count;
        private BigDecimal totalAmount;

        public PaymentStatusSummary() {}

        public PaymentStatusSummary(String status, String displayName, Long count, BigDecimal totalAmount) {
            this.status = status;
            this.displayName = displayName;
            this.count = count;
            this.totalAmount = totalAmount;
        }

        // Getters and Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    }
}




