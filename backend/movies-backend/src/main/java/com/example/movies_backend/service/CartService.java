package com.example.movies_backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import com.example.movies_backend.dto.SingleMovieDTO;

import jakarta.servlet.http.HttpSession;

@Service
public class CartService {
// In-memory cart storage for demonstration purposes (session-based)
    private static final String CART_SESSION_KEY = "cart";
   private final ObjectProvider<MovieService> movieServiceProvider;

    public CartService(ObjectProvider<MovieService> movieServiceProvider) {
        this.movieServiceProvider = movieServiceProvider;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> getOrCreateCart(HttpSession session) {
        if (session == null) {
            return new HashMap<>();
        }

        Object cartObj = session.getAttribute(CART_SESSION_KEY);

        if (cartObj == null) {
            Map<String, Integer> cart = new HashMap<>();
            session.setAttribute(CART_SESSION_KEY, cart);
            return cart;
        }

        return (Map<String, Integer>) cartObj;
    }

    public void clearCart(HttpSession session) {
        if (session != null) {
            session.removeAttribute(CART_SESSION_KEY);
        }
    }
    /**
     * Logic:
     * Returns current shopping cart data for active session/user.
     *
     * Params:
     * None (session is managed by Spring and passed as parameter).
     *
     * Return:
     * Map containing list of cart items and total item count (planned for next phase).
     */
    public Map<String, Object> getCart(HttpSession session) {
        Map<String, Integer> cart = getOrCreateCart(session);
        MovieService movieService = movieServiceProvider.getIfAvailable();

        List<Map<String, Object>> items = new ArrayList<>();
        int totalItems = 0;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String movieId = entry.getKey();
            Integer quantity = entry.getValue();

            Map<String, Object> item = new HashMap<>();
            item.put("movieId", movieId);
            item.put("quantity", quantity);

            if (movieService != null) {
SingleMovieDTO movie = movieService.getMovieById(movieId);                if (movie != null) {
                    item.put("title", movie.getTitle());
                    item.put("year", movie.getYear());
                    item.put("director", movie.getDirector());
                }
            }

            items.add(item);
            totalItems += quantity;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("totalItems", totalItems);
        response.put("total", totalItems);

        return response;
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
    public Map<String, Object> addToCart(String movieId, Integer quantity, HttpSession session) {
        Map<String, Integer> cart = getOrCreateCart(session);

        if (movieId == null || movieId.isBlank()) {
            return Map.of("success", false, "message", "movieId is required");
        }

        if (quantity == null || quantity <= 0) {
            return Map.of("success", false, "message", "quantity must be greater than 0");
        }

        cart.put(movieId, cart.getOrDefault(movieId, 0) + quantity);
        if (session != null) {
            session.setAttribute(CART_SESSION_KEY, cart);
        }

        Map<String, Object> item = new HashMap<>();
        item.put("movieId", movieId);
        item.put("quantity", cart.get(movieId));

        return Map.of(
                "success", true,
                "message", "Movie added to cart",
                "movieId", movieId,
                "quantity", cart.get(movieId),
                "item", item);
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
    public Map<String, Object> updateCartItem(String movieId, Integer quantity, HttpSession session) {
        Map<String, Integer> cart = getOrCreateCart(session);

        if (quantity == null) {
            return Map.of("success", false, "message", "quantity is required");
        }

        if (quantity <= 0) {
            cart.remove(movieId);
            if (session != null) {
                session.setAttribute(CART_SESSION_KEY, cart);
            }

            return Map.of(
                    "success", true,
                    "message", "Movie removed from cart",
                    "movieId", movieId,
                    "removedMovieId", movieId);
        }

        cart.put(movieId, quantity);
        if (session != null) {
            session.setAttribute(CART_SESSION_KEY, cart);
        }

        Map<String, Object> item = new HashMap<>();
        item.put("movieId", movieId);
        item.put("quantity", quantity);

        return Map.of(
                "success", true,
                "message", "Cart item updated",
                "movieId", movieId,
                "quantity", quantity,
                "item", item);
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
    public Map<String, Object> removeCartItem(String movieId, HttpSession session) {
        Map<String, Integer> cart = getOrCreateCart(session);

        cart.remove(movieId);
        if (session != null) {
            session.setAttribute(CART_SESSION_KEY, cart);
        }

        return Map.of(
                "success", true,
                "message", "Movie removed from cart",
                "movieId", movieId,
                "removedMovieId", movieId);
    }
}
