package com.example.movies_backend.dto;

import java.util.List;
import java.util.Map;

public class CheckoutResponseDTO {
    private boolean success;
    private String message;
    private String orderId;
    private List<Map<String, Object>> items;

    public CheckoutResponseDTO() {
    }

    public CheckoutResponseDTO(boolean success, String message) {
        this(success, message, null, null);
    }

    public CheckoutResponseDTO(boolean success, String message, String orderId, List<Map<String, Object>> items) {
        this.success = success;
        this.message = message;
        this.orderId = orderId;
        this.items = items;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }
}
