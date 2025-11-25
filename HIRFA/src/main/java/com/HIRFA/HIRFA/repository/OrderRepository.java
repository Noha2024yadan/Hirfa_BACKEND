package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

       // Find orders by user ID
       Page<Order> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

       // Find orders by status
       List<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status);

       // Find orders by status with pagination
       Page<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status, Pageable pageable);

       // Find orders by user ID and status
       Page<Order> findByUserIdAndStatusOrderByCreatedAtDesc(UUID userId, Order.OrderStatus status, Pageable pageable);

       // Find orders by payment status
       List<Order> findByPaymentStatusOrderByCreatedAtDesc(Order.PaymentStatus paymentStatus);

       // Find orders by payment status with pagination
       Page<Order> findByPaymentStatusOrderByCreatedAtDesc(Order.PaymentStatus paymentStatus, Pageable pageable);

       // Find orders by user ID and payment status
       Page<Order> findByUserIdAndPaymentStatusOrderByCreatedAtDesc(UUID userId, Order.PaymentStatus paymentStatus,
                     Pageable pageable);

       // Find orders by date range
       @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
       Page<Order> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                     @Param("endDate") LocalDateTime endDate, Pageable pageable);

       // Find orders by user ID and date range
       @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
       Page<Order> findByUserIdAndCreatedAtBetween(@Param("userId") UUID userId,
                     @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                     Pageable pageable);

       // Find orders by total amount range
       @Query("SELECT o FROM Order o WHERE o.totalAmount BETWEEN :minAmount AND :maxAmount ORDER BY o.createdAt DESC")
       Page<Order> findByTotalAmountBetween(@Param("minAmount") BigDecimal minAmount,
                     @Param("maxAmount") BigDecimal maxAmount, Pageable pageable);

       // Find orders by user ID and total amount range
       @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.totalAmount BETWEEN :minAmount AND :maxAmount ORDER BY o.createdAt DESC")
       Page<Order> findByUserIdAndTotalAmountBetween(@Param("userId") UUID userId,
                     @Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount,
                     Pageable pageable);

       // Search orders by order number
       @Query("SELECT o FROM Order o WHERE o.orderNumber LIKE %:orderNumber% ORDER BY o.createdAt DESC")
       Page<Order> findByOrderNumberContaining(@Param("orderNumber") String orderNumber, Pageable pageable);

       // Search orders by user ID and order number
       @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.orderNumber LIKE %:orderNumber% ORDER BY o.createdAt DESC")
       Page<Order> findByUserIdAndOrderNumberContaining(@Param("userId") UUID userId,
                     @Param("orderNumber") String orderNumber, Pageable pageable);

       // Find orders by tracking number
       @Query("SELECT o FROM Order o WHERE o.trackingNumber = :trackingNumber ORDER BY o.createdAt DESC")
       List<Order> findByTrackingNumber(@Param("trackingNumber") String trackingNumber);

       // Find orders by payment reference
       @Query("SELECT o FROM Order o WHERE o.paymentReference = :paymentReference ORDER BY o.createdAt DESC")
       List<Order> findByPaymentReference(@Param("paymentReference") String paymentReference);

       // Count orders by user ID
       Long countByUserId(UUID userId);

       // Count orders by status
       Long countByStatus(Order.OrderStatus status);

       // Count orders by user ID and status
       Long countByUserIdAndStatus(UUID userId, Order.OrderStatus status);

       // Count orders by payment status
       Long countByPaymentStatus(Order.PaymentStatus paymentStatus);

       // Count orders by user ID and payment status
       Long countByUserIdAndPaymentStatus(UUID userId, Order.PaymentStatus paymentStatus);

       // Find recent orders (last 30 days)
       @Query("SELECT o FROM Order o WHERE o.createdAt >= :thirtyDaysAgo ORDER BY o.createdAt DESC")
       Page<Order> findRecentOrders(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo, Pageable pageable);

       // Find recent orders by user ID
       @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.createdAt >= :thirtyDaysAgo ORDER BY o.createdAt DESC")
       Page<Order> findRecentOrdersByUser(@Param("userId") UUID userId,
                     @Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo, Pageable pageable);

       // Find orders that need attention
       @Query("SELECT o FROM Order o WHERE o.status IN ('PENDING', 'CONFIRMED', 'PROCESSING') OR o.paymentStatus = 'PENDING' ORDER BY o.createdAt ASC")
       Page<Order> findOrdersNeedingAttention(Pageable pageable);

       // Find orders by user ID that need attention
       @Query("SELECT o FROM Order o WHERE o.userId = :userId AND (o.status IN ('PENDING', 'CONFIRMED', 'PROCESSING') OR o.paymentStatus = 'PENDING') ORDER BY o.createdAt ASC")
       Page<Order> findOrdersNeedingAttentionByUser(@Param("userId") UUID userId, Pageable pageable);

       // Get order statistics by user ID
       @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.userId = :userId GROUP BY o.status")
       List<Object[]> getOrderStatusCountsByUser(@Param("userId") UUID userId);

       // Get order statistics by payment status for user ID
       @Query("SELECT o.paymentStatus, COUNT(o) FROM Order o WHERE o.userId = :userId GROUP BY o.paymentStatus")
       List<Object[]> getPaymentStatusCountsByUser(@Param("userId") UUID userId);

       // Get total amount by user ID
       @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.userId = :userId")
       BigDecimal getTotalAmountByUser(@Param("userId") UUID userId);

       // Get total amount by user ID and status
       @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.userId = :userId AND o.status = :status")
       BigDecimal getTotalAmountByUserAndStatus(@Param("userId") UUID userId,
                     @Param("status") Order.OrderStatus status);

       // Find order by order number
       Optional<Order> findByOrderNumber(String orderNumber);

       // Find order by order number and user ID
       Optional<Order> findByOrderNumberAndUserId(String orderNumber, UUID userId);
}
