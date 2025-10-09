package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.ClientRequestResponseDto;
import com.HIRFA.HIRFA.dto.RequestStatisticsDto;
import com.HIRFA.HIRFA.entity.ClientRequest;
import com.HIRFA.HIRFA.exception.ResourceNotFoundException;
import com.HIRFA.HIRFA.repository.ClientRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientRequestService {

    @Autowired
    private ClientRequestRepository clientRequestRepository;

    // Get all requests for a cooperative with pagination
    public Page<ClientRequestResponseDto> getAllRequestsByCooperative(UUID cooperativeId, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository
                .findByCooperativeUserIdOrderByCreatedAtDesc(cooperativeId, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests by status for a cooperative with pagination
    public Page<ClientRequestResponseDto> getRequestsByCooperativeAndStatus(UUID cooperativeId,
            ClientRequest.RequestStatus status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository
                .findByCooperativeUserIdAndStatusOrderByCreatedAtDesc(cooperativeId, status, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests by priority for a cooperative with pagination
    public Page<ClientRequestResponseDto> getRequestsByCooperativeAndPriority(UUID cooperativeId,
            ClientRequest.RequestPriority priority, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository
                .findByCooperativeUserIdAndPriorityOrderByCreatedAtDesc(cooperativeId, priority, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests by category for a cooperative with pagination
    public Page<ClientRequestResponseDto> getRequestsByCooperativeAndCategory(UUID cooperativeId,
            ClientRequest.RequestCategory category, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository
                .findByCooperativeUserIdAndCategoryOrderByCreatedAtDesc(cooperativeId, category, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get urgent requests for a cooperative with pagination
    public Page<ClientRequestResponseDto> getUrgentRequestsByCooperative(UUID cooperativeId, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository
                .findByCooperativeUserIdAndIsUrgentTrueOrderByCreatedAtDesc(cooperativeId, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get overdue requests for a cooperative with pagination
    public Page<ClientRequestResponseDto> getOverdueRequestsByCooperative(UUID cooperativeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("deadline").ascending());

        Page<ClientRequest> requests = clientRequestRepository.findOverdueRequestsByCooperative(cooperativeId,
                LocalDateTime.now(), pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests that need attention for a cooperative with pagination
    public Page<ClientRequestResponseDto> getRequestsNeedingAttentionByCooperative(UUID cooperativeId, int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        Page<ClientRequest> requests = clientRequestRepository.findRequestsNeedingAttentionByCooperative(cooperativeId,
                LocalDateTime.now(), pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Search requests for a cooperative
    public Page<ClientRequestResponseDto> searchRequestsByCooperative(UUID cooperativeId, String search, int page,
            int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.searchRequestsByCooperative(cooperativeId, search,
                pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests by multiple criteria for a cooperative
    public Page<ClientRequestResponseDto> getRequestsByCooperativeAndMultipleCriteria(UUID cooperativeId,
            ClientRequest.RequestStatus status,
            ClientRequest.RequestPriority priority, ClientRequest.RequestCategory category,
            Boolean isUrgent, LocalDateTime startDate, LocalDateTime endDate,
            String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.findByMultipleCriteria(cooperativeId, status, priority,
                category, isUrgent, startDate, endDate, search, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get request by ID for a cooperative
    public ClientRequestResponseDto getRequestByIdAndCooperative(Long requestId, UUID cooperativeId) {
        ClientRequest request = clientRequestRepository.findByIdAndCooperativeUserId(requestId, cooperativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        return new ClientRequestResponseDto(request);
    }

    // Get recent requests for a cooperative (last 30 days)
    public Page<ClientRequestResponseDto> getRecentRequestsByCooperative(UUID cooperativeId, int page, int size) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ClientRequest> requests = clientRequestRepository.findRecentRequestsByCooperative(cooperativeId,
                thirtyDaysAgo, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Update request status
    public ClientRequestResponseDto updateRequestStatus(Long requestId, UUID cooperativeId,
            ClientRequest.RequestStatus status) {
        ClientRequest request = clientRequestRepository.findByIdAndCooperativeUserId(requestId, cooperativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        request.setStatus(status);

        if (status == ClientRequest.RequestStatus.COMPLETED) {
            request.setCompletedAt(LocalDateTime.now());
        }

        ClientRequest savedRequest = clientRequestRepository.save(request);
        return new ClientRequestResponseDto(savedRequest);
    }

    // Add cooperative response to request
    public ClientRequestResponseDto addCooperativeResponse(Long requestId, UUID cooperativeId, String response,
            String notes) {
        ClientRequest request = clientRequestRepository.findByIdAndCooperativeUserId(requestId, cooperativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        request.setCooperativeResponse(response);
        request.setCooperativeNotes(notes);
        request.setRespondedAt(LocalDateTime.now());

        // Update status to reviewed if it was pending
        if (request.getStatus() == ClientRequest.RequestStatus.PENDING) {
            request.setStatus(ClientRequest.RequestStatus.REVIEWED);
        }

        ClientRequest savedRequest = clientRequestRepository.save(request);
        return new ClientRequestResponseDto(savedRequest);
    }

    // Add quote to request
    public ClientRequestResponseDto addQuoteToRequest(Long requestId, UUID cooperativeId,
            java.math.BigDecimal quotedPrice,
            String quotedDuration, String response, String notes) {
        ClientRequest request = clientRequestRepository.findByIdAndCooperativeUserId(requestId, cooperativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        request.setQuotedPrice(quotedPrice);
        request.setQuotedDuration(quotedDuration);
        request.setCooperativeResponse(response);
        request.setCooperativeNotes(notes);
        request.setStatus(ClientRequest.RequestStatus.QUOTED);
        request.setRespondedAt(LocalDateTime.now());

        ClientRequest savedRequest = clientRequestRepository.save(request);
        return new ClientRequestResponseDto(savedRequest);
    }

    // Accept request
    public ClientRequestResponseDto acceptRequest(Long requestId, UUID cooperativeId) {
        ClientRequest request = clientRequestRepository.findByIdAndCooperativeUserId(requestId, cooperativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        if (!request.canBeAccepted()) {
            throw new IllegalStateException("Request cannot be accepted in current status: " + request.getStatus());
        }

        request.setStatus(ClientRequest.RequestStatus.ACCEPTED);

        ClientRequest savedRequest = clientRequestRepository.save(request);
        return new ClientRequestResponseDto(savedRequest);
    }

    // Complete request
    public ClientRequestResponseDto completeRequest(Long requestId, UUID cooperativeId) {
        ClientRequest request = clientRequestRepository.findByIdAndCooperativeUserId(requestId, cooperativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        if (request.getStatus() != ClientRequest.RequestStatus.ACCEPTED
                && request.getStatus() != ClientRequest.RequestStatus.IN_PROGRESS) {
            throw new IllegalStateException("Request cannot be completed in current status: " + request.getStatus());
        }

        request.setStatus(ClientRequest.RequestStatus.COMPLETED);
        request.setCompletedAt(LocalDateTime.now());

        ClientRequest savedRequest = clientRequestRepository.save(request);
        return new ClientRequestResponseDto(savedRequest);
    }

    // Cancel request
    public ClientRequestResponseDto cancelRequest(Long requestId, UUID cooperativeId) {
        ClientRequest request = clientRequestRepository.findByIdAndCooperativeUserId(requestId, cooperativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        if (request.isCompleted() || request.isCancelled()) {
            throw new IllegalStateException("Request cannot be cancelled in current status: " + request.getStatus());
        }

        request.setStatus(ClientRequest.RequestStatus.CANCELLED);

        ClientRequest savedRequest = clientRequestRepository.save(request);
        return new ClientRequestResponseDto(savedRequest);
    }

    // Reject request
    public ClientRequestResponseDto rejectRequest(Long requestId, UUID cooperativeId, String reason) {
        ClientRequest request = clientRequestRepository.findByIdAndCooperativeUserId(requestId, cooperativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        if (request.isCompleted() || request.isCancelled()) {
            throw new IllegalStateException("Request cannot be rejected in current status: " + request.getStatus());
        }

        request.setStatus(ClientRequest.RequestStatus.REJECTED);
        request.setCooperativeNotes(reason);

        ClientRequest savedRequest = clientRequestRepository.save(request);
        return new ClientRequestResponseDto(savedRequest);
    }

    // Get request statistics for a cooperative
    public RequestStatisticsDto getRequestStatisticsByCooperative(UUID cooperativeId) {
        // Get basic counts
        Long totalRequests = clientRequestRepository.countByCooperativeUserId(cooperativeId);
        Long pendingRequests = clientRequestRepository.countByCooperativeUserIdAndStatus(cooperativeId,
                ClientRequest.RequestStatus.PENDING);
        Long completedRequests = clientRequestRepository.countByCooperativeUserIdAndStatus(cooperativeId,
                ClientRequest.RequestStatus.COMPLETED);
        Long cancelledRequests = clientRequestRepository.countByCooperativeUserIdAndStatus(cooperativeId,
                ClientRequest.RequestStatus.CANCELLED) +
                clientRequestRepository.countByCooperativeUserIdAndStatus(cooperativeId,
                        ClientRequest.RequestStatus.REJECTED);
        Long overdueRequests = clientRequestRepository.countOverdueRequestsByCooperative(cooperativeId,
                LocalDateTime.now());
        Long urgentRequests = clientRequestRepository.countByCooperativeUserIdAndIsUrgentTrue(cooperativeId);

        // Get status counts
        List<Object[]> statusCounts = clientRequestRepository.getRequestStatusCountsByCooperative(cooperativeId);
        Map<String, Long> statusCountMap = statusCounts.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]));

        // Get priority counts
        List<Object[]> priorityCounts = clientRequestRepository.getRequestPriorityCountsByCooperative(cooperativeId);
        Map<String, Long> priorityCountMap = priorityCounts.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]));

        // Get category counts
        List<Object[]> categoryCounts = clientRequestRepository.getRequestCategoryCountsByCooperative(cooperativeId);
        Map<String, Long> categoryCountMap = categoryCounts.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]));

        return new RequestStatisticsDto(totalRequests, pendingRequests, completedRequests, cancelledRequests,
                overdueRequests, urgentRequests, statusCountMap, priorityCountMap, categoryCountMap);
    }

    // Get all requests (admin only) with pagination
    public Page<ClientRequestResponseDto> getAllRequests(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.findAll(pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests by status (admin only) with pagination
    public Page<ClientRequestResponseDto> getRequestsByStatus(ClientRequest.RequestStatus status, int page, int size,
            String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests by priority (admin only) with pagination
    public Page<ClientRequestResponseDto> getRequestsByPriority(ClientRequest.RequestPriority priority, int page,
            int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.findByPriorityOrderByCreatedAtDesc(priority, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests by category (admin only) with pagination
    public Page<ClientRequestResponseDto> getRequestsByCategory(ClientRequest.RequestCategory category, int page,
            int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get urgent requests (admin only) with pagination
    public Page<ClientRequestResponseDto> getUrgentRequests(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.findByIsUrgentTrueOrderByCreatedAtDesc(pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get overdue requests (admin only) with pagination
    public Page<ClientRequestResponseDto> getOverdueRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("deadline").ascending());

        Page<ClientRequest> requests = clientRequestRepository.findOverdueRequests(LocalDateTime.now(), pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests that need attention (admin only) with pagination
    public Page<ClientRequestResponseDto> getRequestsNeedingAttention(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        Page<ClientRequest> requests = clientRequestRepository.findRequestsNeedingAttention(LocalDateTime.now(),
                pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Search requests (admin only)
    public Page<ClientRequestResponseDto> searchRequests(String search, int page, int size, String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.searchRequests(search, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get requests by multiple criteria (admin only)
    public Page<ClientRequestResponseDto> getRequestsByMultipleCriteria(ClientRequest.RequestStatus status,
            ClientRequest.RequestPriority priority, ClientRequest.RequestCategory category,
            Boolean isUrgent, LocalDateTime startDate, LocalDateTime endDate,
            String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClientRequest> requests = clientRequestRepository.findByMultipleCriteria(null, status, priority, category,
                isUrgent, startDate, endDate, search, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get request by ID (admin only)
    public ClientRequestResponseDto getRequestById(Long requestId) {
        ClientRequest request = clientRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        return new ClientRequestResponseDto(request);
    }

    // Get recent requests (admin only)
    public Page<ClientRequestResponseDto> getRecentRequests(int page, int size) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ClientRequest> requests = clientRequestRepository.findRecentRequests(thirtyDaysAgo, pageable);
        return requests.map(ClientRequestResponseDto::new);
    }

    // Get request statistics (admin only)
    public RequestStatisticsDto getRequestStatistics() {
        // Get basic counts
        Long totalRequests = clientRequestRepository.count();
        Long pendingRequests = clientRequestRepository.countByStatus(ClientRequest.RequestStatus.PENDING);
        Long completedRequests = clientRequestRepository.countByStatus(ClientRequest.RequestStatus.COMPLETED);
        Long cancelledRequests = clientRequestRepository.countByStatus(ClientRequest.RequestStatus.CANCELLED) +
                clientRequestRepository.countByStatus(ClientRequest.RequestStatus.REJECTED);
        Long overdueRequests = clientRequestRepository.countOverdueRequests(LocalDateTime.now());
        Long urgentRequests = clientRequestRepository.countByIsUrgentTrue();

        return new RequestStatisticsDto(totalRequests, pendingRequests, completedRequests, cancelledRequests,
                overdueRequests, urgentRequests, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
