package com.example.movies_backend.dto;

import java.util.List;

public class CartResponseDTO {
    private List<MovieSummaryDTO> items;
    private Integer totalItems;

    public CartResponseDTO() {
    }

    public CartResponseDTO(List<MovieSummaryDTO> items, Integer totalItems) {
        this.items = items;
        this.totalItems = totalItems;
    }

    public List<MovieSummaryDTO> getItems() {
        return items;
    }

    public void setItems(List<MovieSummaryDTO> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
}
