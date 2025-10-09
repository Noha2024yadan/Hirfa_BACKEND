package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.OrderResponseDto;
import com.HIRFA.HIRFA.dto.OrderStatisticsDto;
import com.HIRFA.HIRFA.entity.Order;
import com.HIRFA.HIRFA.exception.ResourceNotFoundException;
import com.HIRFA.HIRFA.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Get all orders for a user with pagination
    public Page<OrderResponseDto> getAllOrdersByUser(UUID userId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders by status for a user with pagination
    public Page<OrderResponseDto> getOrdersByUserAndStatus(UUID userId, Order.OrderStatus status, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders by payment status for a user with pagination
    public Page<OrderResponseDto> getOrdersByUserAndPaymentStatus(UUID userId, Order.PaymentStatus paymentStatus,
            int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByUserIdAndPaymentStatusOrderByCreatedAtDesc(userId, paymentStatus,
                pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders by date range for a user with pagination
    public Page<OrderResponseDto> getOrdersByUserAndDateRange(UUID userId, LocalDateTime startDate,
            LocalDateTime endDate, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders by amount range for a user with pagination
    public Page<OrderResponseDto> getOrdersByUserAndAmountRange(UUID userId, BigDecimal minAmount, BigDecimal maxAmount,
            int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByUserIdAndTotalAmountBetween(userId, minAmount, maxAmount, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Search orders by order number for a user
    public Page<OrderResponseDto> searchOrdersByUserAndOrderNumber(UUID userId, String orderNumber, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByUserIdAndOrderNumberContaining(userId, orderNumber, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders by multiple criteria for a user
    public Page<OrderResponseDto> getOrdersByUserAndMultipleCriteria(UUID userId, Order.OrderStatus status,
            Order.PaymentStatus paymentStatus,
            LocalDateTime startDate, LocalDateTime endDate, BigDecimal minAmount,
            BigDecimal maxAmount, String orderNumber, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByUserIdAndMultipleCriteria(userId, status, paymentStatus, startDate,
                endDate, minAmount, maxAmount, orderNumber, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get order by ID for a user
    public OrderResponseDto getOrderByIdAndUser(Long orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Order not found for user");
        }

        return new OrderResponseDto(order);
    }

    // Get order by order number for a user
    public OrderResponseDto getOrderByOrderNumberAndUser(String orderNumber, UUID userId) {
        Order order = orderRepository.findByOrderNumberAndUserId(orderNumber, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));

        return new OrderResponseDto(order);
    }

    // Get recent orders for a user (last 30 days)
    public Page<OrderResponseDto> getRecentOrdersByUser(UUID userId, int page, int size) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orders = orderRepository.findRecentOrdersByUser(userId, thirtyDaysAgo, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders that need attention for a user
    public Page<OrderResponseDto> getOrdersNeedingAttentionByUser(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        Page<Order> orders = orderRepository.findOrdersNeedingAttentionByUser(userId, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get order statistics for a user
    public OrderStatisticsDto getOrderStatisticsByUser(UUID userId) {
        // Get basic counts
        Long totalOrders = orderRepository.countByUserId(userId);
        Long pendingOrders = orderRepository.countByUserIdAndStatus(userId, Order.OrderStatus.PENDING);
        Long completedOrders = orderRepository.countByUserIdAndStatus(userId, Order.OrderStatus.DELIVERED);
        Long cancelledOrders = orderRepository.countByUserIdAndStatus(userId, Order.OrderStatus.CANCELLED) +
                orderRepository.countByUserIdAndStatus(userId, Order.OrderStatus.REFUNDED) +
                orderRepository.countByUserIdAndStatus(userId, Order.OrderStatus.RETURNED);

        // Get total amount
        BigDecimal totalAmount = orderRepository.getTotalAmountByUser(userId);
        if (totalAmount == null)
            totalAmount = BigDecimal.ZERO;

        // Calculate average order value
        BigDecimal averageOrderValue = totalOrders > 0
                ? totalAmount.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Get status counts
        List<Object[]> statusCounts = orderRepository.getOrderStatusCountsByUser(userId);
        Map<String, Long> statusCountMap = statusCounts.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]));

        // Get payment status counts
        List<Object[]> paymentStatusCounts = orderRepository.getPaymentStatusCountsByUser(userId);
        Map<String, Long> paymentStatusCountMap = paymentStatusCounts.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]));

        return new OrderStatisticsDto(totalOrders, pendingOrders, completedOrders, cancelledOrders,
                totalAmount, averageOrderValue, statusCountMap, paymentStatusCountMap);
    }

    // Get all orders (admin only) with pagination
    public Page<OrderResponseDto> getAllOrders(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders by status (admin only) with pagination
    public Page<OrderResponseDto> getOrdersByStatus(Order.OrderStatus status, int page, int size, String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders by payment status (admin only) with pagination
    public Page<OrderResponseDto> getOrdersByPaymentStatus(Order.PaymentStatus paymentStatus, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByPaymentStatusOrderByCreatedAtDesc(paymentStatus, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders by multiple criteria (admin only)
    public Page<OrderResponseDto> getOrdersByMultipleCriteria(Order.OrderStatus status,
            Order.PaymentStatus paymentStatus,
            LocalDateTime startDate, LocalDateTime endDate, BigDecimal minAmount,
            BigDecimal maxAmount, String orderNumber, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByMultipleCriteria(null, status, paymentStatus, startDate, endDate,
                minAmount, maxAmount, orderNumber, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get order by ID (admin only)
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return new OrderResponseDto(order);
    }

    // Get order by order number (admin only)
    public OrderResponseDto getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));

        return new OrderResponseDto(order);
    }

    // Get orders by tracking number
    public List<OrderResponseDto> getOrdersByTrackingNumber(String trackingNumber) {
        List<Order> orders = orderRepository.findByTrackingNumber(trackingNumber);
        return orders.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    // Get orders by payment reference
    public List<OrderResponseDto> getOrdersByPaymentReference(String paymentReference) {
        List<Order> orders = orderRepository.findByPaymentReference(paymentReference);
        return orders.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    // Get recent orders (admin only)
    public Page<OrderResponseDto> getRecentOrders(int page, int size) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orders = orderRepository.findRecentOrders(thirtyDaysAgo, pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get orders that need attention (admin only)
    public Page<OrderResponseDto> getOrdersNeedingAttention(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        Page<Order> orders = orderRepository.findOrdersNeedingAttention(pageable);
        return orders.map(OrderResponseDto::new);
    }

    // Get order statistics (admin only)
    public OrderStatisticsDto getOrderStatistics() {
        // Get basic counts
        Long totalOrders = orderRepository.count();
        Long pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        Long completedOrders = orderRepository.countByStatus(Order.OrderStatus.DELIVERED);
        Long cancelledOrders = orderRepository.countByStatus(Order.OrderStatus.CANCELLED) +
                orderRepository.countByStatus(Order.OrderStatus.REFUNDED) +
                orderRepository.countByStatus(Order.OrderStatus.RETURNED);

        // Get total amount (this would need a custom query for all orders)
        // For now, we'll calculate it from individual orders
        BigDecimal totalAmount = BigDecimal.ZERO; // This would need a custom query

        // Calculate average order value
        BigDecimal averageOrderValue = totalOrders > 0
                ? totalAmount.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)

                : BigDecimal.ZERO;

        return new OrderStatisticsDto(totalOrders, pendingOrders, completedOrders, cancelledOrders,
                totalAmount, averageOrderValue, new HashMap<>(), new HashMap<>());
    }
}
