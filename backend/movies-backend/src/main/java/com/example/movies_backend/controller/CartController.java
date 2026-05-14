package com.example.movies_backend.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.movies_backend.dto.CartItemRequestDTO;
import com.example.movies_backend.service.CartService;

@RestController
@RequestMapping("/api")
@CrossOrigin(
        originPatterns = {
                "http://localhost:*",
                "http://127.0.0.1:*",
                "https://localhost:*",
                "https://127.0.0.1:*"
        },
        allowCredentials = "true")
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
    public ResponseEntity<?> getCart(HttpSession session) {
        return ResponseEntity.ok(cartService.getCart(session));
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
    public ResponseEntity<?> addToCart(
            @RequestBody CartItemRequestDTO request,
            HttpSession session) {

        return ResponseEntity.ok(
                cartService.addToCart(
                        request.getMovieId(),
                        request.getQuantity(),
                        session));
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
            @RequestBody Map<String, Object> body,
            HttpSession session) {

Integer quantity = Integer.parseInt(body.get("quantity").toString());
        return ResponseEntity.ok(
                cartService.updateCartItem(movieId, quantity, session));
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
    public ResponseEntity<?> removeCartItem(
            @PathVariable String movieId,
            HttpSession session) {

        return ResponseEntity.ok(
                cartService.removeCartItem(movieId, session));
    }
}
