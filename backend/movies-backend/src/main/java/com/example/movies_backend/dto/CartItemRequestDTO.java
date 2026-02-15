package com.example.movies_backend.dto;

public class CartItemRequestDTO {
    private String movieId;
    private Integer quantity;

    public CartItemRequestDTO() {
    }

    public CartItemRequestDTO(String movieId, Integer quantity) {
        this.movieId = movieId;
        this.quantity = quantity;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
