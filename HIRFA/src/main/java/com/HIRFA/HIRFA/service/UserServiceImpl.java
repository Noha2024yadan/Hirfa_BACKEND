package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.dto.UserActivityDto;
import com.HIRFA.HIRFA.entity.*;
import com.HIRFA.HIRFA.entity.UserType;
import com.HIRFA.HIRFA.exception.ResourceNotFoundException;
import com.HIRFA.HIRFA.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(BaseUserRegistrationDto registrationDto) {
        if (existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // This is a base implementation. Should be overridden in specific user type
        // services
        throw new UnsupportedOperationException("This method should be overridden by specific user type services");
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Page<User> findAllUsers(UserType userType, Boolean enabled, String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return userRepository.searchUsers(search, userType, enabled, pageable);
        } else if (userType != null && enabled != null) {
            return userRepository.findByUserTypeAndEnabled(userType, enabled, pageable);
        } else if (userType != null) {
            return userRepository.findByUserType(userType, pageable);
        } else if (enabled != null) {
            return userRepository.findByEnabled(enabled, pageable);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public User updateUserStatus(UUID userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    @Override
    public long countAllUsers() {
        return userRepository.count();
    }

    @Override
    public long countUsersByType(UserType userType) {
        return userRepository.countByUserType(userType);
    }

    @Override
    public long countActiveUsersToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return userRepository.countByDerniereConnexionAfter(startOfDay);
    }

    @Override
    public long countNewUsersThisWeek() {
        LocalDateTime startOfWeek = LocalDate.now().minusDays(7).atStartOfDay();
        return userRepository.countByDateCreationAfter(startOfWeek);
    }

    @Override
    public Page<User> searchUsers(String query, Pageable pageable) {
        return userRepository.searchUsers(query, null, null, pageable);
    }

    @Override
    public Page<UserActivityDto> getRecentActivities(Pageable pageable) {
        Page<User> users = userRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Direction.DESC, "derniereConnexion")));

        return users.map(user -> new UserActivityDto(
                user.getUsername(),
                user.getEmail(),
                user.getDerniereConnexion(),
                "LOGIN",
                "User logged in"));
    }

    protected String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
