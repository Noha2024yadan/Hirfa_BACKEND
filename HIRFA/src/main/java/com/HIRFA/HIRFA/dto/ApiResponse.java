package com.HIRFA.HIRFA.dto;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;

    // Default constructor
    public ApiResponse() {
    }

    // Success constructor with message
    public ApiResponse(T data, String message) {
        this.success = true;
        this.data = data;
        this.message = message;
    }

    // Success constructor
    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
    }

    // Error constructor with data
    public ApiResponse(T data, String message, String errorCode) {
        this.success = false;
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
    }

    // Error constructor
    public ApiResponse(String message, String errorCode) {
        this.success = false;
        this.message = message;
        this.errorCode = errorCode;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}
