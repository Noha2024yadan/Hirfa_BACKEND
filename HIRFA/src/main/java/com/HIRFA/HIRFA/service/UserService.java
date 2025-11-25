package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Object registerUser(BaseUserRegistrationDto registrationDto);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Object> findUserById(UUID userId);

    Page<Object> findAllUsers(UserType userType, Boolean enabled, String search, Pageable pageable);

    Object updateUserStatus(UUID userId, boolean enabled);

    void deleteUser(UUID userId);

    long countAllUsers();

    long countUsersByType(UserType userType);

    long countActiveUsersToday();

    long countNewUsersThisWeek();

    Page<Object> searchUsers(String query, Pageable pageable);

    /**
     * Retrieves a page of recent user activities.
     *
     * @param pageable pagination information
     * @return page of user activities
     */
    Page<UserActivityDto> getRecentActivities(Pageable pageable);

    Object registerUser(Object registrationDto);
}
