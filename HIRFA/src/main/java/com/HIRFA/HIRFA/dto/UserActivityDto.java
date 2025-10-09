package com.HIRFA.HIRFA.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for user activity information.
 */
public class UserActivityDto {
    private String username;
    private String email;
    private LocalDateTime lastLogin;
    private String activityType;
    private String details;
    private LocalDateTime activityTime;

    public UserActivityDto(String username, String email, LocalDateTime lastLogin, 
                          String activityType, String details) {
        this.username = username;
        this.email = email;
        this.lastLogin = lastLogin;
        this.activityType = activityType;
        this.details = details;
        this.activityTime = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(LocalDateTime activityTime) {
        this.activityTime = activityTime;
    }
}
