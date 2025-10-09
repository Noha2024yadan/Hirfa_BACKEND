package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.ClientRequest;
import com.HIRFA.HIRFA.entity.Cooperative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRequestRepository extends JpaRepository<ClientRequest, Long> {
    
    // Find requests by cooperative
    List<ClientRequest> findByCooperativeOrderByCreatedAtDesc(Cooperative cooperative);
    
    // Find requests by cooperative with pagination
    Page<ClientRequest> findByCooperativeOrderByCreatedAtDesc(Cooperative cooperative, Pageable pageable);
    
    // Find requests by cooperative ID
    Page<ClientRequest> findByCooperativeUserIdOrderByCreatedAtDesc(UUID cooperativeId, Pageable pageable);
    
    // Find requests by status
    List<ClientRequest> findByStatusOrderByCreatedAtDesc(ClientRequest.RequestStatus status);
    
    // Find requests by status with pagination
    Page<ClientRequest> findByStatusOrderByCreatedAtDesc(ClientRequest.RequestStatus status, Pageable pageable);
    
    // Find requests by cooperative and status
    List<ClientRequest> findByCooperativeAndStatusOrderByCreatedAtDesc(Cooperative cooperative, ClientRequest.RequestStatus status);
    
    // Find requests by cooperative and status with pagination
    Page<ClientRequest> findByCooperativeAndStatusOrderByCreatedAtDesc(Cooperative cooperative, ClientRequest.RequestStatus status, Pageable pageable);
    
    // Find requests by cooperative ID and status
    Page<ClientRequest> findByCooperativeUserIdAndStatusOrderByCreatedAtDesc(UUID cooperativeId, ClientRequest.RequestStatus status, Pageable pageable);
    
    // Find requests by priority
    List<ClientRequest> findByPriorityOrderByCreatedAtDesc(ClientRequest.RequestPriority priority);
    
    // Find requests by priority with pagination
    Page<ClientRequest> findByPriorityOrderByCreatedAtDesc(ClientRequest.RequestPriority priority, Pageable pageable);
    
    // Find requests by cooperative and priority
    Page<ClientRequest> findByCooperativeUserIdAndPriorityOrderByCreatedAtDesc(UUID cooperativeId, ClientRequest.RequestPriority priority, Pageable pageable);
    
    // Find requests by category
    List<ClientRequest> findByCategoryOrderByCreatedAtDesc(ClientRequest.RequestCategory category);
    
    // Find requests by category with pagination
    Page<ClientRequest> findByCategoryOrderByCreatedAtDesc(ClientRequest.RequestCategory category, Pageable pageable);
    
    // Find requests by cooperative and category
    Page<ClientRequest> findByCooperativeUserIdAndCategoryOrderByCreatedAtDesc(UUID cooperativeId, ClientRequest.RequestCategory category, Pageable pageable);
    
    // Find urgent requests
    List<ClientRequest> findByIsUrgentTrueOrderByCreatedAtDesc();
    
    // Find urgent requests with pagination
    Page<ClientRequest> findByIsUrgentTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // Find urgent requests by cooperative
    Page<ClientRequest> findByCooperativeUserIdAndIsUrgentTrueOrderByCreatedAtDesc(UUID cooperativeId, Pageable pageable);
    
    // Find overdue requests
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.deadline < :now AND cr.status NOT IN ('COMPLETED', 'CANCELLED', 'REJECTED', 'EXPIRED') ORDER BY cr.deadline ASC")
    List<ClientRequest> findOverdueRequests(@Param("now") LocalDateTime now);
    
    // Find overdue requests with pagination
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.deadline < :now AND cr.status NOT IN ('COMPLETED', 'CANCELLED', 'REJECTED', 'EXPIRED') ORDER BY cr.deadline ASC")
    Page<ClientRequest> findOverdueRequests(@Param("now") LocalDateTime now, Pageable pageable);
    
    // Find overdue requests by cooperative
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId AND cr.deadline < :now AND cr.status NOT IN ('COMPLETED', 'CANCELLED', 'REJECTED', 'EXPIRED') ORDER BY cr.deadline ASC")
    Page<ClientRequest> findOverdueRequestsByCooperative(@Param("cooperativeId") UUID cooperativeId, @Param("now") LocalDateTime now, Pageable pageable);
    
    // Find requests that need attention
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.status IN ('PENDING', 'REVIEWED') OR (cr.deadline < :now AND cr.status NOT IN ('COMPLETED', 'CANCELLED', 'REJECTED', 'EXPIRED')) ORDER BY cr.createdAt ASC")
    Page<ClientRequest> findRequestsNeedingAttention(@Param("now") LocalDateTime now, Pageable pageable);
    
    // Find requests that need attention by cooperative
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId AND (cr.status IN ('PENDING', 'REVIEWED') OR (cr.deadline < :now AND cr.status NOT IN ('COMPLETED', 'CANCELLED', 'REJECTED', 'EXPIRED'))) ORDER BY cr.createdAt ASC")
    Page<ClientRequest> findRequestsNeedingAttentionByCooperative(@Param("cooperativeId") UUID cooperativeId, @Param("now") LocalDateTime now, Pageable pageable);
    
    // Find requests by date range
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.createdAt BETWEEN :startDate AND :endDate ORDER BY cr.createdAt DESC")
    Page<ClientRequest> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    // Find requests by cooperative and date range
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId AND cr.createdAt BETWEEN :startDate AND :endDate ORDER BY cr.createdAt DESC")
    Page<ClientRequest> findByCooperativeAndCreatedAtBetween(@Param("cooperativeId") UUID cooperativeId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    // Search requests by title or description
    @Query("SELECT cr FROM ClientRequest cr WHERE " +
           "(LOWER(cr.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(cr.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY cr.createdAt DESC")
    Page<ClientRequest> searchRequests(@Param("search") String search, Pageable pageable);
    
    // Search requests by cooperative and title or description
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId AND " +
           "(LOWER(cr.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(cr.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY cr.createdAt DESC")
    Page<ClientRequest> searchRequestsByCooperative(@Param("cooperativeId") UUID cooperativeId, @Param("search") String search, Pageable pageable);
    
    // Find requests by multiple criteria
    @Query("SELECT cr FROM ClientRequest cr WHERE " +
           "(:cooperativeId IS NULL OR cr.cooperative.userId = :cooperativeId) AND " +
           "(:status IS NULL OR cr.status = :status) AND " +
           "(:priority IS NULL OR cr.priority = :priority) AND " +
           "(:category IS NULL OR cr.category = :category) AND " +
           "(:isUrgent IS NULL OR cr.isUrgent = :isUrgent) AND " +
           "(:startDate IS NULL OR cr.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR cr.createdAt <= :endDate) AND " +
           "(:search IS NULL OR LOWER(cr.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(cr.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ClientRequest> findByMultipleCriteria(@Param("cooperativeId") UUID cooperativeId,
                                              @Param("status") ClientRequest.RequestStatus status,
                                              @Param("priority") ClientRequest.RequestPriority priority,
                                              @Param("category") ClientRequest.RequestCategory category,
                                              @Param("isUrgent") Boolean isUrgent,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("search") String search,
                                              Pageable pageable);
    
    // Find request by ID and cooperative
    Optional<ClientRequest> findByIdAndCooperativeUserId(Long id, UUID cooperativeId);
    
    // Count requests by cooperative
    Long countByCooperative(Cooperative cooperative);
    
    // Count requests by cooperative ID
    Long countByCooperativeUserId(UUID cooperativeId);
    
    // Count requests by status
    Long countByStatus(ClientRequest.RequestStatus status);
    
    // Count requests by cooperative and status
    Long countByCooperativeUserIdAndStatus(UUID cooperativeId, ClientRequest.RequestStatus status);
    
    // Count requests by priority
    Long countByPriority(ClientRequest.RequestPriority priority);
    
    // Count requests by cooperative and priority
    Long countByCooperativeUserIdAndPriority(UUID cooperativeId, ClientRequest.RequestPriority priority);
    
    // Count requests by category
    Long countByCategory(ClientRequest.RequestCategory category);
    
    // Count requests by cooperative and category
    Long countByCooperativeUserIdAndCategory(UUID cooperativeId, ClientRequest.RequestCategory category);
    
    // Count urgent requests
    Long countByIsUrgentTrue();
    
    // Count urgent requests by cooperative
    Long countByCooperativeUserIdAndIsUrgentTrue(UUID cooperativeId);
    
    // Count overdue requests
    @Query("SELECT COUNT(cr) FROM ClientRequest cr WHERE cr.deadline < :now AND cr.status NOT IN ('COMPLETED', 'CANCELLED', 'REJECTED', 'EXPIRED')")
    Long countOverdueRequests(@Param("now") LocalDateTime now);
    
    // Count overdue requests by cooperative
    @Query("SELECT COUNT(cr) FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId AND cr.deadline < :now AND cr.status NOT IN ('COMPLETED', 'CANCELLED', 'REJECTED', 'EXPIRED')")
    Long countOverdueRequestsByCooperative(@Param("cooperativeId") UUID cooperativeId, @Param("now") LocalDateTime now);
    
    // Get request statistics by cooperative
    @Query("SELECT cr.status, COUNT(cr) FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId GROUP BY cr.status")
    List<Object[]> getRequestStatusCountsByCooperative(@Param("cooperativeId") UUID cooperativeId);
    
    // Get request statistics by priority for cooperative
    @Query("SELECT cr.priority, COUNT(cr) FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId GROUP BY cr.priority")
    List<Object[]> getRequestPriorityCountsByCooperative(@Param("cooperativeId") UUID cooperativeId);
    
    // Get request statistics by category for cooperative
    @Query("SELECT cr.category, COUNT(cr) FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId GROUP BY cr.category")
    List<Object[]> getRequestCategoryCountsByCooperative(@Param("cooperativeId") UUID cooperativeId);
    
    // Find recent requests (last 30 days)
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.createdAt >= :thirtyDaysAgo ORDER BY cr.createdAt DESC")
    Page<ClientRequest> findRecentRequests(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo, Pageable pageable);
    
    // Find recent requests by cooperative
    @Query("SELECT cr FROM ClientRequest cr WHERE cr.cooperative.userId = :cooperativeId AND cr.createdAt >= :thirtyDaysAgo ORDER BY cr.createdAt DESC")
    Page<ClientRequest> findRecentRequestsByCooperative(@Param("cooperativeId") UUID cooperativeId, @Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo, Pageable pageable);
}




