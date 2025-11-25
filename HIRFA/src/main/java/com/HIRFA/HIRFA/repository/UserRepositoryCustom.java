package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepositoryCustom { // Renamed because no User entity
        @Query("SELECT u FROM User u WHERE " + // This query will need a valid entity, so it's only example
                        "(:search IS NULL OR " +
                        "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
                        "AND (:userType IS NULL OR u.userType = :userType) " +
                        "AND (:enabled IS NULL OR u.enabled = :enabled)")
        Page<Object> searchUsers(
                        @Param("search") String search,
                        @Param("userType") UserType userType,
                        @Param("enabled") Boolean enabled,
                        Pageable pageable);
}
