package com.example.movies_backend.service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CartService {

    /**
     * Logic:
     * Returns current shopping cart data for active session/user.
     *
     * Params:
     * None.
     *
     * Return:
     * Cart object/map (planned for next phase).
     */
    public Map<String, Object> getCart() {
        throw new UnsupportedOperationException("Planned for next phase");
    }

    /**
     * Logic:
     * Adds a movie item to shopping cart.
     *
     * Params:
     * movieId: movie primary key.
     * quantity: item quantity to add.
     *
     * Return:
     * Operation result map (planned for next phase).
     */
    public Map<String, Object> addToCart(String movieId, Integer quantity) {
        throw new UnsupportedOperationException("Planned for next phase");
    }

    /**
     * Logic:
     * Updates quantity of one existing cart item.
     *
     * Params:
     * movieId: movie primary key.
     * quantity: new quantity.
     *
     * Return:
     * Operation result map (planned for next phase).
     */
    public Map<String, Object> updateCartItem(String movieId, Integer quantity) {
        throw new UnsupportedOperationException("Planned for next phase");
    }

    /**
     * Logic:
     * Removes one movie item from shopping cart.
     *
     * Params:
     * movieId: movie primary key.
     *
     * Return:
     * Operation result map (planned for next phase).
     */
    public Map<String, Object> removeCartItem(String movieId) {
        throw new UnsupportedOperationException("Planned for next phase");
    }
}
