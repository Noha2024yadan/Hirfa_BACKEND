package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.User;
import com.HIRFA.HIRFA.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "(:search IS NULL OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.prenom) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:userType IS NULL OR u.userType = :userType) " +
            "AND (:enabled IS NULL OR u.enabled = :enabled)")
    Page<User> searchUsers(
            @Param("search") String search,
            @Param("userType") UserType userType,
            @Param("enabled") Boolean enabled,
            Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(:userType IS NULL OR u.userType = :userType) " +
            "AND (:enabled IS NULL OR u.enabled = :enabled)")
    Page<User> findByUserTypeAndEnabled(
            @Param("userType") UserType userType,
            @Param("enabled") Boolean enabled,
            Pageable pageable);

    Page<User> findByUserType(UserType userType, Pageable pageable);

    Page<User> findByEnabled(boolean enabled, Pageable pageable);

    long countByUserType(UserType userType);

    long countByDerniereConnexionAfter(LocalDateTime date);

    long countByDateCreationAfter(LocalDateTime date);
}
