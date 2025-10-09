package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.LoginAttempt;
import com.HIRFA.HIRFA.repository.LoginAttemptRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginSecurityService {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_TIME_DURATION = 15; // minutes

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    public void recordLoginAttempt(String username, boolean successful, HttpServletRequest request) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setUsername(username);
        attempt.setSuccessful(successful);
        attempt.setAttemptTime(LocalDateTime.now());
        attempt.setIpAddress(getClientIP(request));
        loginAttemptRepository.save(attempt);
    }

    public boolean isAccountLocked(String username) {
        LocalDateTime lockCheckTime = LocalDateTime.now().minusMinutes(LOCK_TIME_DURATION);
        long failedAttempts = loginAttemptRepository.countFailedAttempts(username, lockCheckTime);
        return failedAttempts >= MAX_FAILED_ATTEMPTS;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || "unknown".equalsIgnoreCase(xfHeader)) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}