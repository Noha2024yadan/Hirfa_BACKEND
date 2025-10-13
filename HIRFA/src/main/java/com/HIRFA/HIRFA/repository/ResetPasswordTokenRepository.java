package com.HIRFA.HIRFA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.HIRFA.HIRFA.entity.ResetPasswordToken;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String token);

    void deleteByToken(String token);
}
