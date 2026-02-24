package com.example.movies_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movies_backend.service.CartService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Logic:
     * Returns current shopping cart items for session.
     *
     * Params:
     * None.
     *
     * Return:
     * HTTP 501 Not Implemented for Phase 2 (contract only).
     */
    @GetMapping("/cart")
    public ResponseEntity<?> getCart() {
        // Service Contract:
        cartService.getCart();

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of(
                        "page", "cart",
                        "endpoint", "GET /api/cart",
                        "items", List.of(),
                        "message", "You are on Cart page. Cart retrieval implementation is planned for next phase."));
    }

    /**
     * Logic:
     * Adds one movie to shopping cart.
     *
     * Params:
     * JSON body with movieId and quantity.
     *
     * Return:
     * HTTP 501 Not Implemented for Phase 2 (contract only).
     */
    @PostMapping("/cart/items")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> body) {
        // Service Contract:
        cartService.addToCart((String) body.get("movieId"), (Integer) body.get("quantity"));

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of(
                        "page", "cart",
                        "endpoint", "POST /api/cart/items",
                        "message", "You are on Cart page. Add-to-cart implementation is planned for next phase."));
    }

    /**
     * Logic:
     * Updates quantity for one cart item.
     *
     * Params:
     * movieId path variable, quantity in request body.
     *
     * Return:
     * HTTP 501 Not Implemented for Phase 2 (contract only).
     */
    @PutMapping("/cart/items/{movieId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable String movieId,
            @RequestBody Map<String, Object> body) {
        // Service Contract:
        cartService.updateCartItem(movieId, (Integer) body.get("quantity"));

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of(
                        "page", "cart",
                        "endpoint", "PUT /api/cart/items/{movieId}",
                        "movieId", movieId,
                        "message", "You are on Cart page. Update quantity implementation is planned for next phase."));
    }

    /**
     * Logic:
     * Removes one movie from shopping cart.
     *
     * Params:
     * movieId path variable.
     *
     * Return:
     * HTTP 501 Not Implemented for Phase 2 (contract only).
     */
    @DeleteMapping("/cart/items/{movieId}")
    public ResponseEntity<?> removeCartItem(@PathVariable String movieId) {
        // Service Contract:
        cartService.removeCartItem(movieId);

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of(
                        "page", "cart",
                        "endpoint", "DELETE /api/cart/items/{movieId}",
                        "movieId", movieId,
                        "message", "You are on Cart page. Remove item implementation is planned for next phase."));
    }
}
