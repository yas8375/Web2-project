package com.example.movies_backend.dto;

public class LoginResponseDTO {
    private boolean success;
    private String message;
    private String token;
    private String tokenType;
    private Integer customerId;
    private Long expiresInSeconds;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponseDTO(
            boolean success,
            String message,
            String token,
            String tokenType,
            Integer customerId,
            Long expiresInSeconds) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.tokenType = tokenType;
        this.customerId = customerId;
        this.expiresInSeconds = expiresInSeconds;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public void setExpiresInSeconds(Long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }
}
