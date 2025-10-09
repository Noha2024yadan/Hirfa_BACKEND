package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    @Query("SELECT COUNT(l) FROM LoginAttempt l WHERE l.username = :username AND l.successful = false AND l.attemptTime > :since")
    long countFailedAttempts(@Param("username") String username, @Param("since") LocalDateTime since);

    @Query("SELECT l FROM LoginAttempt l WHERE l.username = :username AND l.attemptTime > :since ORDER BY l.attemptTime DESC")
    List<LoginAttempt> findRecentAttempts(@Param("username") String username, @Param("since") LocalDateTime since);
}