package com.HIRFA.HIRFA.forgetpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String token);

    void deleteByToken(String token);
}
