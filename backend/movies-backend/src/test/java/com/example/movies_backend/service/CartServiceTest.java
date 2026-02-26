package com.example.movies_backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CartServiceTest {

    /**
     * Phase 3 Current Behavior:
     * getCart() is a placeholder and returns null.
     */
    @Test
    void getCart_returnsNull_inPhase3() {
        CartService cartService = new CartService();

        Map<String, Object> cart = cartService.getCart();

        assertNull(
            cart,
            "Phase 3: getCart() is a placeholder and should return null"
        );
    }

    /**
     * Phase 4 Expected Behavior:
     * getCart() should return a non-null cart object.
     * Disabled until implementation is completed.
     */
    @Disabled("Phase 4: getCart() should return cart data after implementation")
    @Test
    void getCart_shouldReturnCartObject_inPhase4() {
        CartService cartService = new CartService();

        Map<String, Object> cart = cartService.getCart();

        assertNotNull(
            cart,
            "Phase 4 expected: getCart() should return non-null cart"
        );
    }

    /**
     * Phase 4 Expected Behavior:
     * addToCart should return operation result map.
     */
    @Disabled("Phase 4: addToCart() not implemented yet")
    @Test
    void addToCart_shouldReturnResultMap_inPhase4() {
        CartService cartService = new CartService();

        Map<String, Object> result =
            cartService.addToCart("tt123", 2);

        assertNotNull(
            result,
            "Phase 4 expected: addToCart() should return operation result map"
        );
    }

    /**
     * Phase 4 Expected Behavior:
     * updateCartItem should return updated cart info.
     */
    @Disabled("Phase 4: updateCartItem() not implemented yet")
    @Test
    void updateCartItem_shouldReturnResultMap_inPhase4() {
        CartService cartService = new CartService();

        Map<String, Object> result =
            cartService.updateCartItem("tt123", 5);

        assertNotNull(
            result,
            "Phase 4 expected: updateCartItem() should return operation result map"
        );
    }

    /**
     * Phase 4 Expected Behavior:
     * removeCartItem should return operation result map.
     */
    @Disabled("Phase 4: removeCartItem() not implemented yet")
    @Test
    void removeCartItem_shouldReturnResultMap_inPhase4() {
        CartService cartService = new CartService();

        Map<String, Object> result =
            cartService.removeCartItem("tt123");

        assertNotNull(
            result,
            "Phase 4 expected: removeCartItem() should return operation result map"
        );
    }
}